import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParameter, NewParameter } from '../parameter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParameter for edit and NewParameterFormGroupInput for create.
 */
type ParameterFormGroupInput = IParameter | PartialWithRequiredKeyOf<NewParameter>;

type ParameterFormDefaults = Pick<NewParameter, 'id' | 'isDeducTax' | 'isDeducInsurance'>;

type ParameterFormGroupContent = {
  id: FormControl<IParameter['id'] | NewParameter['id']>;
  title: FormControl<IParameter['title']>;
  manualOrAuto: FormControl<IParameter['manualOrAuto']>;
  formula: FormControl<IParameter['formula']>;
  unit: FormControl<IParameter['unit']>;
  isDeducTax: FormControl<IParameter['isDeducTax']>;
  isDeducInsurance: FormControl<IParameter['isDeducInsurance']>;
  laborTime: FormControl<IParameter['laborTime']>;
  hokm: FormControl<IParameter['hokm']>;
  earnings: FormControl<IParameter['earnings']>;
  deduction: FormControl<IParameter['deduction']>;
  other: FormControl<IParameter['other']>;
  status: FormControl<IParameter['status']>;
  internalUser: FormControl<IParameter['internalUser']>;
  company: FormControl<IParameter['company']>;
};

export type ParameterFormGroup = FormGroup<ParameterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParameterFormService {
  createParameterFormGroup(parameter: ParameterFormGroupInput = { id: null }): ParameterFormGroup {
    const parameterRawValue = {
      ...this.getFormDefaults(),
      ...parameter,
    };
    return new FormGroup<ParameterFormGroupContent>({
      id: new FormControl(
        { value: parameterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(parameterRawValue.title),
      manualOrAuto: new FormControl(parameterRawValue.manualOrAuto),
      formula: new FormControl(parameterRawValue.formula),
      unit: new FormControl(parameterRawValue.unit),
      isDeducTax: new FormControl(parameterRawValue.isDeducTax),
      isDeducInsurance: new FormControl(parameterRawValue.isDeducInsurance),
      laborTime: new FormControl(parameterRawValue.laborTime),
      hokm: new FormControl(parameterRawValue.hokm),
      earnings: new FormControl(parameterRawValue.earnings),
      deduction: new FormControl(parameterRawValue.deduction),
      other: new FormControl(parameterRawValue.other),
      status: new FormControl(parameterRawValue.status),
      internalUser: new FormControl(parameterRawValue.internalUser),
      company: new FormControl(parameterRawValue.company),
    });
  }

  getParameter(form: ParameterFormGroup): IParameter | NewParameter {
    return form.getRawValue() as IParameter | NewParameter;
  }

  resetForm(form: ParameterFormGroup, parameter: ParameterFormGroupInput): void {
    const parameterRawValue = { ...this.getFormDefaults(), ...parameter };
    form.reset(
      {
        ...parameterRawValue,
        id: { value: parameterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParameterFormDefaults {
    return {
      id: null,
      isDeducTax: false,
      isDeducInsurance: false,
    };
  }
}
