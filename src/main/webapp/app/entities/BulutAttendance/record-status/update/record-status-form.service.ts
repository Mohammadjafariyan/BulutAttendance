import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecordStatus, NewRecordStatus } from '../record-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecordStatus for edit and NewRecordStatusFormGroupInput for create.
 */
type RecordStatusFormGroupInput = IRecordStatus | PartialWithRequiredKeyOf<NewRecordStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecordStatus | NewRecordStatus> = Omit<T, 'fromDateTime' | 'toDateTime'> & {
  fromDateTime?: string | null;
  toDateTime?: string | null;
};

type RecordStatusFormRawValue = FormValueOf<IRecordStatus>;

type NewRecordStatusFormRawValue = FormValueOf<NewRecordStatus>;

type RecordStatusFormDefaults = Pick<NewRecordStatus, 'fromDateTime' | 'toDateTime' | 'isDeleted' | 'id'>;

type RecordStatusFormGroupContent = {
  fromDateTime: FormControl<RecordStatusFormRawValue['fromDateTime']>;
  toDateTime: FormControl<RecordStatusFormRawValue['toDateTime']>;
  isDeleted: FormControl<RecordStatusFormRawValue['isDeleted']>;
  id: FormControl<RecordStatusFormRawValue['id'] | NewRecordStatus['id']>;
};

export type RecordStatusFormGroup = FormGroup<RecordStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecordStatusFormService {
  createRecordStatusFormGroup(recordStatus: RecordStatusFormGroupInput = { id: null }): RecordStatusFormGroup {
    const recordStatusRawValue = this.convertRecordStatusToRecordStatusRawValue({
      ...this.getFormDefaults(),
      ...recordStatus,
    });
    return new FormGroup<RecordStatusFormGroupContent>({
      fromDateTime: new FormControl(recordStatusRawValue.fromDateTime),
      toDateTime: new FormControl(recordStatusRawValue.toDateTime),
      isDeleted: new FormControl(recordStatusRawValue.isDeleted),
      id: new FormControl(
        { value: recordStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getRecordStatus(form: RecordStatusFormGroup): IRecordStatus | NewRecordStatus {
    return this.convertRecordStatusRawValueToRecordStatus(form.getRawValue() as RecordStatusFormRawValue | NewRecordStatusFormRawValue);
  }

  resetForm(form: RecordStatusFormGroup, recordStatus: RecordStatusFormGroupInput): void {
    const recordStatusRawValue = this.convertRecordStatusToRecordStatusRawValue({ ...this.getFormDefaults(), ...recordStatus });
    form.reset(
      {
        ...recordStatusRawValue,
        id: { value: recordStatusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecordStatusFormDefaults {
    const currentTime = dayjs();

    return {
      fromDateTime: currentTime,
      toDateTime: currentTime,
      isDeleted: false,
      id: null,
    };
  }

  private convertRecordStatusRawValueToRecordStatus(
    rawRecordStatus: RecordStatusFormRawValue | NewRecordStatusFormRawValue,
  ): IRecordStatus | NewRecordStatus {
    return {
      ...rawRecordStatus,
      fromDateTime: dayjs(rawRecordStatus.fromDateTime, DATE_TIME_FORMAT),
      toDateTime: dayjs(rawRecordStatus.toDateTime, DATE_TIME_FORMAT),
    };
  }

  private convertRecordStatusToRecordStatusRawValue(
    recordStatus: IRecordStatus | (Partial<NewRecordStatus> & RecordStatusFormDefaults),
  ): RecordStatusFormRawValue | PartialWithRequiredKeyOf<NewRecordStatusFormRawValue> {
    return {
      ...recordStatus,
      fromDateTime: recordStatus.fromDateTime ? recordStatus.fromDateTime.format(DATE_TIME_FORMAT) : undefined,
      toDateTime: recordStatus.toDateTime ? recordStatus.toDateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
