import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITransactionAccount } from '../transaction-account.model';

@Component({
  standalone: true,
  selector: 'jhi-transaction-account-detail',
  templateUrl: './transaction-account-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TransactionAccountDetailComponent {
  transactionAccount = input<ITransactionAccount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
