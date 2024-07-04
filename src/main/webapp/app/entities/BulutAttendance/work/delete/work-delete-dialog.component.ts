import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWork } from '../work.model';
import { WorkService } from '../service/work.service';

@Component({
  standalone: true,
  templateUrl: './work-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkDeleteDialogComponent {
  work?: IWork;

  protected workService = inject(WorkService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.workService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
