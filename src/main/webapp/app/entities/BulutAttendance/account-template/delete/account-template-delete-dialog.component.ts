import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountTemplate } from '../account-template.model';
import { AccountTemplateService } from '../service/account-template.service';

@Component({
  standalone: true,
  templateUrl: './account-template-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountTemplateDeleteDialogComponent {
  accountTemplate?: IAccountTemplate;

  protected accountTemplateService = inject(AccountTemplateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.accountTemplateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
