import { ILeaveSummary, NewLeaveSummary } from './leave-summary.model';

export const sampleWithRequiredData: ILeaveSummary = {
  id: '185775ff-57ff-46c7-9b76-2b5e4055e7c3',
};

export const sampleWithPartialData: ILeaveSummary = {
  remainHours: 24035,
  remainDays: 17379,
  year: 9831,
  id: '9ddbee36-9875-4aa3-9c5d-67e634222ece',
};

export const sampleWithFullData: ILeaveSummary = {
  remainHours: 8655,
  remainDays: 14574,
  year: 1531,
  id: 'ebb4fa4f-7b1e-464d-8db3-e8f55697bd96',
};

export const sampleWithNewData: NewLeaveSummary = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
