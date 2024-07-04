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
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';
import { AccountingProcedureService } from 'app/entities/BulutAttendance/accounting-procedure/service/accounting-procedure.service';
import { AccountingProcedureExecutionService } from '../service/accounting-procedure-execution.service';
import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';
import {
  AccountingProcedureExecutionFormService,
  AccountingProcedureExecutionFormGroup,
} from './accounting-procedure-execution-form.service';

@Component({
  standalone: true,
  selector: 'jhi-accounting-procedure-execution-update',
  templateUrl: './accounting-procedure-execution-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountingProcedureExecutionUpdateComponent implements OnInit {
  isSaving = false;
  accountingProcedureExecution: IAccountingProcedureExecution | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountingProceduresSharedCollection: IAccountingProcedure[] = [];

  protected accountingProcedureExecutionService = inject(AccountingProcedureExecutionService);
  protected accountingProcedureExecutionFormService = inject(AccountingProcedureExecutionFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected accountingProcedureService = inject(AccountingProcedureService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountingProcedureExecutionFormGroup =
    this.accountingProcedureExecutionFormService.createAccountingProcedureExecutionFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountingProcedure = (o1: IAccountingProcedure | null, o2: IAccountingProcedure | null): boolean =>
    this.accountingProcedureService.compareAccountingProcedure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountingProcedureExecution }) => {
      this.accountingProcedureExecution = accountingProcedureExecution;
      if (accountingProcedureExecution) {
        this.updateForm(accountingProcedureExecution);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountingProcedureExecution = this.accountingProcedureExecutionFormService.getAccountingProcedureExecution(this.editForm);
    if (accountingProcedureExecution.id !== null) {
      this.subscribeToSaveResponse(this.accountingProcedureExecutionService.update(accountingProcedureExecution));
    } else {
      this.subscribeToSaveResponse(this.accountingProcedureExecutionService.create(accountingProcedureExecution));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountingProcedureExecution>>): void {
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

  protected updateForm(accountingProcedureExecution: IAccountingProcedureExecution): void {
    this.accountingProcedureExecution = accountingProcedureExecution;
    this.accountingProcedureExecutionFormService.resetForm(this.editForm, accountingProcedureExecution);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accountingProcedureExecution.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accountingProcedureExecution.company,
    );
    this.accountingProceduresSharedCollection =
      this.accountingProcedureService.addAccountingProcedureToCollectionIfMissing<IAccountingProcedure>(
        this.accountingProceduresSharedCollection,
        accountingProcedureExecution.procedure,
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
            this.accountingProcedureExecution?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accountingProcedureExecution?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.accountingProcedureService
      .query()
      .pipe(map((res: HttpResponse<IAccountingProcedure[]>) => res.body ?? []))
      .pipe(
        map((accountingProcedures: IAccountingProcedure[]) =>
          this.accountingProcedureService.addAccountingProcedureToCollectionIfMissing<IAccountingProcedure>(
            accountingProcedures,
            this.accountingProcedureExecution?.procedure,
          ),
        ),
      )
      .subscribe((accountingProcedures: IAccountingProcedure[]) => (this.accountingProceduresSharedCollection = accountingProcedures));
  }
}
