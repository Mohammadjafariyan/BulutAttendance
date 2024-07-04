import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWork, NewWork } from '../work.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWork for edit and NewWorkFormGroupInput for create.
 */
type WorkFormGroupInput = IWork | PartialWithRequiredKeyOf<NewWork>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWork | NewWork> = Omit<T, 'issueDate'> & {
  issueDate?: string | null;
};

type WorkFormRawValue = FormValueOf<IWork>;

type NewWorkFormRawValue = FormValueOf<NewWork>;

type WorkFormDefaults = Pick<NewWork, 'id' | 'issueDate'>;

type WorkFormGroupContent = {
  id: FormControl<WorkFormRawValue['id'] | NewWork['id']>;
  issueDate: FormControl<WorkFormRawValue['issueDate']>;
  desc: FormControl<WorkFormRawValue['desc']>;
  year: FormControl<WorkFormRawValue['year']>;
  month: FormControl<WorkFormRawValue['month']>;
  internalUser: FormControl<WorkFormRawValue['internalUser']>;
  company: FormControl<WorkFormRawValue['company']>;
};

export type WorkFormGroup = FormGroup<WorkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkFormService {
  createWorkFormGroup(work: WorkFormGroupInput = { id: null }): WorkFormGroup {
    const workRawValue = this.convertWorkToWorkRawValue({
      ...this.getFormDefaults(),
      ...work,
    });
    return new FormGroup<WorkFormGroupContent>({
      id: new FormControl(
        { value: workRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      issueDate: new FormControl(workRawValue.issueDate),
      desc: new FormControl(workRawValue.desc),
      year: new FormControl(workRawValue.year),
      month: new FormControl(workRawValue.month),
      internalUser: new FormControl(workRawValue.internalUser),
      company: new FormControl(workRawValue.company),
    });
  }

  getWork(form: WorkFormGroup): IWork | NewWork {
    return this.convertWorkRawValueToWork(form.getRawValue() as WorkFormRawValue | NewWorkFormRawValue);
  }

  resetForm(form: WorkFormGroup, work: WorkFormGroupInput): void {
    const workRawValue = this.convertWorkToWorkRawValue({ ...this.getFormDefaults(), ...work });
    form.reset(
      {
        ...workRawValue,
        id: { value: workRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
    };
  }

  private convertWorkRawValueToWork(rawWork: WorkFormRawValue | NewWorkFormRawValue): IWork | NewWork {
    return {
      ...rawWork,
      issueDate: dayjs(rawWork.issueDate, DATE_TIME_FORMAT),
    };
  }

  private convertWorkToWorkRawValue(
    work: IWork | (Partial<NewWork> & WorkFormDefaults),
  ): WorkFormRawValue | PartialWithRequiredKeyOf<NewWorkFormRawValue> {
    return {
      ...work,
      issueDate: work.issueDate ? work.issueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
