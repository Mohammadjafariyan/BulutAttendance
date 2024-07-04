import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILeaveSummary } from '../leave-summary.model';

@Component({
  standalone: true,
  selector: 'jhi-leave-summary-detail',
  templateUrl: './leave-summary-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LeaveSummaryDetailComponent {
  leaveSummary = input<ILeaveSummary | null>(null);

  previousState(): void {
    window.history.back();
  }
}
