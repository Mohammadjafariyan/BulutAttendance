import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccountingProcedure, NewAccountingProcedure } from '../accounting-procedure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountingProcedure for edit and NewAccountingProcedureFormGroupInput for create.
 */
type AccountingProcedureFormGroupInput = IAccountingProcedure | PartialWithRequiredKeyOf<NewAccountingProcedure>;

type AccountingProcedureFormDefaults = Pick<NewAccountingProcedure, 'id'>;

type AccountingProcedureFormGroupContent = {
  id: FormControl<IAccountingProcedure['id'] | NewAccountingProcedure['id']>;
  title: FormControl<IAccountingProcedure['title']>;
  status: FormControl<IAccountingProcedure['status']>;
  internalUser: FormControl<IAccountingProcedure['internalUser']>;
  company: FormControl<IAccountingProcedure['company']>;
  executeAfter: FormControl<IAccountingProcedure['executeAfter']>;
};

export type AccountingProcedureFormGroup = FormGroup<AccountingProcedureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountingProcedureFormService {
  createAccountingProcedureFormGroup(accountingProcedure: AccountingProcedureFormGroupInput = { id: null }): AccountingProcedureFormGroup {
    const accountingProcedureRawValue = {
      ...this.getFormDefaults(),
      ...accountingProcedure,
    };
    return new FormGroup<AccountingProcedureFormGroupContent>({
      id: new FormControl(
        { value: accountingProcedureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(accountingProcedureRawValue.title),
      status: new FormControl(accountingProcedureRawValue.status),
      internalUser: new FormControl(accountingProcedureRawValue.internalUser),
      company: new FormControl(accountingProcedureRawValue.company),
      executeAfter: new FormControl(accountingProcedureRawValue.executeAfter),
    });
  }

  getAccountingProcedure(form: AccountingProcedureFormGroup): IAccountingProcedure | NewAccountingProcedure {
    return form.getRawValue() as IAccountingProcedure | NewAccountingProcedure;
  }

  resetForm(form: AccountingProcedureFormGroup, accountingProcedure: AccountingProcedureFormGroupInput): void {
    const accountingProcedureRawValue = { ...this.getFormDefaults(), ...accountingProcedure };
    form.reset(
      {
        ...accountingProcedureRawValue,
        id: { value: accountingProcedureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountingProcedureFormDefaults {
    return {
      id: null,
    };
  }
}
