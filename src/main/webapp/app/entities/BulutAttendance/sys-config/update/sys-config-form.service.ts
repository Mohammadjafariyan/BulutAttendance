import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISysConfig, NewSysConfig } from '../sys-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISysConfig for edit and NewSysConfigFormGroupInput for create.
 */
type SysConfigFormGroupInput = ISysConfig | PartialWithRequiredKeyOf<NewSysConfig>;

type SysConfigFormDefaults = Pick<NewSysConfig, 'id'>;

type SysConfigFormGroupContent = {
  taxFormula: FormControl<ISysConfig['taxFormula']>;
  sanavatFormula: FormControl<ISysConfig['sanavatFormula']>;
  year: FormControl<ISysConfig['year']>;
  id: FormControl<ISysConfig['id'] | NewSysConfig['id']>;
  status: FormControl<ISysConfig['status']>;
  internalUser: FormControl<ISysConfig['internalUser']>;
  company: FormControl<ISysConfig['company']>;
};

export type SysConfigFormGroup = FormGroup<SysConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SysConfigFormService {
  createSysConfigFormGroup(sysConfig: SysConfigFormGroupInput = { id: null }): SysConfigFormGroup {
    const sysConfigRawValue = {
      ...this.getFormDefaults(),
      ...sysConfig,
    };
    return new FormGroup<SysConfigFormGroupContent>({
      taxFormula: new FormControl(sysConfigRawValue.taxFormula),
      sanavatFormula: new FormControl(sysConfigRawValue.sanavatFormula),
      year: new FormControl(sysConfigRawValue.year),
      id: new FormControl(
        { value: sysConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(sysConfigRawValue.status),
      internalUser: new FormControl(sysConfigRawValue.internalUser),
      company: new FormControl(sysConfigRawValue.company),
    });
  }

  getSysConfig(form: SysConfigFormGroup): ISysConfig | NewSysConfig {
    return form.getRawValue() as ISysConfig | NewSysConfig;
  }

  resetForm(form: SysConfigFormGroup, sysConfig: SysConfigFormGroupInput): void {
    const sysConfigRawValue = { ...this.getFormDefaults(), ...sysConfig };
    form.reset(
      {
        ...sysConfigRawValue,
        id: { value: sysConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SysConfigFormDefaults {
    return {
      id: null,
    };
  }
}
