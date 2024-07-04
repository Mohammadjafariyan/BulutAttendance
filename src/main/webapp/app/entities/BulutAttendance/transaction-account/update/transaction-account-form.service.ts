import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITransactionAccount, NewTransactionAccount } from '../transaction-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionAccount for edit and NewTransactionAccountFormGroupInput for create.
 */
type TransactionAccountFormGroupInput = ITransactionAccount | PartialWithRequiredKeyOf<NewTransactionAccount>;

type TransactionAccountFormDefaults = Pick<NewTransactionAccount, 'id'>;

type TransactionAccountFormGroupContent = {
  id: FormControl<ITransactionAccount['id'] | NewTransactionAccount['id']>;
  debitAmount: FormControl<ITransactionAccount['debitAmount']>;
  creditAmount: FormControl<ITransactionAccount['creditAmount']>;
  internalUser: FormControl<ITransactionAccount['internalUser']>;
  company: FormControl<ITransactionAccount['company']>;
  account: FormControl<ITransactionAccount['account']>;
  transaction: FormControl<ITransactionAccount['transaction']>;
};

export type TransactionAccountFormGroup = FormGroup<TransactionAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionAccountFormService {
  createTransactionAccountFormGroup(transactionAccount: TransactionAccountFormGroupInput = { id: null }): TransactionAccountFormGroup {
    const transactionAccountRawValue = {
      ...this.getFormDefaults(),
      ...transactionAccount,
    };
    return new FormGroup<TransactionAccountFormGroupContent>({
      id: new FormControl(
        { value: transactionAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      debitAmount: new FormControl(transactionAccountRawValue.debitAmount),
      creditAmount: new FormControl(transactionAccountRawValue.creditAmount),
      internalUser: new FormControl(transactionAccountRawValue.internalUser),
      company: new FormControl(transactionAccountRawValue.company),
      account: new FormControl(transactionAccountRawValue.account),
      transaction: new FormControl(transactionAccountRawValue.transaction),
    });
  }

  getTransactionAccount(form: TransactionAccountFormGroup): ITransactionAccount | NewTransactionAccount {
    return form.getRawValue() as ITransactionAccount | NewTransactionAccount;
  }

  resetForm(form: TransactionAccountFormGroup, transactionAccount: TransactionAccountFormGroupInput): void {
    const transactionAccountRawValue = { ...this.getFormDefaults(), ...transactionAccount };
    form.reset(
      {
        ...transactionAccountRawValue,
        id: { value: transactionAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionAccountFormDefaults {
    return {
      id: null,
    };
  }
}
