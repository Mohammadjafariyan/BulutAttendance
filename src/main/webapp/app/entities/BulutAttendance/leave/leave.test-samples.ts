import dayjs from 'dayjs/esm';

import { ILeave, NewLeave } from './leave.model';

export const sampleWithRequiredData: ILeave = {
  id: '110f0c64-0b61-4f17-b774-2ebffcdae71a',
};

export const sampleWithPartialData: ILeave = {
  start: dayjs('2024-06-10T14:10'),
  end: dayjs('2024-06-10T02:44'),
  id: 'd082ec95-a208-419a-b53f-ff5066e0b1b3',
};

export const sampleWithFullData: ILeave = {
  start: dayjs('2024-06-09T23:17'),
  end: dayjs('2024-06-09T17:13'),
  id: '8ba55c95-0b00-44a8-b44d-bfe14452e14d',
  bpmsApproveStatus: 5564,
};

export const sampleWithNewData: NewLeave = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
