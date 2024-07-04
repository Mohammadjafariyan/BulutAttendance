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
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { AccountHesabService } from 'app/entities/BulutAttendance/account-hesab/service/account-hesab.service';
import { IAccProccParameter } from 'app/entities/BulutAttendance/acc-procc-parameter/acc-procc-parameter.model';
import { AccProccParameterService } from 'app/entities/BulutAttendance/acc-procc-parameter/service/acc-procc-parameter.service';
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';
import { AccountingProcedureService } from 'app/entities/BulutAttendance/accounting-procedure/service/accounting-procedure.service';
import { AccProcStepService } from '../service/acc-proc-step.service';
import { IAccProcStep } from '../acc-proc-step.model';
import { AccProcStepFormService, AccProcStepFormGroup } from './acc-proc-step-form.service';

@Component({
  standalone: true,
  selector: 'jhi-acc-proc-step-update',
  templateUrl: './acc-proc-step-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccProcStepUpdateComponent implements OnInit {
  isSaving = false;
  accProcStep: IAccProcStep | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountHesabsSharedCollection: IAccountHesab[] = [];
  accProccParametersSharedCollection: IAccProccParameter[] = [];
  accountingProceduresSharedCollection: IAccountingProcedure[] = [];

  protected accProcStepService = inject(AccProcStepService);
  protected accProcStepFormService = inject(AccProcStepFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected accountHesabService = inject(AccountHesabService);
  protected accProccParameterService = inject(AccProccParameterService);
  protected accountingProcedureService = inject(AccountingProcedureService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccProcStepFormGroup = this.accProcStepFormService.createAccProcStepFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountHesab = (o1: IAccountHesab | null, o2: IAccountHesab | null): boolean =>
    this.accountHesabService.compareAccountHesab(o1, o2);

  compareAccProccParameter = (o1: IAccProccParameter | null, o2: IAccProccParameter | null): boolean =>
    this.accProccParameterService.compareAccProccParameter(o1, o2);

  compareAccountingProcedure = (o1: IAccountingProcedure | null, o2: IAccountingProcedure | null): boolean =>
    this.accountingProcedureService.compareAccountingProcedure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accProcStep }) => {
      this.accProcStep = accProcStep;
      if (accProcStep) {
        this.updateForm(accProcStep);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accProcStep = this.accProcStepFormService.getAccProcStep(this.editForm);
    if (accProcStep.id !== null) {
      this.subscribeToSaveResponse(this.accProcStepService.update(accProcStep));
    } else {
      this.subscribeToSaveResponse(this.accProcStepService.create(accProcStep));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccProcStep>>): void {
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

  protected updateForm(accProcStep: IAccProcStep): void {
    this.accProcStep = accProcStep;
    this.accProcStepFormService.resetForm(this.editForm, accProcStep);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accProcStep.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accProcStep.company,
    );
    this.accountHesabsSharedCollection = this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
      this.accountHesabsSharedCollection,
      accProcStep.creditAccount,
      accProcStep.debitAccount,
    );
    this.accProccParametersSharedCollection = this.accProccParameterService.addAccProccParameterToCollectionIfMissing<IAccProccParameter>(
      this.accProccParametersSharedCollection,
      accProcStep.parameter,
    );
    this.accountingProceduresSharedCollection =
      this.accountingProcedureService.addAccountingProcedureToCollectionIfMissing<IAccountingProcedure>(
        this.accountingProceduresSharedCollection,
        accProcStep.procedure,
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
            this.accProcStep?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accProcStep?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.accountHesabService
      .query()
      .pipe(map((res: HttpResponse<IAccountHesab[]>) => res.body ?? []))
      .pipe(
        map((accountHesabs: IAccountHesab[]) =>
          this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
            accountHesabs,
            this.accProcStep?.creditAccount,
            this.accProcStep?.debitAccount,
          ),
        ),
      )
      .subscribe((accountHesabs: IAccountHesab[]) => (this.accountHesabsSharedCollection = accountHesabs));

    this.accProccParameterService
      .query()
      .pipe(map((res: HttpResponse<IAccProccParameter[]>) => res.body ?? []))
      .pipe(
        map((accProccParameters: IAccProccParameter[]) =>
          this.accProccParameterService.addAccProccParameterToCollectionIfMissing<IAccProccParameter>(
            accProccParameters,
            this.accProcStep?.parameter,
          ),
        ),
      )
      .subscribe((accProccParameters: IAccProccParameter[]) => (this.accProccParametersSharedCollection = accProccParameters));

    this.accountingProcedureService
      .query()
      .pipe(map((res: HttpResponse<IAccountingProcedure[]>) => res.body ?? []))
      .pipe(
        map((accountingProcedures: IAccountingProcedure[]) =>
          this.accountingProcedureService.addAccountingProcedureToCollectionIfMissing<IAccountingProcedure>(
            accountingProcedures,
            this.accProcStep?.procedure,
          ),
        ),
      )
      .subscribe((accountingProcedures: IAccountingProcedure[]) => (this.accountingProceduresSharedCollection = accountingProcedures));
  }
}
