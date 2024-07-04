import { IWorkItem, NewWorkItem } from './work-item.model';

export const sampleWithRequiredData: IWorkItem = {
  id: 'c2861541-9da6-47b3-97b7-a1b57297e833',
};

export const sampleWithPartialData: IWorkItem = {
  id: '7b36e5d5-2311-45ab-b9d0-9c7bd4ce1366',
  amount: 3845.42,
};

export const sampleWithFullData: IWorkItem = {
  id: '8df39fd0-cdb8-4485-af10-effb59936131',
  amount: 31515.26,
};

export const sampleWithNewData: NewWorkItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
