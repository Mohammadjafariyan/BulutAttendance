import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRecordStatus } from '../record-status.model';
import { RecordStatusService } from '../service/record-status.service';

@Component({
  standalone: true,
  templateUrl: './record-status-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RecordStatusDeleteDialogComponent {
  recordStatus?: IRecordStatus;

  protected recordStatusService = inject(RecordStatusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.recordStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
