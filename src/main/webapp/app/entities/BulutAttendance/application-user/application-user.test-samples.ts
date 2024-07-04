import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 29338,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 10543,
  userId: 'در',
};

export const sampleWithFullData: IApplicationUser = {
  id: 19188,
  userId: 'پایین آهان در برابر',
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
