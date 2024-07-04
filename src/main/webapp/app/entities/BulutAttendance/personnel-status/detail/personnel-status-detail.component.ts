import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPersonnelStatus } from '../personnel-status.model';

@Component({
  standalone: true,
  selector: 'jhi-personnel-status-detail',
  templateUrl: './personnel-status-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PersonnelStatusDetailComponent {
  personnelStatus = input<IPersonnelStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
