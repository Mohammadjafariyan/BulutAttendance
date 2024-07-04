import dayjs from 'dayjs/esm';

import { IAccountingProcedureExecution, NewAccountingProcedureExecution } from './accounting-procedure-execution.model';

export const sampleWithRequiredData: IAccountingProcedureExecution = {
  id: 14266,
};

export const sampleWithPartialData: IAccountingProcedureExecution = {
  id: 13328,
  dateTime: dayjs('2024-06-09T16:31'),
  desc: 'بنابراین نامناسب',
};

export const sampleWithFullData: IAccountingProcedureExecution = {
  id: 27794,
  dateTime: dayjs('2024-06-09T17:09'),
  desc: 'همچون پر upliftingly',
};

export const sampleWithNewData: NewAccountingProcedureExecution = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
