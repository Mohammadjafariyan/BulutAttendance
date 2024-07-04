import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccProcStep } from '../acc-proc-step.model';
import { AccProcStepService } from '../service/acc-proc-step.service';

@Component({
  standalone: true,
  templateUrl: './acc-proc-step-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccProcStepDeleteDialogComponent {
  accProcStep?: IAccProcStep;

  protected accProcStepService = inject(AccProcStepService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accProcStepService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
