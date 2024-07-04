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
import { AccountingProcedureService } from '../service/accounting-procedure.service';
import { IAccountingProcedure } from '../accounting-procedure.model';
import { AccountingProcedureFormService, AccountingProcedureFormGroup } from './accounting-procedure-form.service';

@Component({
  standalone: true,
  selector: 'jhi-accounting-procedure-update',
  templateUrl: './accounting-procedure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountingProcedureUpdateComponent implements OnInit {
  isSaving = false;
  accountingProcedure: IAccountingProcedure | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountingProceduresSharedCollection: IAccountingProcedure[] = [];

  protected accountingProcedureService = inject(AccountingProcedureService);
  protected accountingProcedureFormService = inject(AccountingProcedureFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountingProcedureFormGroup = this.accountingProcedureFormService.createAccountingProcedureFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountingProcedure = (o1: IAccountingProcedure | null, o2: IAccountingProcedure | null): boolean =>
    this.accountingProcedureService.compareAccountingProcedure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountingProcedure }) => {
      this.accountingProcedure = accountingProcedure;
      if (accountingProcedure) {
        this.updateForm(accountingProcedure);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountingProcedure = this.accountingProcedureFormService.getAccountingProcedure(this.editForm);
    if (accountingProcedure.id !== null) {
      this.subscribeToSaveResponse(this.accountingProcedureService.update(accountingProcedure));
    } else {
      this.subscribeToSaveResponse(this.accountingProcedureService.create(accountingProcedure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountingProcedure>>): void {
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

  protected updateForm(accountingProcedure: IAccountingProcedure): void {
    this.accountingProcedure = accountingProcedure;
    this.accountingProcedureFormService.resetForm(this.editForm, accountingProcedure);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      accountingProcedure.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accountingProcedure.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accountingProcedure.company,
    );
    this.accountingProceduresSharedCollection =
      this.accountingProcedureService.addAccountingProcedureToCollectionIfMissing<IAccountingProcedure>(
        this.accountingProceduresSharedCollection,
        accountingProcedure.executeAfter,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.accountingProcedure?.status),
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
            this.accountingProcedure?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accountingProcedure?.company),
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
            this.accountingProcedure?.executeAfter,
          ),
        ),
      )
      .subscribe((accountingProcedures: IAccountingProcedure[]) => (this.accountingProceduresSharedCollection = accountingProcedures));
  }
}
