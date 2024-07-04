import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrgPosition } from '../org-position.model';
import { OrgPositionService } from '../service/org-position.service';

@Component({
  standalone: true,
  templateUrl: './org-position-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrgPositionDeleteDialogComponent {
  orgPosition?: IOrgPosition;

  protected orgPositionService = inject(OrgPositionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.orgPositionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
