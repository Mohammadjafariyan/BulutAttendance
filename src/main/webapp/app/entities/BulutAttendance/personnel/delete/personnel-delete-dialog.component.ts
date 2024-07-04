import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPersonnel } from '../personnel.model';
import { PersonnelService } from '../service/personnel.service';

@Component({
  standalone: true,
  templateUrl: './personnel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PersonnelDeleteDialogComponent {
  personnel?: IPersonnel;

  protected personnelService = inject(PersonnelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.personnelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
