import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccProccParameter, NewAccProccParameter } from '../acc-procc-parameter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccProccParameter for edit and NewAccProccParameterFormGroupInput for create.
 */
type AccProccParameterFormGroupInput = IAccProccParameter | PartialWithRequiredKeyOf<NewAccProccParameter>;

type AccProccParameterFormDefaults = Pick<NewAccProccParameter, 'id' | 'isDeducTax' | 'isDeducInsurance'>;

type AccProccParameterFormGroupContent = {
  id: FormControl<IAccProccParameter['id'] | NewAccProccParameter['id']>;
  title: FormControl<IAccProccParameter['title']>;
  manualOrAuto: FormControl<IAccProccParameter['manualOrAuto']>;
  formula: FormControl<IAccProccParameter['formula']>;
  unit: FormControl<IAccProccParameter['unit']>;
  isDeducTax: FormControl<IAccProccParameter['isDeducTax']>;
  isDeducInsurance: FormControl<IAccProccParameter['isDeducInsurance']>;
  laborTime: FormControl<IAccProccParameter['laborTime']>;
  hokm: FormControl<IAccProccParameter['hokm']>;
  earnings: FormControl<IAccProccParameter['earnings']>;
  deduction: FormControl<IAccProccParameter['deduction']>;
  other: FormControl<IAccProccParameter['other']>;
  internalUser: FormControl<IAccProccParameter['internalUser']>;
  company: FormControl<IAccProccParameter['company']>;
};

export type AccProccParameterFormGroup = FormGroup<AccProccParameterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccProccParameterFormService {
  createAccProccParameterFormGroup(accProccParameter: AccProccParameterFormGroupInput = { id: null }): AccProccParameterFormGroup {
    const accProccParameterRawValue = {
      ...this.getFormDefaults(),
      ...accProccParameter,
    };
    return new FormGroup<AccProccParameterFormGroupContent>({
      id: new FormControl(
        { value: accProccParameterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(accProccParameterRawValue.title),
      manualOrAuto: new FormControl(accProccParameterRawValue.manualOrAuto),
      formula: new FormControl(accProccParameterRawValue.formula),
      unit: new FormControl(accProccParameterRawValue.unit),
      isDeducTax: new FormControl(accProccParameterRawValue.isDeducTax),
      isDeducInsurance: new FormControl(accProccParameterRawValue.isDeducInsurance),
      laborTime: new FormControl(accProccParameterRawValue.laborTime),
      hokm: new FormControl(accProccParameterRawValue.hokm),
      earnings: new FormControl(accProccParameterRawValue.earnings),
      deduction: new FormControl(accProccParameterRawValue.deduction),
      other: new FormControl(accProccParameterRawValue.other),
      internalUser: new FormControl(accProccParameterRawValue.internalUser),
      company: new FormControl(accProccParameterRawValue.company),
    });
  }

  getAccProccParameter(form: AccProccParameterFormGroup): IAccProccParameter | NewAccProccParameter {
    return form.getRawValue() as IAccProccParameter | NewAccProccParameter;
  }

  resetForm(form: AccProccParameterFormGroup, accProccParameter: AccProccParameterFormGroupInput): void {
    const accProccParameterRawValue = { ...this.getFormDefaults(), ...accProccParameter };
    form.reset(
      {
        ...accProccParameterRawValue,
        id: { value: accProccParameterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccProccParameterFormDefaults {
    return {
      id: null,
      isDeducTax: false,
      isDeducInsurance: false,
    };
  }
}
