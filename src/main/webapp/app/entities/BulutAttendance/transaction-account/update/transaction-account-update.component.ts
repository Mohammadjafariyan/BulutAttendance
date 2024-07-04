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
import { ITransaction } from 'app/entities/BulutAttendance/transaction/transaction.model';
import { TransactionService } from 'app/entities/BulutAttendance/transaction/service/transaction.service';
import { TransactionAccountService } from '../service/transaction-account.service';
import { ITransactionAccount } from '../transaction-account.model';
import { TransactionAccountFormService, TransactionAccountFormGroup } from './transaction-account-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transaction-account-update',
  templateUrl: './transaction-account-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionAccountUpdateComponent implements OnInit {
  isSaving = false;
  transactionAccount: ITransactionAccount | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  accountHesabsSharedCollection: IAccountHesab[] = [];
  transactionsSharedCollection: ITransaction[] = [];

  protected transactionAccountService = inject(TransactionAccountService);
  protected transactionAccountFormService = inject(TransactionAccountFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected accountHesabService = inject(AccountHesabService);
  protected transactionService = inject(TransactionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionAccountFormGroup = this.transactionAccountFormService.createTransactionAccountFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareAccountHesab = (o1: IAccountHesab | null, o2: IAccountHesab | null): boolean =>
    this.accountHesabService.compareAccountHesab(o1, o2);

  compareTransaction = (o1: ITransaction | null, o2: ITransaction | null): boolean => this.transactionService.compareTransaction(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccount }) => {
      this.transactionAccount = transactionAccount;
      if (transactionAccount) {
        this.updateForm(transactionAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionAccount = this.transactionAccountFormService.getTransactionAccount(this.editForm);
    if (transactionAccount.id !== null) {
      this.subscribeToSaveResponse(this.transactionAccountService.update(transactionAccount));
    } else {
      this.subscribeToSaveResponse(this.transactionAccountService.create(transactionAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionAccount>>): void {
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

  protected updateForm(transactionAccount: ITransactionAccount): void {
    this.transactionAccount = transactionAccount;
    this.transactionAccountFormService.resetForm(this.editForm, transactionAccount);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      transactionAccount.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      transactionAccount.company,
    );
    this.accountHesabsSharedCollection = this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(
      this.accountHesabsSharedCollection,
      transactionAccount.account,
    );
    this.transactionsSharedCollection = this.transactionService.addTransactionToCollectionIfMissing<ITransaction>(
      this.transactionsSharedCollection,
      transactionAccount.transaction,
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
            this.transactionAccount?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.transactionAccount?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.accountHesabService
      .query()
      .pipe(map((res: HttpResponse<IAccountHesab[]>) => res.body ?? []))
      .pipe(
        map((accountHesabs: IAccountHesab[]) =>
          this.accountHesabService.addAccountHesabToCollectionIfMissing<IAccountHesab>(accountHesabs, this.transactionAccount?.account),
        ),
      )
      .subscribe((accountHesabs: IAccountHesab[]) => (this.accountHesabsSharedCollection = accountHesabs));

    this.transactionService
      .query()
      .pipe(map((res: HttpResponse<ITransaction[]>) => res.body ?? []))
      .pipe(
        map((transactions: ITransaction[]) =>
          this.transactionService.addTransactionToCollectionIfMissing<ITransaction>(transactions, this.transactionAccount?.transaction),
        ),
      )
      .subscribe((transactions: ITransaction[]) => (this.transactionsSharedCollection = transactions));
  }
}
