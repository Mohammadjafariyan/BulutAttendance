import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHrLetter, NewHrLetter } from '../hr-letter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHrLetter for edit and NewHrLetterFormGroupInput for create.
 */
type HrLetterFormGroupInput = IHrLetter | PartialWithRequiredKeyOf<NewHrLetter>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHrLetter | NewHrLetter> = Omit<T, 'issueDate' | 'executionDate'> & {
  issueDate?: string | null;
  executionDate?: string | null;
};

type HrLetterFormRawValue = FormValueOf<IHrLetter>;

type NewHrLetterFormRawValue = FormValueOf<NewHrLetter>;

type HrLetterFormDefaults = Pick<NewHrLetter, 'id' | 'issueDate' | 'executionDate'>;

type HrLetterFormGroupContent = {
  title: FormControl<HrLetterFormRawValue['title']>;
  uniqueNumber: FormControl<HrLetterFormRawValue['uniqueNumber']>;
  id: FormControl<HrLetterFormRawValue['id'] | NewHrLetter['id']>;
  issueDate: FormControl<HrLetterFormRawValue['issueDate']>;
  executionDate: FormControl<HrLetterFormRawValue['executionDate']>;
  bpmsApproveStatus: FormControl<HrLetterFormRawValue['bpmsApproveStatus']>;
  status: FormControl<HrLetterFormRawValue['status']>;
  type: FormControl<HrLetterFormRawValue['type']>;
  personnelId: FormControl<HrLetterFormRawValue['personnelId']>;
  orgPosition: FormControl<HrLetterFormRawValue['orgPosition']>;
  orgUnit: FormControl<HrLetterFormRawValue['orgUnit']>;
  personnelStatus: FormControl<HrLetterFormRawValue['personnelStatus']>;
  hrLetterParameter: FormControl<HrLetterFormRawValue['hrLetterParameter']>;
  internalUser: FormControl<HrLetterFormRawValue['internalUser']>;
  company: FormControl<HrLetterFormRawValue['company']>;
};

export type HrLetterFormGroup = FormGroup<HrLetterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HrLetterFormService {
  createHrLetterFormGroup(hrLetter: HrLetterFormGroupInput = { id: null }): HrLetterFormGroup {
    const hrLetterRawValue = this.convertHrLetterToHrLetterRawValue({
      ...this.getFormDefaults(),
      ...hrLetter,
    });
    return new FormGroup<HrLetterFormGroupContent>({
      title: new FormControl(hrLetterRawValue.title),
      uniqueNumber: new FormControl(hrLetterRawValue.uniqueNumber),
      id: new FormControl(
        { value: hrLetterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      issueDate: new FormControl(hrLetterRawValue.issueDate),
      executionDate: new FormControl(hrLetterRawValue.executionDate),
      bpmsApproveStatus: new FormControl(hrLetterRawValue.bpmsApproveStatus),
      status: new FormControl(hrLetterRawValue.status),
      type: new FormControl(hrLetterRawValue.type),
      personnelId: new FormControl(hrLetterRawValue.personnelId),
      orgPosition: new FormControl(hrLetterRawValue.orgPosition),
      orgUnit: new FormControl(hrLetterRawValue.orgUnit),
      personnelStatus: new FormControl(hrLetterRawValue.personnelStatus),
      hrLetterParameter: new FormControl(hrLetterRawValue.hrLetterParameter),
      internalUser: new FormControl(hrLetterRawValue.internalUser),
      company: new FormControl(hrLetterRawValue.company),
    });
  }

  getHrLetter(form: HrLetterFormGroup): IHrLetter | NewHrLetter {
    return this.convertHrLetterRawValueToHrLetter(form.getRawValue() as HrLetterFormRawValue | NewHrLetterFormRawValue);
  }

  resetForm(form: HrLetterFormGroup, hrLetter: HrLetterFormGroupInput): void {
    const hrLetterRawValue = this.convertHrLetterToHrLetterRawValue({ ...this.getFormDefaults(), ...hrLetter });
    form.reset(
      {
        ...hrLetterRawValue,
        id: { value: hrLetterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HrLetterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
      executionDate: currentTime,
    };
  }

  private convertHrLetterRawValueToHrLetter(rawHrLetter: HrLetterFormRawValue | NewHrLetterFormRawValue): IHrLetter | NewHrLetter {
    return {
      ...rawHrLetter,
      issueDate: dayjs(rawHrLetter.issueDate, DATE_TIME_FORMAT),
      executionDate: dayjs(rawHrLetter.executionDate, DATE_TIME_FORMAT),
    };
  }

  private convertHrLetterToHrLetterRawValue(
    hrLetter: IHrLetter | (Partial<NewHrLetter> & HrLetterFormDefaults),
  ): HrLetterFormRawValue | PartialWithRequiredKeyOf<NewHrLetterFormRawValue> {
    return {
      ...hrLetter,
      issueDate: hrLetter.issueDate ? hrLetter.issueDate.format(DATE_TIME_FORMAT) : undefined,
      executionDate: hrLetter.executionDate ? hrLetter.executionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
