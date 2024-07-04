import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHrLetterParameter } from '../hr-letter-parameter.model';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-parameter-detail',
  templateUrl: './hr-letter-parameter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HrLetterParameterDetailComponent {
  hrLetterParameter = input<IHrLetterParameter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
