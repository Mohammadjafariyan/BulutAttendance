import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccountTemplate, NewAccountTemplate } from '../account-template.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountTemplate for edit and NewAccountTemplateFormGroupInput for create.
 */
type AccountTemplateFormGroupInput = IAccountTemplate | PartialWithRequiredKeyOf<NewAccountTemplate>;

type AccountTemplateFormDefaults = Pick<NewAccountTemplate, 'id'>;

type AccountTemplateFormGroupContent = {
  title: FormControl<IAccountTemplate['title']>;
  code: FormControl<IAccountTemplate['code']>;
  level: FormControl<IAccountTemplate['level']>;
  levelTitle: FormControl<IAccountTemplate['levelTitle']>;
  id: FormControl<IAccountTemplate['id'] | NewAccountTemplate['id']>;
  type: FormControl<IAccountTemplate['type']>;
  levelInTree: FormControl<IAccountTemplate['levelInTree']>;
  debitAmount: FormControl<IAccountTemplate['debitAmount']>;
  creditAmount: FormControl<IAccountTemplate['creditAmount']>;
  typeInFormula: FormControl<IAccountTemplate['typeInFormula']>;
  status: FormControl<IAccountTemplate['status']>;
  internalUser: FormControl<IAccountTemplate['internalUser']>;
  company: FormControl<IAccountTemplate['company']>;
};

export type AccountTemplateFormGroup = FormGroup<AccountTemplateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountTemplateFormService {
  createAccountTemplateFormGroup(accountTemplate: AccountTemplateFormGroupInput = { id: null }): AccountTemplateFormGroup {
    const accountTemplateRawValue = {
      ...this.getFormDefaults(),
      ...accountTemplate,
    };
    return new FormGroup<AccountTemplateFormGroupContent>({
      title: new FormControl(accountTemplateRawValue.title),
      code: new FormControl(accountTemplateRawValue.code),
      level: new FormControl(accountTemplateRawValue.level),
      levelTitle: new FormControl(accountTemplateRawValue.levelTitle),
      id: new FormControl(
        { value: accountTemplateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(accountTemplateRawValue.type),
      levelInTree: new FormControl(accountTemplateRawValue.levelInTree),
      debitAmount: new FormControl(accountTemplateRawValue.debitAmount),
      creditAmount: new FormControl(accountTemplateRawValue.creditAmount),
      typeInFormula: new FormControl(accountTemplateRawValue.typeInFormula),
      status: new FormControl(accountTemplateRawValue.status),
      internalUser: new FormControl(accountTemplateRawValue.internalUser),
      company: new FormControl(accountTemplateRawValue.company),
    });
  }

  getAccountTemplate(form: AccountTemplateFormGroup): IAccountTemplate | NewAccountTemplate {
    return form.getRawValue() as IAccountTemplate | NewAccountTemplate;
  }

  resetForm(form: AccountTemplateFormGroup, accountTemplate: AccountTemplateFormGroupInput): void {
    const accountTemplateRawValue = { ...this.getFormDefaults(), ...accountTemplate };
    form.reset(
      {
        ...accountTemplateRawValue,
        id: { value: accountTemplateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountTemplateFormDefaults {
    return {
      id: null,
    };
  }
}
