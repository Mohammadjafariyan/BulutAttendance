import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrgUnit, NewOrgUnit } from '../org-unit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrgUnit for edit and NewOrgUnitFormGroupInput for create.
 */
type OrgUnitFormGroupInput = IOrgUnit | PartialWithRequiredKeyOf<NewOrgUnit>;

type OrgUnitFormDefaults = Pick<NewOrgUnit, 'id'>;

type OrgUnitFormGroupContent = {
  title: FormControl<IOrgUnit['title']>;
  id: FormControl<IOrgUnit['id'] | NewOrgUnit['id']>;
  status: FormControl<IOrgUnit['status']>;
  internalUser: FormControl<IOrgUnit['internalUser']>;
  company: FormControl<IOrgUnit['company']>;
  parent: FormControl<IOrgUnit['parent']>;
};

export type OrgUnitFormGroup = FormGroup<OrgUnitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrgUnitFormService {
  createOrgUnitFormGroup(orgUnit: OrgUnitFormGroupInput = { id: null }): OrgUnitFormGroup {
    const orgUnitRawValue = {
      ...this.getFormDefaults(),
      ...orgUnit,
    };
    return new FormGroup<OrgUnitFormGroupContent>({
      title: new FormControl(orgUnitRawValue.title),
      id: new FormControl(
        { value: orgUnitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(orgUnitRawValue.status),
      internalUser: new FormControl(orgUnitRawValue.internalUser),
      company: new FormControl(orgUnitRawValue.company),
      parent: new FormControl(orgUnitRawValue.parent),
    });
  }

  getOrgUnit(form: OrgUnitFormGroup): IOrgUnit | NewOrgUnit {
    return form.getRawValue() as IOrgUnit | NewOrgUnit;
  }

  resetForm(form: OrgUnitFormGroup, orgUnit: OrgUnitFormGroupInput): void {
    const orgUnitRawValue = { ...this.getFormDefaults(), ...orgUnit };
    form.reset(
      {
        ...orgUnitRawValue,
        id: { value: orgUnitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrgUnitFormDefaults {
    return {
      id: null,
    };
  }
}
