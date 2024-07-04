import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITaxTemplate } from '../tax-template.model';
import { TaxTemplateService } from '../service/tax-template.service';

@Component({
  standalone: true,
  templateUrl: './tax-template-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TaxTemplateDeleteDialogComponent {
  taxTemplate?: ITaxTemplate;

  protected taxTemplateService = inject(TaxTemplateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.taxTemplateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
