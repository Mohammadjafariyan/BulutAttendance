import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountHesab } from '../account-hesab.model';
import { AccountHesabService } from '../service/account-hesab.service';

@Component({
  standalone: true,
  templateUrl: './account-hesab-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountHesabDeleteDialogComponent {
  accountHesab?: IAccountHesab;

  protected accountHesabService = inject(AccountHesabService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.accountHesabService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
