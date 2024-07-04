import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccProcStep, NewAccProcStep } from '../acc-proc-step.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccProcStep for edit and NewAccProcStepFormGroupInput for create.
 */
type AccProcStepFormGroupInput = IAccProcStep | PartialWithRequiredKeyOf<NewAccProcStep>;

type AccProcStepFormDefaults = Pick<NewAccProcStep, 'id'>;

type AccProcStepFormGroupContent = {
  id: FormControl<IAccProcStep['id'] | NewAccProcStep['id']>;
  internalUser: FormControl<IAccProcStep['internalUser']>;
  company: FormControl<IAccProcStep['company']>;
  creditAccount: FormControl<IAccProcStep['creditAccount']>;
  debitAccount: FormControl<IAccProcStep['debitAccount']>;
  parameter: FormControl<IAccProcStep['parameter']>;
  procedure: FormControl<IAccProcStep['procedure']>;
};

export type AccProcStepFormGroup = FormGroup<AccProcStepFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccProcStepFormService {
  createAccProcStepFormGroup(accProcStep: AccProcStepFormGroupInput = { id: null }): AccProcStepFormGroup {
    const accProcStepRawValue = {
      ...this.getFormDefaults(),
      ...accProcStep,
    };
    return new FormGroup<AccProcStepFormGroupContent>({
      id: new FormControl(
        { value: accProcStepRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      internalUser: new FormControl(accProcStepRawValue.internalUser),
      company: new FormControl(accProcStepRawValue.company),
      creditAccount: new FormControl(accProcStepRawValue.creditAccount),
      debitAccount: new FormControl(accProcStepRawValue.debitAccount),
      parameter: new FormControl(accProcStepRawValue.parameter),
      procedure: new FormControl(accProcStepRawValue.procedure),
    });
  }

  getAccProcStep(form: AccProcStepFormGroup): IAccProcStep | NewAccProcStep {
    return form.getRawValue() as IAccProcStep | NewAccProcStep;
  }

  resetForm(form: AccProcStepFormGroup, accProcStep: AccProcStepFormGroupInput): void {
    const accProcStepRawValue = { ...this.getFormDefaults(), ...accProcStep };
    form.reset(
      {
        ...accProcStepRawValue,
        id: { value: accProcStepRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccProcStepFormDefaults {
    return {
      id: null,
    };
  }
}
