import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccountHesab, NewAccountHesab } from '../account-hesab.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountHesab for edit and NewAccountHesabFormGroupInput for create.
 */
type AccountHesabFormGroupInput = IAccountHesab | PartialWithRequiredKeyOf<NewAccountHesab>;

type AccountHesabFormDefaults = Pick<NewAccountHesab, 'id'>;

type AccountHesabFormGroupContent = {
  title: FormControl<IAccountHesab['title']>;
  code: FormControl<IAccountHesab['code']>;
  level: FormControl<IAccountHesab['level']>;
  levelTitle: FormControl<IAccountHesab['levelTitle']>;
  id: FormControl<IAccountHesab['id'] | NewAccountHesab['id']>;
  type: FormControl<IAccountHesab['type']>;
  levelInTree: FormControl<IAccountHesab['levelInTree']>;
  debitAmount: FormControl<IAccountHesab['debitAmount']>;
  creditAmount: FormControl<IAccountHesab['creditAmount']>;
  typeInFormula: FormControl<IAccountHesab['typeInFormula']>;
  status: FormControl<IAccountHesab['status']>;
  internalUser: FormControl<IAccountHesab['internalUser']>;
  company: FormControl<IAccountHesab['company']>;
  parentAccountId: FormControl<IAccountHesab['parentAccountId']>;
  personnelId: FormControl<IAccountHesab['personnelId']>;
  bank: FormControl<IAccountHesab['bank']>;
};

export type AccountHesabFormGroup = FormGroup<AccountHesabFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountHesabFormService {
  createAccountHesabFormGroup(accountHesab: AccountHesabFormGroupInput = { id: null }): AccountHesabFormGroup {
    const accountHesabRawValue = {
      ...this.getFormDefaults(),
      ...accountHesab,
    };
    return new FormGroup<AccountHesabFormGroupContent>({
      title: new FormControl(accountHesabRawValue.title),
      code: new FormControl(accountHesabRawValue.code),
      level: new FormControl(accountHesabRawValue.level),
      levelTitle: new FormControl(accountHesabRawValue.levelTitle),
      id: new FormControl(
        { value: accountHesabRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(accountHesabRawValue.type),
      levelInTree: new FormControl(accountHesabRawValue.levelInTree),
      debitAmount: new FormControl(accountHesabRawValue.debitAmount),
      creditAmount: new FormControl(accountHesabRawValue.creditAmount),
      typeInFormula: new FormControl(accountHesabRawValue.typeInFormula),
      status: new FormControl(accountHesabRawValue.status),
      internalUser: new FormControl(accountHesabRawValue.internalUser),
      company: new FormControl(accountHesabRawValue.company),
      parentAccountId: new FormControl(accountHesabRawValue.parentAccountId),
      personnelId: new FormControl(accountHesabRawValue.personnelId),
      bank: new FormControl(accountHesabRawValue.bank),
    });
  }

  getAccountHesab(form: AccountHesabFormGroup): IAccountHesab | NewAccountHesab {
    return form.getRawValue() as IAccountHesab | NewAccountHesab;
  }

  resetForm(form: AccountHesabFormGroup, accountHesab: AccountHesabFormGroupInput): void {
    const accountHesabRawValue = { ...this.getFormDefaults(), ...accountHesab };
    form.reset(
      {
        ...accountHesabRawValue,
        id: { value: accountHesabRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountHesabFormDefaults {
    return {
      id: null,
    };
  }
}
