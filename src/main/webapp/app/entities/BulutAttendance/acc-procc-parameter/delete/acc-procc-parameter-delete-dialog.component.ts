import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccProccParameter } from '../acc-procc-parameter.model';
import { AccProccParameterService } from '../service/acc-procc-parameter.service';

@Component({
  standalone: true,
  templateUrl: './acc-procc-parameter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccProccParameterDeleteDialogComponent {
  accProccParameter?: IAccProccParameter;

  protected accProccParameterService = inject(AccProccParameterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accProccParameterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
