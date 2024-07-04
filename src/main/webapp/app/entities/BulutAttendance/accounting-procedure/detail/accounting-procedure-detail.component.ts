import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccountingProcedure } from '../accounting-procedure.model';

@Component({
  standalone: true,
  selector: 'jhi-accounting-procedure-detail',
  templateUrl: './accounting-procedure-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccountingProcedureDetailComponent {
  accountingProcedure = input<IAccountingProcedure | null>(null);

  previousState(): void {
    window.history.back();
  }
}
