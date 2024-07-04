import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHrLetterType, NewHrLetterType } from '../hr-letter-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHrLetterType for edit and NewHrLetterTypeFormGroupInput for create.
 */
type HrLetterTypeFormGroupInput = IHrLetterType | PartialWithRequiredKeyOf<NewHrLetterType>;

type HrLetterTypeFormDefaults = Pick<NewHrLetterType, 'id'>;

type HrLetterTypeFormGroupContent = {
  title: FormControl<IHrLetterType['title']>;
  id: FormControl<IHrLetterType['id'] | NewHrLetterType['id']>;
  status: FormControl<IHrLetterType['status']>;
  internalUser: FormControl<IHrLetterType['internalUser']>;
  company: FormControl<IHrLetterType['company']>;
};

export type HrLetterTypeFormGroup = FormGroup<HrLetterTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HrLetterTypeFormService {
  createHrLetterTypeFormGroup(hrLetterType: HrLetterTypeFormGroupInput = { id: null }): HrLetterTypeFormGroup {
    const hrLetterTypeRawValue = {
      ...this.getFormDefaults(),
      ...hrLetterType,
    };
    return new FormGroup<HrLetterTypeFormGroupContent>({
      title: new FormControl(hrLetterTypeRawValue.title),
      id: new FormControl(
        { value: hrLetterTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(hrLetterTypeRawValue.status),
      internalUser: new FormControl(hrLetterTypeRawValue.internalUser),
      company: new FormControl(hrLetterTypeRawValue.company),
    });
  }

  getHrLetterType(form: HrLetterTypeFormGroup): IHrLetterType | NewHrLetterType {
    return form.getRawValue() as IHrLetterType | NewHrLetterType;
  }

  resetForm(form: HrLetterTypeFormGroup, hrLetterType: HrLetterTypeFormGroupInput): void {
    const hrLetterTypeRawValue = { ...this.getFormDefaults(), ...hrLetterType };
    form.reset(
      {
        ...hrLetterTypeRawValue,
        id: { value: hrLetterTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HrLetterTypeFormDefaults {
    return {
      id: null,
    };
  }
}
