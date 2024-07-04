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
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountLevelInTree } from 'app/entities/enumerations/account-level-in-tree.model';
import { AccountingFormulaType } from 'app/entities/enumerations/accounting-formula-type.model';
import { AccountTemplateService } from '../service/account-template.service';
import { IAccountTemplate } from '../account-template.model';
import { AccountTemplateFormService, AccountTemplateFormGroup } from './account-template-form.service';

@Component({
  standalone: true,
  selector: 'jhi-account-template-update',
  templateUrl: './account-template-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountTemplateUpdateComponent implements OnInit {
  isSaving = false;
  accountTemplate: IAccountTemplate | null = null;
  accountTypeValues = Object.keys(AccountType);
  accountLevelInTreeValues = Object.keys(AccountLevelInTree);
  accountingFormulaTypeValues = Object.keys(AccountingFormulaType);

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected accountTemplateService = inject(AccountTemplateService);
  protected accountTemplateFormService = inject(AccountTemplateFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountTemplateFormGroup = this.accountTemplateFormService.createAccountTemplateFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountTemplate }) => {
      this.accountTemplate = accountTemplate;
      if (accountTemplate) {
        this.updateForm(accountTemplate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountTemplate = this.accountTemplateFormService.getAccountTemplate(this.editForm);
    if (accountTemplate.id !== null) {
      this.subscribeToSaveResponse(this.accountTemplateService.update(accountTemplate));
    } else {
      this.subscribeToSaveResponse(this.accountTemplateService.create(accountTemplate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountTemplate>>): void {
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

  protected updateForm(accountTemplate: IAccountTemplate): void {
    this.accountTemplate = accountTemplate;
    this.accountTemplateFormService.resetForm(this.editForm, accountTemplate);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      accountTemplate.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      accountTemplate.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      accountTemplate.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.accountTemplate?.status),
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
            this.accountTemplate?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.accountTemplate?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
