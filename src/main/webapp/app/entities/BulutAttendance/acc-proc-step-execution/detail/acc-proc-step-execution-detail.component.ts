import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccProcStepExecution } from '../acc-proc-step-execution.model';

@Component({
  standalone: true,
  selector: 'jhi-acc-proc-step-execution-detail',
  templateUrl: './acc-proc-step-execution-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccProcStepExecutionDetailComponent {
  accProcStepExecution = input<IAccProcStepExecution | null>(null);

  previousState(): void {
    window.history.back();
  }
}
