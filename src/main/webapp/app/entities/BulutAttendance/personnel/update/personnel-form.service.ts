import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPersonnel, NewPersonnel } from '../personnel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonnel for edit and NewPersonnelFormGroupInput for create.
 */
type PersonnelFormGroupInput = IPersonnel | PartialWithRequiredKeyOf<NewPersonnel>;

type PersonnelFormDefaults = Pick<NewPersonnel, 'id'>;

type PersonnelFormGroupContent = {
  firstName: FormControl<IPersonnel['firstName']>;
  lastName: FormControl<IPersonnel['lastName']>;
  requitmentDate: FormControl<IPersonnel['requitmentDate']>;
  father: FormControl<IPersonnel['father']>;
  shenasname: FormControl<IPersonnel['shenasname']>;
  mahalesodur: FormControl<IPersonnel['mahalesodur']>;
  birthday: FormControl<IPersonnel['birthday']>;
  isSingle: FormControl<IPersonnel['isSingle']>;
  lastEducation: FormControl<IPersonnel['lastEducation']>;
  educationField: FormControl<IPersonnel['educationField']>;
  children: FormControl<IPersonnel['children']>;
  id: FormControl<IPersonnel['id'] | NewPersonnel['id']>;
  status: FormControl<IPersonnel['status']>;
  internalUser: FormControl<IPersonnel['internalUser']>;
  company: FormControl<IPersonnel['company']>;
};

export type PersonnelFormGroup = FormGroup<PersonnelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonnelFormService {
  createPersonnelFormGroup(personnel: PersonnelFormGroupInput = { id: null }): PersonnelFormGroup {
    const personnelRawValue = {
      ...this.getFormDefaults(),
      ...personnel,
    };
    return new FormGroup<PersonnelFormGroupContent>({
      firstName: new FormControl(personnelRawValue.firstName),
      lastName: new FormControl(personnelRawValue.lastName),
      requitmentDate: new FormControl(personnelRawValue.requitmentDate),
      father: new FormControl(personnelRawValue.father),
      shenasname: new FormControl(personnelRawValue.shenasname),
      mahalesodur: new FormControl(personnelRawValue.mahalesodur),
      birthday: new FormControl(personnelRawValue.birthday),
      isSingle: new FormControl(personnelRawValue.isSingle),
      lastEducation: new FormControl(personnelRawValue.lastEducation),
      educationField: new FormControl(personnelRawValue.educationField),
      children: new FormControl(personnelRawValue.children, {
        validators: [Validators.min(42), Validators.max(42)],
      }),
      id: new FormControl(
        { value: personnelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(personnelRawValue.status),
      internalUser: new FormControl(personnelRawValue.internalUser),
      company: new FormControl(personnelRawValue.company),
    });
  }

  getPersonnel(form: PersonnelFormGroup): IPersonnel | NewPersonnel {
    return form.getRawValue() as IPersonnel | NewPersonnel;
  }

  resetForm(form: PersonnelFormGroup, personnel: PersonnelFormGroupInput): void {
    const personnelRawValue = { ...this.getFormDefaults(), ...personnel };
    form.reset(
      {
        ...personnelRawValue,
        id: { value: personnelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PersonnelFormDefaults {
    return {
      id: null,
    };
  }
}
