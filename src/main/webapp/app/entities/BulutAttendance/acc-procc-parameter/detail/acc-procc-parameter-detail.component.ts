import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccProccParameter } from '../acc-procc-parameter.model';

@Component({
  standalone: true,
  selector: 'jhi-acc-procc-parameter-detail',
  templateUrl: './acc-procc-parameter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccProccParameterDetailComponent {
  accProccParameter = input<IAccProccParameter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
