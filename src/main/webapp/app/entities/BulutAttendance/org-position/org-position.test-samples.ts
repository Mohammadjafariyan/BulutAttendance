import { IOrgPosition, NewOrgPosition } from './org-position.model';

export const sampleWithRequiredData: IOrgPosition = {
  id: '08d9b670-664d-4e3f-8754-7fb2fc623573',
};

export const sampleWithPartialData: IOrgPosition = {
  title: 'اَخ',
  id: '5d5a14e6-6fda-4013-87e3-653e2d78b800',
};

export const sampleWithFullData: IOrgPosition = {
  title: 'عالی آه',
  id: 'f4e7fdb2-25ee-41fe-a372-858609354150',
};

export const sampleWithNewData: NewOrgPosition = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
