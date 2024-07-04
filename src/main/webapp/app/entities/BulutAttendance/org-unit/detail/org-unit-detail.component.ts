import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrgUnit } from '../org-unit.model';

@Component({
  standalone: true,
  selector: 'jhi-org-unit-detail',
  templateUrl: './org-unit-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrgUnitDetailComponent {
  orgUnit = input<IOrgUnit | null>(null);

  previousState(): void {
    window.history.back();
  }
}
