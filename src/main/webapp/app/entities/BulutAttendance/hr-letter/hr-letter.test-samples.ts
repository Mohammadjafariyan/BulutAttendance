import dayjs from 'dayjs/esm';

import { IHrLetter, NewHrLetter } from './hr-letter.model';

export const sampleWithRequiredData: IHrLetter = {
  id: '098e8db6-9696-4cb0-9818-7c39ca9e66f7',
};

export const sampleWithPartialData: IHrLetter = {
  title: 'کوهستانی ultimately',
  uniqueNumber: 'احساس کردن مجبور کردن',
  id: '3ff2f4dc-dc1a-4a96-af88-941935203059',
  bpmsApproveStatus: 27941,
};

export const sampleWithFullData: IHrLetter = {
  title: 'مفید',
  uniqueNumber: 'زیرا انجام دادن شیرین',
  id: '4bd41f57-3555-4944-a36e-f9b75555bb3d',
  issueDate: dayjs('2024-06-09T22:57'),
  executionDate: dayjs('2024-06-10T04:00'),
  bpmsApproveStatus: 12813,
};

export const sampleWithNewData: NewHrLetter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
