import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccProcStep } from '../acc-proc-step.model';

@Component({
  standalone: true,
  selector: 'jhi-acc-proc-step-detail',
  templateUrl: './acc-proc-step-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccProcStepDetailComponent {
  accProcStep = input<IAccProcStep | null>(null);

  previousState(): void {
    window.history.back();
  }
}
