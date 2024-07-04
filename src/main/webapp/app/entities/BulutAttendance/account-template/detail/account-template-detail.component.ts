import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAccountTemplate } from '../account-template.model';

@Component({
  standalone: true,
  selector: 'jhi-account-template-detail',
  templateUrl: './account-template-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccountTemplateDetailComponent {
  accountTemplate = input<IAccountTemplate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
