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
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IBank } from 'app/entities/BulutAttendance/bank/bank.model';
import { BankService } from 'app/entities/BulutAttendance/bank/service/bank.service';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountLevelInTree } from 'app/entities/enumerations/account-level-in-tree.model';
import { AccountingFormulaType } from 'app/entities/enumerations/accounting-formula-type.model';
import { AccountHesabService } from '../service/account-hesab.service';
import { IAccountHesab } from '../account-hesab.model';
import { AccountHesabFormService, AccountHesabFormGroup } from './account-hesab-form.service';

@Component({
  standalone: true,
  selector: 'jhi-account-hesab-update',
  templateUrl: './account-hesab-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountHesabUpdateComponent implements OnInit {
  isSaving = false;
  accountHesab: IAccountHesab | null = null;
  accountTypeValues = Object.keys(AccountType);
  accountLevelInTreeValues = Object.keys(AccountLevelInTree);
  accountingFormulaTypeValues = Object.keys(AccountingFormulaType);

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountHesabsSharedCollection: IAccountHesab[] = [];
  personnelSharedCollection: IPersonnel[] = [];
  banksSharedCollection: IBank[] = [];

  protected accountHesabService = inject(AccountHesabService);
  protected accountHesabFormService = inject(AccountHesabFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected personnelService = inject(PersonnelService);
  protected bankService = inject(BankService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountHesabFormGroup = this.accountHesabFormService.createAccountHesabFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountHesab = (o1: IAccountHesab | null, o2: IAccountHesab | null): boolean =>
    this.accountHesabService.compareAccountHesab(o1, o2);

  comparePersonnel = (o1: IPersonnel | null, o2: IPersonnel | null): boolean => this.personnelService.comparePersonnel(o1, o2);

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountHesab }) => {
      this.accountHesab = accountHesab;
      if (accountHesab) {
        this.updateForm(accountHesab);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountHesab = this.accountHesabFormService.getAccountHesab(this.editForm);
    if (accountHesab.id !== null) {
      this.subscribeToSaveResponse(this.accountHesabService.update(accountHesab));
    } else {
      this.subscribeToSaveResponse(this.accountHesabService.create(accountHesab));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountHesab>>): void {
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

  protected updateForm(accountHesab: IAccountHesab): void {
    this.accountHesab = accountHesab;
    this.accountHesabFormService.resetForm(this.editForm, accountHesab);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      accountHesab.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accountHesab.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accountHesab.company,
    );
    this.accountHesabsSharedCollection = this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
      this.accountHesabsSharedCollection,
      accountHesab.parentAccountId,
    );
    this.personnelSharedCollection = this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(
      this.personnelSharedCollection,
      accountHesab.personnelId,
    );
    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(this.banksSharedCollection, accountHesab.bank);
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.accountHesab?.status),
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
            this.accountHesab?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accountHesab?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.accountHesabService
      .query()
      .pipe(map((res: HttpResponse<IAccountHesab[]>) => res.body ?? []))
      .pipe(
        map((accountHesabs: IAccountHesab[]) =>
          this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(accountHesabs, this.accountHesab?.parentAccountId),
        ),
      )
      .subscribe((accountHesabs: IAccountHesab[]) => (this.accountHesabsSharedCollection = accountHesabs));

    this.personnelService
      .query()
      .pipe(map((res: HttpResponse<IPersonnel[]>) => res.body ?? []))
      .pipe(
        map((personnel: IPersonnel[]) =>
          this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(personnel, this.accountHesab?.personnelId),
        ),
      )
      .subscribe((personnel: IPersonnel[]) => (this.personnelSharedCollection = personnel));

    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(map((banks: IBank[]) => this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.accountHesab?.bank)))
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));
  }
}
