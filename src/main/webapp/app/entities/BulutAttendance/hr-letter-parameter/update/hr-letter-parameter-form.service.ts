import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHrLetterParameter, NewHrLetterParameter } from '../hr-letter-parameter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHrLetterParameter for edit and NewHrLetterParameterFormGroupInput for create.
 */
type HrLetterParameterFormGroupInput = IHrLetterParameter | PartialWithRequiredKeyOf<NewHrLetterParameter>;

type HrLetterParameterFormDefaults = Pick<NewHrLetterParameter, 'id' | 'isDeducTax' | 'isDeducInsurance' | 'isEnabled'>;

type HrLetterParameterFormGroupContent = {
  id: FormControl<IHrLetterParameter['id'] | NewHrLetterParameter['id']>;
  title: FormControl<IHrLetterParameter['title']>;
  manualOrAuto: FormControl<IHrLetterParameter['manualOrAuto']>;
  formula: FormControl<IHrLetterParameter['formula']>;
  unit: FormControl<IHrLetterParameter['unit']>;
  isDeducTax: FormControl<IHrLetterParameter['isDeducTax']>;
  isDeducInsurance: FormControl<IHrLetterParameter['isDeducInsurance']>;
  laborTime: FormControl<IHrLetterParameter['laborTime']>;
  hokm: FormControl<IHrLetterParameter['hokm']>;
  earnings: FormControl<IHrLetterParameter['earnings']>;
  deduction: FormControl<IHrLetterParameter['deduction']>;
  other: FormControl<IHrLetterParameter['other']>;
  isEnabled: FormControl<IHrLetterParameter['isEnabled']>;
  status: FormControl<IHrLetterParameter['status']>;
  internalUser: FormControl<IHrLetterParameter['internalUser']>;
  company: FormControl<IHrLetterParameter['company']>;
};

export type HrLetterParameterFormGroup = FormGroup<HrLetterParameterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HrLetterParameterFormService {
  createHrLetterParameterFormGroup(hrLetterParameter: HrLetterParameterFormGroupInput = { id: null }): HrLetterParameterFormGroup {
    const hrLetterParameterRawValue = {
      ...this.getFormDefaults(),
      ...hrLetterParameter,
    };
    return new FormGroup<HrLetterParameterFormGroupContent>({
      id: new FormControl(
        { value: hrLetterParameterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(hrLetterParameterRawValue.title),
      manualOrAuto: new FormControl(hrLetterParameterRawValue.manualOrAuto),
      formula: new FormControl(hrLetterParameterRawValue.formula),
      unit: new FormControl(hrLetterParameterRawValue.unit),
      isDeducTax: new FormControl(hrLetterParameterRawValue.isDeducTax),
      isDeducInsurance: new FormControl(hrLetterParameterRawValue.isDeducInsurance),
      laborTime: new FormControl(hrLetterParameterRawValue.laborTime),
      hokm: new FormControl(hrLetterParameterRawValue.hokm),
      earnings: new FormControl(hrLetterParameterRawValue.earnings),
      deduction: new FormControl(hrLetterParameterRawValue.deduction),
      other: new FormControl(hrLetterParameterRawValue.other),
      isEnabled: new FormControl(hrLetterParameterRawValue.isEnabled),
      status: new FormControl(hrLetterParameterRawValue.status),
      internalUser: new FormControl(hrLetterParameterRawValue.internalUser),
      company: new FormControl(hrLetterParameterRawValue.company),
    });
  }

  getHrLetterParameter(form: HrLetterParameterFormGroup): IHrLetterParameter | NewHrLetterParameter {
    return form.getRawValue() as IHrLetterParameter | NewHrLetterParameter;
  }

  resetForm(form: HrLetterParameterFormGroup, hrLetterParameter: HrLetterParameterFormGroupInput): void {
    const hrLetterParameterRawValue = { ...this.getFormDefaults(), ...hrLetterParameter };
    form.reset(
      {
        ...hrLetterParameterRawValue,
        id: { value: hrLetterParameterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HrLetterParameterFormDefaults {
    return {
      id: null,
      isDeducTax: false,
      isDeducInsurance: false,
      isEnabled: false,
    };
  }
}
