import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILeave, NewLeave } from '../leave.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeave for edit and NewLeaveFormGroupInput for create.
 */
type LeaveFormGroupInput = ILeave | PartialWithRequiredKeyOf<NewLeave>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILeave | NewLeave> = Omit<T, 'start' | 'end'> & {
  start?: string | null;
  end?: string | null;
};

type LeaveFormRawValue = FormValueOf<ILeave>;

type NewLeaveFormRawValue = FormValueOf<NewLeave>;

type LeaveFormDefaults = Pick<NewLeave, 'start' | 'end' | 'id'>;

type LeaveFormGroupContent = {
  start: FormControl<LeaveFormRawValue['start']>;
  end: FormControl<LeaveFormRawValue['end']>;
  id: FormControl<LeaveFormRawValue['id'] | NewLeave['id']>;
  bpmsApproveStatus: FormControl<LeaveFormRawValue['bpmsApproveStatus']>;
  status: FormControl<LeaveFormRawValue['status']>;
  personnelId: FormControl<LeaveFormRawValue['personnelId']>;
  internalUser: FormControl<LeaveFormRawValue['internalUser']>;
  company: FormControl<LeaveFormRawValue['company']>;
};

export type LeaveFormGroup = FormGroup<LeaveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveFormService {
  createLeaveFormGroup(leave: LeaveFormGroupInput = { id: null }): LeaveFormGroup {
    const leaveRawValue = this.convertLeaveToLeaveRawValue({
      ...this.getFormDefaults(),
      ...leave,
    });
    return new FormGroup<LeaveFormGroupContent>({
      start: new FormControl(leaveRawValue.start),
      end: new FormControl(leaveRawValue.end),
      id: new FormControl(
        { value: leaveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bpmsApproveStatus: new FormControl(leaveRawValue.bpmsApproveStatus),
      status: new FormControl(leaveRawValue.status),
      personnelId: new FormControl(leaveRawValue.personnelId),
      internalUser: new FormControl(leaveRawValue.internalUser),
      company: new FormControl(leaveRawValue.company),
    });
  }

  getLeave(form: LeaveFormGroup): ILeave | NewLeave {
    return this.convertLeaveRawValueToLeave(form.getRawValue() as LeaveFormRawValue | NewLeaveFormRawValue);
  }

  resetForm(form: LeaveFormGroup, leave: LeaveFormGroupInput): void {
    const leaveRawValue = this.convertLeaveToLeaveRawValue({ ...this.getFormDefaults(), ...leave });
    form.reset(
      {
        ...leaveRawValue,
        id: { value: leaveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LeaveFormDefaults {
    const currentTime = dayjs();

    return {
      start: currentTime,
      end: currentTime,
      id: null,
    };
  }

  private convertLeaveRawValueToLeave(rawLeave: LeaveFormRawValue | NewLeaveFormRawValue): ILeave | NewLeave {
    return {
      ...rawLeave,
      start: dayjs(rawLeave.start, DATE_TIME_FORMAT),
      end: dayjs(rawLeave.end, DATE_TIME_FORMAT),
    };
  }

  private convertLeaveToLeaveRawValue(
    leave: ILeave | (Partial<NewLeave> & LeaveFormDefaults),
  ): LeaveFormRawValue | PartialWithRequiredKeyOf<NewLeaveFormRawValue> {
    return {
      ...leave,
      start: leave.start ? leave.start.format(DATE_TIME_FORMAT) : undefined,
      end: leave.end ? leave.end.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
