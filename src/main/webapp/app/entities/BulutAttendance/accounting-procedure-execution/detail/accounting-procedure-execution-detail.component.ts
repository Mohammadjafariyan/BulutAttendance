import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';

@Component({
  standalone: true,
  selector: 'jhi-accounting-procedure-execution-detail',
  templateUrl: './accounting-procedure-execution-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccountingProcedureExecutionDetailComponent {
  accountingProcedureExecution = input<IAccountingProcedureExecution | null>(null);

  previousState(): void {
    window.history.back();
  }
}
