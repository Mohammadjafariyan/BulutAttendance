import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHrLetterType } from '../hr-letter-type.model';
import { HrLetterTypeService } from '../service/hr-letter-type.service';

@Component({
  standalone: true,
  templateUrl: './hr-letter-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HrLetterTypeDeleteDialogComponent {
  hrLetterType?: IHrLetterType;

  protected hrLetterTypeService = inject(HrLetterTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.hrLetterTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
