import { IAccountTemplate, NewAccountTemplate } from './account-template.model';

export const sampleWithRequiredData: IAccountTemplate = {
  id: '93188c30-e5fd-432d-bf62-b453f4308537',
};

export const sampleWithPartialData: IAccountTemplate = {
  title: 'گذاشتن هر چند دریافت کردن',
  id: 'bace64ed-8b01-4870-8daf-51d9a92aab90',
  type: 'Debit',
  levelInTree: 'TAFZIL_3',
  debitAmount: 18820.66,
  creditAmount: 16340.64,
};

export const sampleWithFullData: IAccountTemplate = {
  title: 'هورا فوق‌العاده بین‌المللی',
  code: 'بالا به عنوان بچه',
  level: 2073,
  levelTitle: 'لباس بودن',
  id: 'fa7fa53d-3d1e-4933-afc2-9fff2dd704dd',
  type: 'Debit',
  levelInTree: 'TAFZIL_8',
  debitAmount: 7598.83,
  creditAmount: 27821.11,
  typeInFormula: 'EXPENSES',
};

export const sampleWithNewData: NewAccountTemplate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
