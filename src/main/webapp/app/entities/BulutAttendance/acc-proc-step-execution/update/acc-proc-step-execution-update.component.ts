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
import { IAccountingProcedureExecution } from 'app/entities/BulutAttendance/accounting-procedure-execution/accounting-procedure-execution.model';
import { AccountingProcedureExecutionService } from 'app/entities/BulutAttendance/accounting-procedure-execution/service/accounting-procedure-execution.service';
import { IAccProcStep } from 'app/entities/BulutAttendance/acc-proc-step/acc-proc-step.model';
import { AccProcStepService } from 'app/entities/BulutAttendance/acc-proc-step/service/acc-proc-step.service';
import { AccProcStepExecutionService } from '../service/acc-proc-step-execution.service';
import { IAccProcStepExecution } from '../acc-proc-step-execution.model';
import { AccProcStepExecutionFormService, AccProcStepExecutionFormGroup } from './acc-proc-step-execution-form.service';

@Component({
  standalone: true,
  selector: 'jhi-acc-proc-step-execution-update',
  templateUrl: './acc-proc-step-execution-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccProcStepExecutionUpdateComponent implements OnInit {
  isSaving = false;
  accProcStepExecution: IAccProcStepExecution | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountHesabsSharedCollection: IAccountHesab[] = [];
  accountingProcedureExecutionsSharedCollection: IAccountingProcedureExecution[] = [];
  accProcStepsSharedCollection: IAccProcStep[] = [];

  protected accProcStepExecutionService = inject(AccProcStepExecutionService);
  protected accProcStepExecutionFormService = inject(AccProcStepExecutionFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected accountHesabService = inject(AccountHesabService);
  protected accountingProcedureExecutionService = inject(AccountingProcedureExecutionService);
  protected accProcStepService = inject(AccProcStepService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccProcStepExecutionFormGroup = this.accProcStepExecutionFormService.createAccProcStepExecutionFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountHesab = (o1: IAccountHesab | null, o2: IAccountHesab | null): boolean =>
    this.accountHesabService.compareAccountHesab(o1, o2);

  compareAccountingProcedureExecution = (o1: IAccountingProcedureExecution | null, o2: IAccountingProcedureExecution | null): boolean =>
    this.accountingProcedureExecutionService.compareAccountingProcedureExecution(o1, o2);

  compareAccProcStep = (o1: IAccProcStep | null, o2: IAccProcStep | null): boolean => this.accProcStepService.compareAccProcStep(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accProcStepExecution }) => {
      this.accProcStepExecution = accProcStepExecution;
      if (accProcStepExecution) {
        this.updateForm(accProcStepExecution);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accProcStepExecution = this.accProcStepExecutionFormService.getAccProcStepExecution(this.editForm);
    if (accProcStepExecution.id !== null) {
      this.subscribeToSaveResponse(this.accProcStepExecutionService.update(accProcStepExecution));
    } else {
      this.subscribeToSaveResponse(this.accProcStepExecutionService.create(accProcStepExecution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccProcStepExecution>>): void {
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

  protected updateForm(accProcStepExecution: IAccProcStepExecution): void {
    this.accProcStepExecution = accProcStepExecution;
    this.accProcStepExecutionFormService.resetForm(this.editForm, accProcStepExecution);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accProcStepExecution.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accProcStepExecution.company,
    );
    this.accountHesabsSharedCollection = this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
      this.accountHesabsSharedCollection,
      accProcStepExecution.creditAccount,
      accProcStepExecution.debitAccount,
    );
    this.accountingProcedureExecutionsSharedCollection =
      this.accountingProcedureExecutionService.addAccountingProcedureExecutionToCollectionIfMissing<IAccountingProcedureExecution>(
        this.accountingProcedureExecutionsSharedCollection,
        accProcStepExecution.procedure,
      );
    this.accProcStepsSharedCollection = this.accProcStepService.addAccProcStepToCollectionIfMissing<IAccProcStep>(
      this.accProcStepsSharedCollection,
      accProcStepExecution.step,
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
            this.accProcStepExecution?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accProcStepExecution?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.accountHesabService
      .query()
      .pipe(map((res: HttpResponse<IAccountHesab[]>) => res.body ?? []))
      .pipe(
        map((accountHesabs: IAccountHesab[]) =>
          this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
            accountHesabs,
            this.accProcStepExecution?.creditAccount,
            this.accProcStepExecution?.debitAccount,
          ),
        ),
      )
      .subscribe((accountHesabs: IAccountHesab[]) => (this.accountHesabsSharedCollection = accountHesabs));

    this.accountingProcedureExecutionService
      .query()
      .pipe(map((res: HttpResponse<IAccountingProcedureExecution[]>) => res.body ?? []))
      .pipe(
        map((accountingProcedureExecutions: IAccountingProcedureExecution[]) =>
          this.accountingProcedureExecutionService.addAccountingProcedureExecutionToCollectionIfMissing<IAccountingProcedureExecution>(
            accountingProcedureExecutions,
            this.accProcStepExecution?.procedure,
          ),
        ),
      )
      .subscribe(
        (accountingProcedureExecutions: IAccountingProcedureExecution[]) =>
          (this.accountingProcedureExecutionsSharedCollection = accountingProcedureExecutions),
      );

    this.accProcStepService
      .query()
      .pipe(map((res: HttpResponse<IAccProcStep[]>) => res.body ?? []))
      .pipe(
        map((accProcSteps: IAccProcStep[]) =>
          this.accProcStepService.addAccProcStepToCollectionIfMissing<IAccProcStep>(accProcSteps, this.accProcStepExecution?.step),
        ),
      )
      .subscribe((accProcSteps: IAccProcStep[]) => (this.accProcStepsSharedCollection = accProcSteps));
  }
}
