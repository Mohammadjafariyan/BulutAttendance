import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkItem, NewWorkItem } from '../work-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkItem for edit and NewWorkItemFormGroupInput for create.
 */
type WorkItemFormGroupInput = IWorkItem | PartialWithRequiredKeyOf<NewWorkItem>;

type WorkItemFormDefaults = Pick<NewWorkItem, 'id'>;

type WorkItemFormGroupContent = {
  id: FormControl<IWorkItem['id'] | NewWorkItem['id']>;
  amount: FormControl<IWorkItem['amount']>;
  hrLetterParameter: FormControl<IWorkItem['hrLetterParameter']>;
  internalUser: FormControl<IWorkItem['internalUser']>;
  company: FormControl<IWorkItem['company']>;
  work: FormControl<IWorkItem['work']>;
};

export type WorkItemFormGroup = FormGroup<WorkItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkItemFormService {
  createWorkItemFormGroup(workItem: WorkItemFormGroupInput = { id: null }): WorkItemFormGroup {
    const workItemRawValue = {
      ...this.getFormDefaults(),
      ...workItem,
    };
    return new FormGroup<WorkItemFormGroupContent>({
      id: new FormControl(
        { value: workItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      amount: new FormControl(workItemRawValue.amount),
      hrLetterParameter: new FormControl(workItemRawValue.hrLetterParameter),
      internalUser: new FormControl(workItemRawValue.internalUser),
      company: new FormControl(workItemRawValue.company),
      work: new FormControl(workItemRawValue.work),
    });
  }

  getWorkItem(form: WorkItemFormGroup): IWorkItem | NewWorkItem {
    return form.getRawValue() as IWorkItem | NewWorkItem;
  }

  resetForm(form: WorkItemFormGroup, workItem: WorkItemFormGroupInput): void {
    const workItemRawValue = { ...this.getFormDefaults(), ...workItem };
    form.reset(
      {
        ...workItemRawValue,
        id: { value: workItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkItemFormDefaults {
    return {
      id: null,
    };
  }
}
