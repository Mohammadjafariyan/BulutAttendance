import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrgPosition, NewOrgPosition } from '../org-position.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrgPosition for edit and NewOrgPositionFormGroupInput for create.
 */
type OrgPositionFormGroupInput = IOrgPosition | PartialWithRequiredKeyOf<NewOrgPosition>;

type OrgPositionFormDefaults = Pick<NewOrgPosition, 'id'>;

type OrgPositionFormGroupContent = {
  title: FormControl<IOrgPosition['title']>;
  id: FormControl<IOrgPosition['id'] | NewOrgPosition['id']>;
  status: FormControl<IOrgPosition['status']>;
  internalUser: FormControl<IOrgPosition['internalUser']>;
  company: FormControl<IOrgPosition['company']>;
};

export type OrgPositionFormGroup = FormGroup<OrgPositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrgPositionFormService {
  createOrgPositionFormGroup(orgPosition: OrgPositionFormGroupInput = { id: null }): OrgPositionFormGroup {
    const orgPositionRawValue = {
      ...this.getFormDefaults(),
      ...orgPosition,
    };
    return new FormGroup<OrgPositionFormGroupContent>({
      title: new FormControl(orgPositionRawValue.title),
      id: new FormControl(
        { value: orgPositionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(orgPositionRawValue.status),
      internalUser: new FormControl(orgPositionRawValue.internalUser),
      company: new FormControl(orgPositionRawValue.company),
    });
  }

  getOrgPosition(form: OrgPositionFormGroup): IOrgPosition | NewOrgPosition {
    return form.getRawValue() as IOrgPosition | NewOrgPosition;
  }

  resetForm(form: OrgPositionFormGroup, orgPosition: OrgPositionFormGroupInput): void {
    const orgPositionRawValue = { ...this.getFormDefaults(), ...orgPosition };
    form.reset(
      {
        ...orgPositionRawValue,
        id: { value: orgPositionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrgPositionFormDefaults {
    return {
      id: null,
    };
  }
}
