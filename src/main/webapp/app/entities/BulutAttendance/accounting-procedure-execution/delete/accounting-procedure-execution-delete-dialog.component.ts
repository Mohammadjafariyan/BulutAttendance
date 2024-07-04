import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';
import { AccountingProcedureExecutionService } from '../service/accounting-procedure-execution.service';

@Component({
  standalone: true,
  templateUrl: './accounting-procedure-execution-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountingProcedureExecutionDeleteDialogComponent {
  accountingProcedureExecution?: IAccountingProcedureExecution;

  protected accountingProcedureExecutionService = inject(AccountingProcedureExecutionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountingProcedureExecutionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
