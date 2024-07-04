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
import { OrgPositionService } from '../service/org-position.service';
import { IOrgPosition } from '../org-position.model';
import { OrgPositionFormService, OrgPositionFormGroup } from './org-position-form.service';

@Component({
  standalone: true,
  selector: 'jhi-org-position-update',
  templateUrl: './org-position-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrgPositionUpdateComponent implements OnInit {
  isSaving = false;
  orgPosition: IOrgPosition | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected orgPositionService = inject(OrgPositionService);
  protected orgPositionFormService = inject(OrgPositionFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrgPositionFormGroup = this.orgPositionFormService.createOrgPositionFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgPosition }) => {
      this.orgPosition = orgPosition;
      if (orgPosition) {
        this.updateForm(orgPosition);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgPosition = this.orgPositionFormService.getOrgPosition(this.editForm);
    if (orgPosition.id !== null) {
      this.subscribeToSaveResponse(this.orgPositionService.update(orgPosition));
    } else {
      this.subscribeToSaveResponse(this.orgPositionService.create(orgPosition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgPosition>>): void {
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

  protected updateForm(orgPosition: IOrgPosition): void {
    this.orgPosition = orgPosition;
    this.orgPositionFormService.resetForm(this.editForm, orgPosition);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      orgPosition.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      orgPosition.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      orgPosition.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.orgPosition?.status),
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
            this.orgPosition?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.orgPosition?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
