import { ITaxTemplate, NewTaxTemplate } from './tax-template.model';

export const sampleWithRequiredData: ITaxTemplate = {
  id: 'ed4df2ab-3f60-4763-930c-68cf3fae2047',
};

export const sampleWithPartialData: ITaxTemplate = {
  rangeFrom: 9234.09,
  rangeTo: 29086.06,
  id: 'b3f5c7f8-2a62-4bf7-86c6-116f5794e2e7',
};

export const sampleWithFullData: ITaxTemplate = {
  rangeFrom: 12794.54,
  rangeTo: 10389.25,
  percent: 14975,
  year: 20713,
  id: '73d448f2-593a-4696-b800-10d64afb237d',
};

export const sampleWithNewData: NewTaxTemplate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
