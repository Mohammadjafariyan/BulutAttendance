import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPersonnelStatus, NewPersonnelStatus } from '../personnel-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonnelStatus for edit and NewPersonnelStatusFormGroupInput for create.
 */
type PersonnelStatusFormGroupInput = IPersonnelStatus | PartialWithRequiredKeyOf<NewPersonnelStatus>;

type PersonnelStatusFormDefaults = Pick<NewPersonnelStatus, 'id'>;

type PersonnelStatusFormGroupContent = {
  title: FormControl<IPersonnelStatus['title']>;
  id: FormControl<IPersonnelStatus['id'] | NewPersonnelStatus['id']>;
  status: FormControl<IPersonnelStatus['status']>;
  internalUser: FormControl<IPersonnelStatus['internalUser']>;
  company: FormControl<IPersonnelStatus['company']>;
};

export type PersonnelStatusFormGroup = FormGroup<PersonnelStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonnelStatusFormService {
  createPersonnelStatusFormGroup(personnelStatus: PersonnelStatusFormGroupInput = { id: null }): PersonnelStatusFormGroup {
    const personnelStatusRawValue = {
      ...this.getFormDefaults(),
      ...personnelStatus,
    };
    return new FormGroup<PersonnelStatusFormGroupContent>({
      title: new FormControl(personnelStatusRawValue.title),
      id: new FormControl(
        { value: personnelStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(personnelStatusRawValue.status),
      internalUser: new FormControl(personnelStatusRawValue.internalUser),
      company: new FormControl(personnelStatusRawValue.company),
    });
  }

  getPersonnelStatus(form: PersonnelStatusFormGroup): IPersonnelStatus | NewPersonnelStatus {
    return form.getRawValue() as IPersonnelStatus | NewPersonnelStatus;
  }

  resetForm(form: PersonnelStatusFormGroup, personnelStatus: PersonnelStatusFormGroupInput): void {
    const personnelStatusRawValue = { ...this.getFormDefaults(), ...personnelStatus };
    form.reset(
      {
        ...personnelStatusRawValue,
        id: { value: personnelStatusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonnelStatusFormDefaults {
    return {
      id: null,
    };
  }
}
