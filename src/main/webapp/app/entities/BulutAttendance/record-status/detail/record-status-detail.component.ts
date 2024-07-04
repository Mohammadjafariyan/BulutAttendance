import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRecordStatus } from '../record-status.model';

@Component({
  standalone: true,
  selector: 'jhi-record-status-detail',
  templateUrl: './record-status-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RecordStatusDetailComponent {
  recordStatus = input<IRecordStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
