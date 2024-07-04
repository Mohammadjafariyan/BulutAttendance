import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { HrLetterParameterService } from 'app/entities/BulutAttendance/hr-letter-parameter/service/hr-letter-parameter.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IWork } from 'app/entities/BulutAttendance/work/work.model';
import { WorkService } from 'app/entities/BulutAttendance/work/service/work.service';
import { WorkItemService } from '../service/work-item.service';
import { IWorkItem } from '../work-item.model';
import { WorkItemFormService, WorkItemFormGroup } from './work-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-work-item-update',
  templateUrl: './work-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkItemUpdateComponent implements OnInit {
  isSaving = false;
  workItem: IWorkItem | null = null;

  hrLetterParametersCollection: IHrLetterParameter[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  companiesSharedCollection: ICompany[] = [];
  worksSharedCollection: IWork[] = [];

  protected workItemService = inject(WorkItemService);
  protected workItemFormService = inject(WorkItemFormService);
  protected hrLetterParameterService = inject(HrLetterParameterService);
  protected applicationUserService = inject(ApplicationUserService);
  protected companyService = inject(CompanyService);
  protected workService = inject(WorkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorkItemFormGroup = this.workItemFormService.createWorkItemFormGroup();

  compareHrLetterParameter = (o1: IHrLetterParameter | null, o2: IHrLetterParameter | null): boolean =>
    this.hrLetterParameterService.compareHrLetterParameter(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareWork = (o1: IWork | null, o2: IWork | null): boolean => this.workService.compareWork(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workItem }) => {
      this.workItem = workItem;
      if (workItem) {
        this.updateForm(workItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workItem = this.workItemFormService.getWorkItem(this.editForm);
    if (workItem.id !== null) {
      this.subscribeToSaveResponse(this.workItemService.update(workItem));
    } else {
      this.subscribeToSaveResponse(this.workItemService.create(workItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkItem>>): void {
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

  protected updateForm(workItem: IWorkItem): void {
    this.workItem = workItem;
    this.workItemFormService.resetForm(this.editForm, workItem);

    this.hrLetterParametersCollection = this.hrLetterParameterService.addHrLetterParameterToCollectionIfMissing<IHrLetterParameter>(
      this.hrLetterParametersCollection,
      workItem.hrLetterParameter,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      workItem.internalUser,
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      workItem.company,
    );
    this.worksSharedCollection = this.workService.addWorkToCollectionIfMissing<IWork>(this.worksSharedCollection, workItem.work);
  }

  protected loadRelationshipsOptions(): void {
    this.hrLetterParameterService
      .query({ filter: 'workitem-is-null' })
      .pipe(map((res: HttpResponse<IHrLetterParameter[]>) => res.body ?? []))
      .pipe(
        map((hrLetterParameters: IHrLetterParameter[]) =>
          this.hrLetterParameterService.addHrLetterParameterToCollectionIfMissing<IHrLetterParameter>(
            hrLetterParameters,
            this.workItem?.hrLetterParameter,
          ),
        ),
      )
      .subscribe((hrLetterParameters: IHrLetterParameter[]) => (this.hrLetterParametersCollection = hrLetterParameters));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.workItem?.internalUser,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.workItem?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.workService
      .query()
      .pipe(map((res: HttpResponse<IWork[]>) => res.body ?? []))
      .pipe(map((works: IWork[]) => this.workService.addWorkToCollectionIfMissing<IWork>(works, this.workItem?.work)))
      .subscribe((works: IWork[]) => (this.worksSharedCollection = works));
  }
}
