import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccountHesab } from '../account-hesab.model';

@Component({
  standalone: true,
  selector: 'jhi-account-hesab-detail',
  templateUrl: './account-hesab-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccountHesabDetailComponent {
  accountHesab = input<IAccountHesab | null>(null);

  previousState(): void {
    window.history.back();
  }
}
