import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBank } from '../bank.model';

@Component({
  standalone: true,
  selector: 'jhi-bank-detail',
  templateUrl: './bank-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BankDetailComponent {
  bank = input<IBank | null>(null);

  previousState(): void {
    window.history.back();
  }
}
