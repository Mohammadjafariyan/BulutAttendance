import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILeaveSummary, NewLeaveSummary } from '../leave-summary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeaveSummary for edit and NewLeaveSummaryFormGroupInput for create.
 */
type LeaveSummaryFormGroupInput = ILeaveSummary | PartialWithRequiredKeyOf<NewLeaveSummary>;

type LeaveSummaryFormDefaults = Pick<NewLeaveSummary, 'id'>;

type LeaveSummaryFormGroupContent = {
  remainHours: FormControl<ILeaveSummary['remainHours']>;
  remainDays: FormControl<ILeaveSummary['remainDays']>;
  year: FormControl<ILeaveSummary['year']>;
  id: FormControl<ILeaveSummary['id'] | NewLeaveSummary['id']>;
  status: FormControl<ILeaveSummary['status']>;
  internalUser: FormControl<ILeaveSummary['internalUser']>;
  company: FormControl<ILeaveSummary['company']>;
};

export type LeaveSummaryFormGroup = FormGroup<LeaveSummaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveSummaryFormService {
  createLeaveSummaryFormGroup(leaveSummary: LeaveSummaryFormGroupInput = { id: null }): LeaveSummaryFormGroup {
    const leaveSummaryRawValue = {
      ...this.getFormDefaults(),
      ...leaveSummary,
    };
    return new FormGroup<LeaveSummaryFormGroupContent>({
      remainHours: new FormControl(leaveSummaryRawValue.remainHours),
      remainDays: new FormControl(leaveSummaryRawValue.remainDays),
      year: new FormControl(leaveSummaryRawValue.year),
      id: new FormControl(
        { value: leaveSummaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(leaveSummaryRawValue.status),
      internalUser: new FormControl(leaveSummaryRawValue.internalUser),
      company: new FormControl(leaveSummaryRawValue.company),
    });
  }

  getLeaveSummary(form: LeaveSummaryFormGroup): ILeaveSummary | NewLeaveSummary {
    return form.getRawValue() as ILeaveSummary | NewLeaveSummary;
  }

  resetForm(form: LeaveSummaryFormGroup, leaveSummary: LeaveSummaryFormGroupInput): void {
    const leaveSummaryRawValue = { ...this.getFormDefaults(), ...leaveSummary };
    form.reset(
      {
        ...leaveSummaryRawValue,
        id: { value: leaveSummaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LeaveSummaryFormDefaults {
    return {
      id: null,
    };
  }
}
