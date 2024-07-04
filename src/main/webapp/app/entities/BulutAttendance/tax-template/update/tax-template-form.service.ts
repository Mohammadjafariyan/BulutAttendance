import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITaxTemplate, NewTaxTemplate } from '../tax-template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITaxTemplate for edit and NewTaxTemplateFormGroupInput for create.
 */
type TaxTemplateFormGroupInput = ITaxTemplate | PartialWithRequiredKeyOf<NewTaxTemplate>;

type TaxTemplateFormDefaults = Pick<NewTaxTemplate, 'id'>;

type TaxTemplateFormGroupContent = {
  rangeFrom: FormControl<ITaxTemplate['rangeFrom']>;
  rangeTo: FormControl<ITaxTemplate['rangeTo']>;
  percent: FormControl<ITaxTemplate['percent']>;
  year: FormControl<ITaxTemplate['year']>;
  id: FormControl<ITaxTemplate['id'] | NewTaxTemplate['id']>;
  status: FormControl<ITaxTemplate['status']>;
  internalUser: FormControl<ITaxTemplate['internalUser']>;
  company: FormControl<ITaxTemplate['company']>;
};

export type TaxTemplateFormGroup = FormGroup<TaxTemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaxTemplateFormService {
  createTaxTemplateFormGroup(taxTemplate: TaxTemplateFormGroupInput = { id: null }): TaxTemplateFormGroup {
    const taxTemplateRawValue = {
      ...this.getFormDefaults(),
      ...taxTemplate,
    };
    return new FormGroup<TaxTemplateFormGroupContent>({
      rangeFrom: new FormControl(taxTemplateRawValue.rangeFrom),
      rangeTo: new FormControl(taxTemplateRawValue.rangeTo),
      percent: new FormControl(taxTemplateRawValue.percent),
      year: new FormControl(taxTemplateRawValue.year),
      id: new FormControl(
        { value: taxTemplateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(taxTemplateRawValue.status),
      internalUser: new FormControl(taxTemplateRawValue.internalUser),
      company: new FormControl(taxTemplateRawValue.company),
    });
  }

  getTaxTemplate(form: TaxTemplateFormGroup): ITaxTemplate | NewTaxTemplate {
    return form.getRawValue() as ITaxTemplate | NewTaxTemplate;
  }

  resetForm(form: TaxTemplateFormGroup, taxTemplate: TaxTemplateFormGroupInput): void {
    const taxTemplateRawValue = { ...this.getFormDefaults(), ...taxTemplate };
    form.reset(
      {
        ...taxTemplateRawValue,
        id: { value: taxTemplateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TaxTemplateFormDefaults {
    return {
      id: null,
    };
  }
}
