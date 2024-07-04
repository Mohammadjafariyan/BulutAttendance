import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISysConfig } from '../sys-config.model';

@Component({
  standalone: true,
  selector: 'jhi-sys-config-detail',
  templateUrl: './sys-config-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SysConfigDetailComponent {
  sysConfig = input<ISysConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
