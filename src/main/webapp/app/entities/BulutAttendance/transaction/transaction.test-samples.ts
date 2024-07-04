import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 12966,
};

export const sampleWithPartialData: ITransaction = {
  id: 10738,
  issueDateTime: dayjs('2024-06-10T15:03'),
  totalAmount: 20273.49,
  desc: 'مفید شال',
  docNumber: 'هر چند',
};

export const sampleWithFullData: ITransaction = {
  id: 2344,
  issueDateTime: dayjs('2024-06-10T14:55'),
  totalAmount: 19695.77,
  desc: 'واو',
  docNumber: 'در دسترس',
};

export const sampleWithNewData: NewTransaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
