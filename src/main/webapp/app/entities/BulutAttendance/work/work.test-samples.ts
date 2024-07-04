import dayjs from 'dayjs/esm';

import { IWork, NewWork } from './work.model';

export const sampleWithRequiredData: IWork = {
  id: 'af4b5b88-ae61-4988-ab60-506c15b7775d',
};

export const sampleWithPartialData: IWork = {
  id: 'ef2bc32a-8706-4392-9f0a-9a4b21d24006',
  desc: 'انتخاب کردن ناشنا اَخ',
};

export const sampleWithFullData: IWork = {
  id: '38bb3993-419a-473c-8aa4-15cf04be34c7',
  issueDate: dayjs('2024-06-10T02:20'),
  desc: 'تا گوش',
  year: 13626,
  month: 4689,
};

export const sampleWithNewData: NewWork = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
