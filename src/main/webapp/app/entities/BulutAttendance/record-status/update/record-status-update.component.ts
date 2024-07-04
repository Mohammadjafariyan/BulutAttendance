import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecordStatus } from '../record-status.model';
import { RecordStatusService } from '../service/record-status.service';
import { RecordStatusFormService, RecordStatusFormGroup } from './record-status-form.service';

@Component({
  standalone: true,
  selector: 'jhi-record-status-update',
  templateUrl: './record-status-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecordStatusUpdateComponent implements OnInit {
  isSaving = false;
  recordStatus: IRecordStatus | null = null;

  protected recordStatusService = inject(RecordStatusService);
  protected recordStatusFormService = inject(RecordStatusFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecordStatusFormGroup = this.recordStatusFormService.createRecordStatusFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recordStatus }) => {
      this.recordStatus = recordStatus;
      if (recordStatus) {
        this.updateForm(recordStatus);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recordStatus = this.recordStatusFormService.getRecordStatus(this.editForm);
    if (recordStatus.id !== null) {
      this.subscribeToSaveResponse(this.recordStatusService.update(recordStatus));
    } else {
      this.subscribeToSaveResponse(this.recordStatusService.create(recordStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecordStatus>>): void {
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

  protected updateForm(recordStatus: IRecordStatus): void {
    this.recordStatus = recordStatus;
    this.recordStatusFormService.resetForm(this.editForm, recordStatus);
  }
}
