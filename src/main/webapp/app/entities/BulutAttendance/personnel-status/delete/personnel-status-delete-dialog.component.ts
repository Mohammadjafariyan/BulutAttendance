import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPersonnelStatus } from '../personnel-status.model';
import { PersonnelStatusService } from '../service/personnel-status.service';

@Component({
  standalone: true,
  templateUrl: './personnel-status-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PersonnelStatusDeleteDialogComponent {
  personnelStatus?: IPersonnelStatus;

  protected personnelStatusService = inject(PersonnelStatusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.personnelStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
