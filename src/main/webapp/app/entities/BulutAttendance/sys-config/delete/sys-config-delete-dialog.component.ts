import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISysConfig } from '../sys-config.model';
import { SysConfigService } from '../service/sys-config.service';

@Component({
  standalone: true,
  templateUrl: './sys-config-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SysConfigDeleteDialogComponent {
  sysConfig?: ISysConfig;

  protected sysConfigService = inject(SysConfigService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.sysConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
