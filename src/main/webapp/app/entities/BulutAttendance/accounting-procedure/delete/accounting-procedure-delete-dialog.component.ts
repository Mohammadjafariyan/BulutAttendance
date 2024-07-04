import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountingProcedure } from '../accounting-procedure.model';
import { AccountingProcedureService } from '../service/accounting-procedure.service';

@Component({
  standalone: true,
  templateUrl: './accounting-procedure-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountingProcedureDeleteDialogComponent {
  accountingProcedure?: IAccountingProcedure;

  protected accountingProcedureService = inject(AccountingProcedureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountingProcedureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
