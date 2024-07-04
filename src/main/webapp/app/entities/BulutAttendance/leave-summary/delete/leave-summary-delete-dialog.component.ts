import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILeaveSummary } from '../leave-summary.model';
import { LeaveSummaryService } from '../service/leave-summary.service';

@Component({
  standalone: true,
  templateUrl: './leave-summary-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LeaveSummaryDeleteDialogComponent {
  leaveSummary?: ILeaveSummary;

  protected leaveSummaryService = inject(LeaveSummaryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.leaveSummaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
