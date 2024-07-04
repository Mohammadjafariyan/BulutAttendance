import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHrLetterType } from '../hr-letter-type.model';

@Component({
  standalone: true,
  selector: 'jhi-hr-letter-type-detail',
  templateUrl: './hr-letter-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HrLetterTypeDetailComponent {
  hrLetterType = input<IHrLetterType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
