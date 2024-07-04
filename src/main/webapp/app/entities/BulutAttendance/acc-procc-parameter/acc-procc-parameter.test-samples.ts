import { IAccProccParameter, NewAccProccParameter } from './acc-procc-parameter.model';

export const sampleWithRequiredData: IAccProccParameter = {
  id: 18853,
};

export const sampleWithPartialData: IAccProccParameter = {
  id: 32482,
  title: 'بین',
  isDeducInsurance: false,
  hokm: 'LABOR',
  other: 'Supplementary_insurance',
};

export const sampleWithFullData: IAccProccParameter = {
  id: 14964,
  title: 'شهر به نوعی',
  manualOrAuto: 'BySystem',
  formula: 'و کارگر',
  unit: 'Hourly',
  isDeducTax: true,
  isDeducInsurance: false,
  laborTime: 'SANAVAT',
  hokm: 'RANGE',
  earnings: 'Other',
  deduction: 'Total_Deduction',
  other: 'Supplementary_insurance',
};

export const sampleWithNewData: NewAccProccParameter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
