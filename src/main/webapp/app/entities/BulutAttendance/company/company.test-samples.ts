import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 13485,
};

export const sampleWithPartialData: ICompany = {
  id: 17413,
  title: 'اصلی',
  logo: 'زیرا نزدیک و',
};

export const sampleWithFullData: ICompany = {
  id: 14077,
  title: 'تحت آتش بنابراین',
  logo: 'روی',
};

export const sampleWithNewData: NewCompany = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
