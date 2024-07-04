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
import { PersonnelStatusService } from '../service/personnel-status.service';
import { IPersonnelStatus } from '../personnel-status.model';
import { PersonnelStatusFormService, PersonnelStatusFormGroup } from './personnel-status-form.service';

@Component({
  standalone: true,
  selector: 'jhi-personnel-status-update',
  templateUrl: './personnel-status-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonnelStatusUpdateComponent implements OnInit {
  isSaving = false;
  personnelStatus: IPersonnelStatus | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected personnelStatusService = inject(PersonnelStatusService);
  protected personnelStatusFormService = inject(PersonnelStatusFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PersonnelStatusFormGroup = this.personnelStatusFormService.createPersonnelStatusFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnelStatus }) => {
      this.personnelStatus = personnelStatus;
      if (personnelStatus) {
        this.updateForm(personnelStatus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personnelStatus = this.personnelStatusFormService.getPersonnelStatus(this.editForm);
    if (personnelStatus.id !== null) {
      this.subscribeToSaveResponse(this.personnelStatusService.update(personnelStatus));
    } else {
      this.subscribeToSaveResponse(this.personnelStatusService.create(personnelStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonnelStatus>>): void {
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

  protected updateForm(personnelStatus: IPersonnelStatus): void {
    this.personnelStatus = personnelStatus;
    this.personnelStatusFormService.resetForm(this.editForm, personnelStatus);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      personnelStatus.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      personnelStatus.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      personnelStatus.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.personnelStatus?.status),
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
            this.personnelStatus?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.personnelStatus?.company),
        ),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
