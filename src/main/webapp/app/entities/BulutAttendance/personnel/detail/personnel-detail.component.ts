import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPersonnel } from '../personnel.model';

@Component({
  standalone: true,
  selector: 'jhi-personnel-detail',
  templateUrl: './personnel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PersonnelDetailComponent {
  personnel = input<IPersonnel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
