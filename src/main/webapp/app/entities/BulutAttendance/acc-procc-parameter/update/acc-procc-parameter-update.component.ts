import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
import { AccProccParameterService } from '../service/acc-procc-parameter.service';
import { IAccProccParameter } from '../acc-procc-parameter.model';
import { AccProccParameterFormService, AccProccParameterFormGroup } from './acc-procc-parameter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-acc-procc-parameter-update',
  templateUrl: './acc-procc-parameter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccProccParameterUpdateComponent implements OnInit {
  isSaving = false;
  accProccParameter: IAccProccParameter | null = null;
  calcTypeValues = Object.keys(CalcType);
  calcUnitValues = Object.keys(CalcUnit);
  laborTimeValues = Object.keys(LaborTime);
  hokmValues = Object.keys(Hokm);
  earningValues = Object.keys(Earning);
  deductionValues = Object.keys(Deduction);

  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected accProccParameterService = inject(AccProccParameterService);
  protected accProccParameterFormService = inject(AccProccParameterFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccProccParameterFormGroup = this.accProccParameterFormService.createAccProccParameterFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accProccParameter }) => {
      this.accProccParameter = accProccParameter;
      if (accProccParameter) {
        this.updateForm(accProccParameter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accProccParameter = this.accProccParameterFormService.getAccProccParameter(this.editForm);
    if (accProccParameter.id !== null) {
      this.subscribeToSaveResponse(this.accProccParameterService.update(accProccParameter));
    } else {
      this.subscribeToSaveResponse(this.accProccParameterService.create(accProccParameter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccProccParameter>>): void {
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

  protected updateForm(accProccParameter: IAccProccParameter): void {
    this.accProccParameter = accProccParameter;
    this.accProccParameterFormService.resetForm(this.editForm, accProccParameter);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accProccParameter.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accProccParameter.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.accProccParameter?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accProccParameter?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
