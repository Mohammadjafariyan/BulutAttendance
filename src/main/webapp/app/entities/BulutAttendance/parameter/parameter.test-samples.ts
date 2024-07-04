import { IParameter, NewParameter } from './parameter.model';

export const sampleWithRequiredData: IParameter = {
  id: 538,
};

export const sampleWithPartialData: IParameter = {
  id: 28877,
  isDeducTax: false,
  hokm: 'Other',
  deduction: 'Tax',
};

export const sampleWithFullData: IParameter = {
  id: 23296,
  title: 'هورا آفرین هورا',
  manualOrAuto: 'Constant',
  formula: 'خواستن limply',
  unit: 'Hourly',
  isDeducTax: false,
  isDeducInsurance: true,
  laborTime: 'SHIFT_15',
  hokm: 'BASE',
  earnings: 'YEARLY_BONUS',
  deduction: 'Total_Deduction',
  other: 'Insurance_Labor',
};

export const sampleWithNewData: NewParameter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
