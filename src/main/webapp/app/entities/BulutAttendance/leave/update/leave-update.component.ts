import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { LeaveService } from '../service/leave.service';
import { ILeave } from '../leave.model';
import { LeaveFormService, LeaveFormGroup } from './leave-form.service';

@Component({
  standalone: true,
  selector: 'jhi-leave-update',
  templateUrl: './leave-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LeaveUpdateComponent implements OnInit {
  isSaving = false;
  leave: ILeave | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  personnelSharedCollection: IPersonnel[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected leaveService = inject(LeaveService);
  protected leaveFormService = inject(LeaveFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected personnelService = inject(PersonnelService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LeaveFormGroup = this.leaveFormService.createLeaveFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  comparePersonnel = (o1: IPersonnel | null, o2: IPersonnel | null): boolean => this.personnelService.comparePersonnel(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leave }) => {
      this.leave = leave;
      if (leave) {
        this.updateForm(leave);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leave = this.leaveFormService.getLeave(this.editForm);
    if (leave.id !== null) {
      this.subscribeToSaveResponse(this.leaveService.update(leave));
    } else {
      this.subscribeToSaveResponse(this.leaveService.create(leave));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeave>>): void {
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

  protected updateForm(leave: ILeave): void {
    this.leave = leave;
    this.leaveFormService.resetForm(this.editForm, leave);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      leave.status,
    );
    this.personnelSharedCollection = this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(
      this.personnelSharedCollection,
      leave.personnelId,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      leave.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      leave.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.leave?.status),
        ),
      )
      .subscribe((recordStatuses: IRecordStatus[]) => (this.recordStatusesSharedCollection = recordStatuses));

    this.personnelService
      .query()
      .pipe(map((res: HttpResponse<IPersonnel[]>) => res.body ?? []))
      .pipe(
        map((personnel: IPersonnel[]) =>
          this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(personnel, this.leave?.personnelId),
        ),
      )
      .subscribe((personnel: IPersonnel[]) => (this.personnelSharedCollection = personnel));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(applicationUsers, this.leave?.internalUser),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.leave?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
