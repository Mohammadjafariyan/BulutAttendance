import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHrLetterParameter } from '../hr-letter-parameter.model';
import { HrLetterParameterService } from '../service/hr-letter-parameter.service';

@Component({
  standalone: true,
  templateUrl: './hr-letter-parameter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HrLetterParameterDeleteDialogComponent {
  hrLetterParameter?: IHrLetterParameter;

  protected hrLetterParameterService = inject(HrLetterParameterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hrLetterParameterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
