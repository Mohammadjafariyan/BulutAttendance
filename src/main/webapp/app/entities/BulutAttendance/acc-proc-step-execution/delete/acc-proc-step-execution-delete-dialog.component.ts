import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccProcStepExecution } from '../acc-proc-step-execution.model';
import { AccProcStepExecutionService } from '../service/acc-proc-step-execution.service';

@Component({
  standalone: true,
  templateUrl: './acc-proc-step-execution-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccProcStepExecutionDeleteDialogComponent {
  accProcStepExecution?: IAccProcStepExecution;

  protected accProcStepExecutionService = inject(AccProcStepExecutionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accProcStepExecutionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
