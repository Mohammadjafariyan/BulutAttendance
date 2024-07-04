import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccountingProcedureExecution, NewAccountingProcedureExecution } from '../accounting-procedure-execution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountingProcedureExecution for edit and NewAccountingProcedureExecutionFormGroupInput for create.
 */
type AccountingProcedureExecutionFormGroupInput = IAccountingProcedureExecution | PartialWithRequiredKeyOf<NewAccountingProcedureExecution>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAccountingProcedureExecution | NewAccountingProcedureExecution> = Omit<T, 'dateTime'> & {
  dateTime?: string | null;
};

type AccountingProcedureExecutionFormRawValue = FormValueOf<IAccountingProcedureExecution>;

type NewAccountingProcedureExecutionFormRawValue = FormValueOf<NewAccountingProcedureExecution>;

type AccountingProcedureExecutionFormDefaults = Pick<NewAccountingProcedureExecution, 'id' | 'dateTime'>;

type AccountingProcedureExecutionFormGroupContent = {
  id: FormControl<AccountingProcedureExecutionFormRawValue['id'] | NewAccountingProcedureExecution['id']>;
  dateTime: FormControl<AccountingProcedureExecutionFormRawValue['dateTime']>;
  desc: FormControl<AccountingProcedureExecutionFormRawValue['desc']>;
  internalUser: FormControl<AccountingProcedureExecutionFormRawValue['internalUser']>;
  company: FormControl<AccountingProcedureExecutionFormRawValue['company']>;
  procedure: FormControl<AccountingProcedureExecutionFormRawValue['procedure']>;
};

export type AccountingProcedureExecutionFormGroup = FormGroup<AccountingProcedureExecutionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountingProcedureExecutionFormService {
  createAccountingProcedureExecutionFormGroup(
    accountingProcedureExecution: AccountingProcedureExecutionFormGroupInput = { id: null },
  ): AccountingProcedureExecutionFormGroup {
    const accountingProcedureExecutionRawValue = this.convertAccountingProcedureExecutionToAccountingProcedureExecutionRawValue({
      ...this.getFormDefaults(),
      ...accountingProcedureExecution,
    });
    return new FormGroup<AccountingProcedureExecutionFormGroupContent>({
      id: new FormControl(
        { value: accountingProcedureExecutionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateTime: new FormControl(accountingProcedureExecutionRawValue.dateTime),
      desc: new FormControl(accountingProcedureExecutionRawValue.desc),
      internalUser: new FormControl(accountingProcedureExecutionRawValue.internalUser),
      company: new FormControl(accountingProcedureExecutionRawValue.company),
      procedure: new FormControl(accountingProcedureExecutionRawValue.procedure),
    });
  }

  getAccountingProcedureExecution(
    form: AccountingProcedureExecutionFormGroup,
  ): IAccountingProcedureExecution | NewAccountingProcedureExecution {
    return this.convertAccountingProcedureExecutionRawValueToAccountingProcedureExecution(
      form.getRawValue() as AccountingProcedureExecutionFormRawValue | NewAccountingProcedureExecutionFormRawValue,
    );
  }

  resetForm(form: AccountingProcedureExecutionFormGroup, accountingProcedureExecution: AccountingProcedureExecutionFormGroupInput): void {
    const accountingProcedureExecutionRawValue = this.convertAccountingProcedureExecutionToAccountingProcedureExecutionRawValue({
      ...this.getFormDefaults(),
      ...accountingProcedureExecution,
    });
    form.reset(
      {
        ...accountingProcedureExecutionRawValue,
        id: { value: accountingProcedureExecutionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountingProcedureExecutionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTime: currentTime,
    };
  }

  private convertAccountingProcedureExecutionRawValueToAccountingProcedureExecution(
    rawAccountingProcedureExecution: AccountingProcedureExecutionFormRawValue | NewAccountingProcedureExecutionFormRawValue,
  ): IAccountingProcedureExecution | NewAccountingProcedureExecution {
    return {
      ...rawAccountingProcedureExecution,
      dateTime: dayjs(rawAccountingProcedureExecution.dateTime, DATE_TIME_FORMAT),
    };
  }

  private convertAccountingProcedureExecutionToAccountingProcedureExecutionRawValue(
    accountingProcedureExecution:
      | IAccountingProcedureExecution
      | (Partial<NewAccountingProcedureExecution> & AccountingProcedureExecutionFormDefaults),
  ): AccountingProcedureExecutionFormRawValue | PartialWithRequiredKeyOf<NewAccountingProcedureExecutionFormRawValue> {
    return {
      ...accountingProcedureExecution,
      dateTime: accountingProcedureExecution.dateTime ? accountingProcedureExecution.dateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
