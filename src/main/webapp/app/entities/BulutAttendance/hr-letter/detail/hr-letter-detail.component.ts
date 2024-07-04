import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHrLetter } from '../hr-letter.model';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-detail',
  templateUrl: './hr-letter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HrLetterDetailComponent {
  hrLetter = input<IHrLetter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
