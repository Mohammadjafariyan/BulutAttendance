import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccProcStepExecution, NewAccProcStepExecution } from '../acc-proc-step-execution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccProcStepExecution for edit and NewAccProcStepExecutionFormGroupInput for create.
 */
type AccProcStepExecutionFormGroupInput = IAccProcStepExecution | PartialWithRequiredKeyOf<NewAccProcStepExecution>;

type AccProcStepExecutionFormDefaults = Pick<NewAccProcStepExecution, 'id'>;

type AccProcStepExecutionFormGroupContent = {
  id: FormControl<IAccProcStepExecution['id'] | NewAccProcStepExecution['id']>;
  debitAmount: FormControl<IAccProcStepExecution['debitAmount']>;
  creditAmount: FormControl<IAccProcStepExecution['creditAmount']>;
  desc: FormControl<IAccProcStepExecution['desc']>;
  internalUser: FormControl<IAccProcStepExecution['internalUser']>;
  company: FormControl<IAccProcStepExecution['company']>;
  creditAccount: FormControl<IAccProcStepExecution['creditAccount']>;
  debitAccount: FormControl<IAccProcStepExecution['debitAccount']>;
  procedure: FormControl<IAccProcStepExecution['procedure']>;
  step: FormControl<IAccProcStepExecution['step']>;
};

export type AccProcStepExecutionFormGroup = FormGroup<AccProcStepExecutionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccProcStepExecutionFormService {
  createAccProcStepExecutionFormGroup(
    accProcStepExecution: AccProcStepExecutionFormGroupInput = { id: null },
  ): AccProcStepExecutionFormGroup {
    const accProcStepExecutionRawValue = {
      ...this.getFormDefaults(),
      ...accProcStepExecution,
    };
    return new FormGroup<AccProcStepExecutionFormGroupContent>({
      id: new FormControl(
        { value: accProcStepExecutionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      debitAmount: new FormControl(accProcStepExecutionRawValue.debitAmount),
      creditAmount: new FormControl(accProcStepExecutionRawValue.creditAmount),
      desc: new FormControl(accProcStepExecutionRawValue.desc),
      internalUser: new FormControl(accProcStepExecutionRawValue.internalUser),
      company: new FormControl(accProcStepExecutionRawValue.company),
      creditAccount: new FormControl(accProcStepExecutionRawValue.creditAccount),
      debitAccount: new FormControl(accProcStepExecutionRawValue.debitAccount),
      procedure: new FormControl(accProcStepExecutionRawValue.procedure),
      step: new FormControl(accProcStepExecutionRawValue.step),
    });
  }

  getAccProcStepExecution(form: AccProcStepExecutionFormGroup): IAccProcStepExecution | NewAccProcStepExecution {
    return form.getRawValue() as IAccProcStepExecution | NewAccProcStepExecution;
  }

  resetForm(form: AccProcStepExecutionFormGroup, accProcStepExecution: AccProcStepExecutionFormGroupInput): void {
    const accProcStepExecutionRawValue = { ...this.getFormDefaults(), ...accProcStepExecution };
    form.reset(
      {
        ...accProcStepExecutionRawValue,
        id: { value: accProcStepExecutionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccProcStepExecutionFormDefaults {
    return {
      id: null,
    };
  }
}
