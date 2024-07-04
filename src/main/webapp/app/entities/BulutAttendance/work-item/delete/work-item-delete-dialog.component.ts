import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkItem } from '../work-item.model';
import { WorkItemService } from '../service/work-item.service';

@Component({
  standalone: true,
  templateUrl: './work-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkItemDeleteDialogComponent {
  workItem?: IWorkItem;

  protected workItemService = inject(WorkItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.workItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
