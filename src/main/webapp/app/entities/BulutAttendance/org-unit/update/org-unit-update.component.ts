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
import { OrgUnitService } from '../service/org-unit.service';
import { IOrgUnit } from '../org-unit.model';
import { OrgUnitFormService, OrgUnitFormGroup } from './org-unit-form.service';

@Component({
  standalone: true,
  selector: 'jhi-org-unit-update',
  templateUrl: './org-unit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrgUnitUpdateComponent implements OnInit {
  isSaving = false;
  orgUnit: IOrgUnit | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  orgUnitsSharedCollection: IOrgUnit[] = [];

  protected orgUnitService = inject(OrgUnitService);
  protected orgUnitFormService = inject(OrgUnitFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrgUnitFormGroup = this.orgUnitFormService.createOrgUnitFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareOrgUnit = (o1: IOrgUnit | null, o2: IOrgUnit | null): boolean => this.orgUnitService.compareOrgUnit(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgUnit }) => {
      this.orgUnit = orgUnit;
      if (orgUnit) {
        this.updateForm(orgUnit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgUnit = this.orgUnitFormService.getOrgUnit(this.editForm);
    if (orgUnit.id !== null) {
      this.subscribeToSaveResponse(this.orgUnitService.update(orgUnit));
    } else {
      this.subscribeToSaveResponse(this.orgUnitService.create(orgUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUnit>>): void {
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

  protected updateForm(orgUnit: IOrgUnit): void {
    this.orgUnit = orgUnit;
    this.orgUnitFormService.resetForm(this.editForm, orgUnit);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      orgUnit.status,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      orgUnit.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      orgUnit.company,
    );
    this.orgUnitsSharedCollection = this.orgUnitService.addOrgUnitToCollectionIfMissing<IOrgUnit>(
      this.orgUnitsSharedCollection,
      orgUnit.parent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.orgUnit?.status),
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
            this.orgUnit?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.orgUnit?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.orgUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrgUnit[]>) => res.body ?? []))
      .pipe(map((orgUnits: IOrgUnit[]) => this.orgUnitService.addOrgUnitToCollectionIfMissing<IOrgUnit>(orgUnits, this.orgUnit?.parent)))
      .subscribe((orgUnits: IOrgUnit[]) => (this.orgUnitsSharedCollection = orgUnits));
  }
}
