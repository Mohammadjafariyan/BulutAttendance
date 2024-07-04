import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWorkItem } from '../work-item.model';

@Component({
  standalone: true,
  selector: 'jhi-work-item-detail',
  templateUrl: './work-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WorkItemDetailComponent {
  workItem = input<IWorkItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
