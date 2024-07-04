import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';

@Component({
  standalone: true,
  templateUrl: './org-unit-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrgUnitDeleteDialogComponent {
  orgUnit?: IOrgUnit;

  protected orgUnitService = inject(OrgUnitService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.orgUnitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
