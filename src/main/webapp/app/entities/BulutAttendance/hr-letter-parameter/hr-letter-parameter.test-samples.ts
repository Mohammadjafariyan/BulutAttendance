import { IHrLetterParameter, NewHrLetterParameter } from './hr-letter-parameter.model';

export const sampleWithRequiredData: IHrLetterParameter = {
  id: 6037,
};

export const sampleWithPartialData: IHrLetterParameter = {
  id: 9488,
  formula: 'فهمیدن',
  unit: 'Hourly',
  isDeducTax: false,
  isDeducInsurance: true,
  laborTime: 'TOTAL_WORK',
  earnings: 'OverTime',
  deduction: 'Insurance_Labor',
  isEnabled: false,
};

export const sampleWithFullData: IHrLetterParameter = {
  id: 4381,
  title: 'فکر کردن درباره اوف',
  manualOrAuto: 'Constant',
  formula: 'خبر',
  unit: 'Hourly',
  isDeducTax: false,
  isDeducInsurance: false,
  laborTime: 'NIGHT_WORK',
  hokm: 'TOTAL_HOKM',
  earnings: 'Shift',
  deduction: 'Prepaid',
  other: 'Total_Deduction',
  isEnabled: false,
};

export const sampleWithNewData: NewHrLetterParameter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
