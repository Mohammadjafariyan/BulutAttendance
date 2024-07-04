import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { CalcType } from 'app/entities/enumerations/calc-type.model';
import { CalcUnit } from 'app/entities/enumerations/calc-unit.model';
import { LaborTime } from 'app/entities/enumerations/labor-time.model';
import { Hokm } from 'app/entities/enumerations/hokm.model';
import { Earning } from 'app/entities/enumerations/earning.model';
import { Deduction } from 'app/entities/enumerations/deduction.model';
import { HrLetterParameterService } from '../service/hr-letter-parameter.service';
import { IHrLetterParameter } from '../hr-letter-parameter.model';
import { HrLetterParameterFormService, HrLetterParameterFormGroup } from './hr-letter-parameter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-parameter-update',
  templateUrl: './hr-letter-parameter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HrLetterParameterUpdateComponent implements OnInit {
  isSaving = false;
  hrLetterParameter: IHrLetterParameter | null = null;
  calcTypeValues = Object.keys(CalcType);
  calcUnitValues = Object.keys(CalcUnit);
  laborTimeValues = Object.keys(LaborTime);
  hokmValues = Object.keys(Hokm);
  earningValues = Object.keys(Earning);
  deductionValues = Object.keys(Deduction);

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected hrLetterParameterService = inject(HrLetterParameterService);
  protected hrLetterParameterFormService = inject(HrLetterParameterFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HrLetterParameterFormGroup = this.hrLetterParameterFormService.createHrLetterParameterFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hrLetterParameter }) => {
      this.hrLetterParameter = hrLetterParameter;
      if (hrLetterParameter) {
        this.updateForm(hrLetterParameter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hrLetterParameter = this.hrLetterParameterFormService.getHrLetterParameter(this.editForm);
    if (hrLetterParameter.id !== null) {
      this.subscribeToSaveResponse(this.hrLetterParameterService.update(hrLetterParameter));
    } else {
      this.subscribeToSaveResponse(this.hrLetterParameterService.create(hrLetterParameter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHrLetterParameter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(hrLetterParameter: IHrLetterParameter): void {
    this.hrLetterParameter = hrLetterParameter;
    this.hrLetterParameterFormService.resetForm(this.editForm, hrLetterParameter);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      hrLetterParameter.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      hrLetterParameter.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      hrLetterParameter.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.hrLetterParameter?.status),
        ),
      )
      .subscribe((recordStatuses: IRecordStatus[]) => (this.recordStatusesSharedCollection = recordStatuses));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.hrLetterParameter?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.hrLetterParameter?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
