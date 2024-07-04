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
import { HrLetterTypeService } from '../service/hr-letter-type.service';
import { IHrLetterType } from '../hr-letter-type.model';
import { HrLetterTypeFormService, HrLetterTypeFormGroup } from './hr-letter-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-type-update',
  templateUrl: './hr-letter-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HrLetterTypeUpdateComponent implements OnInit {
  isSaving = false;
  hrLetterType: IHrLetterType | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected hrLetterTypeService = inject(HrLetterTypeService);
  protected hrLetterTypeFormService = inject(HrLetterTypeFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HrLetterTypeFormGroup = this.hrLetterTypeFormService.createHrLetterTypeFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hrLetterType }) => {
      this.hrLetterType = hrLetterType;
      if (hrLetterType) {
        this.updateForm(hrLetterType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hrLetterType = this.hrLetterTypeFormService.getHrLetterType(this.editForm);
    if (hrLetterType.id !== null) {
      this.subscribeToSaveResponse(this.hrLetterTypeService.update(hrLetterType));
    } else {
      this.subscribeToSaveResponse(this.hrLetterTypeService.create(hrLetterType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHrLetterType>>): void {
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

  protected updateForm(hrLetterType: IHrLetterType): void {
    this.hrLetterType = hrLetterType;
    this.hrLetterTypeFormService.resetForm(this.editForm, hrLetterType);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      hrLetterType.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      hrLetterType.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      hrLetterType.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.hrLetterType?.status),
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
            this.hrLetterType?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.hrLetterType?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
