import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITaxTemplate } from '../tax-template.model';

@Component({
  standalone: true,
  selector: 'jhi-tax-template-detail',
  templateUrl: './tax-template-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TaxTemplateDetailComponent {
  taxTemplate = input<ITaxTemplate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
