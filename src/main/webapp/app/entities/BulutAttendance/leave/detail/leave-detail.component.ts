import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILeave } from '../leave.model';

@Component({
  standalone: true,
  selector: 'jhi-leave-detail',
  templateUrl: './leave-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LeaveDetailComponent {
  leave = input<ILeave | null>(null);

  previousState(): void {
    window.history.back();
  }
}
