import dayjs from 'dayjs/esm';

import { IRecordStatus, NewRecordStatus } from './record-status.model';

export const sampleWithRequiredData: IRecordStatus = {
  id: 'eae558bc-7ee0-490f-9e6a-e342a26089d7',
};

export const sampleWithPartialData: IRecordStatus = {
  fromDateTime: dayjs('2024-06-10T07:00'),
  toDateTime: dayjs('2024-06-10T09:35'),
  id: 'a554c569-85dd-4e5a-acea-8c4378fa272d',
};

export const sampleWithFullData: IRecordStatus = {
  fromDateTime: dayjs('2024-06-10T10:06'),
  toDateTime: dayjs('2024-06-09T16:30'),
  isDeleted: false,
  id: '7284f212-3930-4a98-9aa2-f66331363974',
};

export const sampleWithNewData: NewRecordStatus = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
