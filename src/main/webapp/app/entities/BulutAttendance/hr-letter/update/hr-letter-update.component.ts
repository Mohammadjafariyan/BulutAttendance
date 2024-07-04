import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IHrLetterType } from 'app/entities/BulutAttendance/hr-letter-type/hr-letter-type.model';
import { HrLetterTypeService } from 'app/entities/BulutAttendance/hr-letter-type/service/hr-letter-type.service';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IOrgPosition } from 'app/entities/BulutAttendance/org-position/org-position.model';
import { OrgPositionService } from 'app/entities/BulutAttendance/org-position/service/org-position.service';
import { IOrgUnit } from 'app/entities/BulutAttendance/org-unit/org-unit.model';
import { OrgUnitService } from 'app/entities/BulutAttendance/org-unit/service/org-unit.service';
import { IPersonnelStatus } from 'app/entities/BulutAttendance/personnel-status/personnel-status.model';
import { PersonnelStatusService } from 'app/entities/BulutAttendance/personnel-status/service/personnel-status.service';
import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { HrLetterParameterService } from 'app/entities/BulutAttendance/hr-letter-parameter/service/hr-letter-parameter.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { HrLetterService } from '../service/hr-letter.service';
import { IHrLetter } from '../hr-letter.model';
import { HrLetterFormService, HrLetterFormGroup } from './hr-letter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-update',
  templateUrl: './hr-letter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HrLetterUpdateComponent implements OnInit {
  isSaving = false;
  hrLetter: IHrLetter | null = null;

  recordStatusesSharedCollection: IRecordStatus[] = [];
  hrLetterTypesSharedCollection: IHrLetterType[] = [];
  personnelSharedCollection: IPersonnel[] = [];
  orgPositionsSharedCollection: IOrgPosition[] = [];
  orgUnitsSharedCollection: IOrgUnit[] = [];
  personnelStatusesSharedCollection: IPersonnelStatus[] = [];
  hrLetterParametersSharedCollection: IHrLetterParameter[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  protected hrLetterService = inject(HrLetterService);
  protected hrLetterFormService = inject(HrLetterFormService);
  protected recordStatusService = inject(RecordStatusService);
  protected hrLetterTypeService = inject(HrLetterTypeService);
  protected personnelService = inject(PersonnelService);
  protected orgPositionService = inject(OrgPositionService);
  protected orgUnitService = inject(OrgUnitService);
  protected personnelStatusService = inject(PersonnelStatusService);
  protected hrLetterParameterService = inject(HrLetterParameterService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HrLetterFormGroup = this.hrLetterFormService.createHrLetterFormGroup();

  compareRecordStatus = (o1: IRecordStatus | null, o2: IRecordStatus | null): boolean =>
    this.recordStatusService.compareRecordStatus(o1, o2);

  compareHrLetterType = (o1: IHrLetterType | null, o2: IHrLetterType | null): boolean =>
    this.hrLetterTypeService.compareHrLetterType(o1, o2);

  comparePersonnel = (o1: IPersonnel | null, o2: IPersonnel | null): boolean => this.personnelService.comparePersonnel(o1, o2);

  compareOrgPosition = (o1: IOrgPosition | null, o2: IOrgPosition | null): boolean => this.orgPositionService.compareOrgPosition(o1, o2);

  compareOrgUnit = (o1: IOrgUnit | null, o2: IOrgUnit | null): boolean => this.orgUnitService.compareOrgUnit(o1, o2);

  comparePersonnelStatus = (o1: IPersonnelStatus | null, o2: IPersonnelStatus | null): boolean =>
    this.personnelStatusService.comparePersonnelStatus(o1, o2);

  compareHrLetterParameter = (o1: IHrLetterParameter | null, o2: IHrLetterParameter | null): boolean =>
    this.hrLetterParameterService.compareHrLetterParameter(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hrLetter }) => {
      this.hrLetter = hrLetter;
      if (hrLetter) {
        this.updateForm(hrLetter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hrLetter = this.hrLetterFormService.getHrLetter(this.editForm);
    if (hrLetter.id !== null) {
      this.subscribeToSaveResponse(this.hrLetterService.update(hrLetter));
    } else {
      this.subscribeToSaveResponse(this.hrLetterService.create(hrLetter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHrLetter>>): void {
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

  protected updateForm(hrLetter: IHrLetter): void {
    this.hrLetter = hrLetter;
    this.hrLetterFormService.resetForm(this.editForm, hrLetter);

    this.recordStatusesSharedCollection = this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(
      this.recordStatusesSharedCollection,
      hrLetter.status,
    );
    this.hrLetterTypesSharedCollection = this.hrLetterTypeService.addHrLetterTypeToCollectionIfMissing<IHrLetterType>(
      this.hrLetterTypesSharedCollection,
      hrLetter.type,
    );
    this.personnelSharedCollection = this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(
      this.personnelSharedCollection,
      hrLetter.personnelId,
    );
    this.orgPositionsSharedCollection = this.orgPositionService.addOrgPositionToCollectionIfMissing<IOrgPosition>(
      this.orgPositionsSharedCollection,
      hrLetter.orgPosition,
    );
    this.orgUnitsSharedCollection = this.orgUnitService.addOrgUnitToCollectionIfMissing<IOrgUnit>(
      this.orgUnitsSharedCollection,
      hrLetter.orgUnit,
    );
    this.personnelStatusesSharedCollection = this.personnelStatusService.addPersonnelStatusToCollectionIfMissing<IPersonnelStatus>(
      this.personnelStatusesSharedCollection,
      hrLetter.personnelStatus,
    );
    this.hrLetterParametersSharedCollection = this.hrLetterParameterService.addHrLetterParameterToCollectionIfMissing<IHrLetterParameter>(
      this.hrLetterParametersSharedCollection,
      hrLetter.hrLetterParameter,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      hrLetter.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      hrLetter.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recordStatusService
      .query()
      .pipe(map((res: HttpResponse<IRecordStatus[]>) => res.body ?? []))
      .pipe(
        map((recordStatuses: IRecordStatus[]) =>
          this.recordStatusService.addRecordStatusToCollectionIfMissing<IRecordStatus>(recordStatuses, this.hrLetter?.status),
        ),
      )
      .subscribe((recordStatuses: IRecordStatus[]) => (this.recordStatusesSharedCollection = recordStatuses));

    this.hrLetterTypeService
      .query()
      .pipe(map((res: HttpResponse<IHrLetterType[]>) => res.body ?? []))
      .pipe(
        map((hrLetterTypes: IHrLetterType[]) =>
          this.hrLetterTypeService.addHrLetterTypeToCollectionIfMissing<IHrLetterType>(hrLetterTypes, this.hrLetter?.type),
        ),
      )
      .subscribe((hrLetterTypes: IHrLetterType[]) => (this.hrLetterTypesSharedCollection = hrLetterTypes));

    this.personnelService
      .query()
      .pipe(map((res: HttpResponse<IPersonnel[]>) => res.body ?? []))
      .pipe(
        map((personnel: IPersonnel[]) =>
          this.personnelService.addPersonnelToCollectionIfMissing<IPersonnel>(personnel, this.hrLetter?.personnelId),
        ),
      )
      .subscribe((personnel: IPersonnel[]) => (this.personnelSharedCollection = personnel));

    this.orgPositionService
      .query()
      .pipe(map((res: HttpResponse<IOrgPosition[]>) => res.body ?? []))
      .pipe(
        map((orgPositions: IOrgPosition[]) =>
          this.orgPositionService.addOrgPositionToCollectionIfMissing<IOrgPosition>(orgPositions, this.hrLetter?.orgPosition),
        ),
      )
      .subscribe((orgPositions: IOrgPosition[]) => (this.orgPositionsSharedCollection = orgPositions));

    this.orgUnitService
      .query()
      .pipe(map((res: HttpResponse<IOrgUnit[]>) => res.body ?? []))
      .pipe(map((orgUnits: IOrgUnit[]) => this.orgUnitService.addOrgUnitToCollectionIfMissing<IOrgUnit>(orgUnits, this.hrLetter?.orgUnit)))
      .subscribe((orgUnits: IOrgUnit[]) => (this.orgUnitsSharedCollection = orgUnits));

    this.personnelStatusService
      .query()
      .pipe(map((res: HttpResponse<IPersonnelStatus[]>) => res.body ?? []))
      .pipe(
        map((personnelStatuses: IPersonnelStatus[]) =>
          this.personnelStatusService.addPersonnelStatusToCollectionIfMissing<IPersonnelStatus>(
            personnelStatuses,
            this.hrLetter?.personnelStatus,
          ),
        ),
      )
      .subscribe((personnelStatuses: IPersonnelStatus[]) => (this.personnelStatusesSharedCollection = personnelStatuses));

    this.hrLetterParameterService
      .query()
      .pipe(map((res: HttpResponse<IHrLetterParameter[]>) => res.body ?? []))
      .pipe(
        map((hrLetterParameters: IHrLetterParameter[]) =>
          this.hrLetterParameterService.addHrLetterParameterToCollectionIfMissing<IHrLetterParameter>(
            hrLetterParameters,
            this.hrLetter?.hrLetterParameter,
          ),
        ),
      )
      .subscribe((hrLetterParameters: IHrLetterParameter[]) => (this.hrLetterParametersSharedCollection = hrLetterParameters));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.hrLetter?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.hrLetter?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
