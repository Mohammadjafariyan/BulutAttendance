import { IAccountingProcedure, NewAccountingProcedure } from './accounting-procedure.model';

export const sampleWithRequiredData: IAccountingProcedure = {
  id: 9386,
};

export const sampleWithPartialData: IAccountingProcedure = {
  id: 4055,
  title: 'خوب',
};

export const sampleWithFullData: IAccountingProcedure = {
  id: 18573,
  title: 'owlishly آفرین داشتن',
};

export const sampleWithNewData: NewAccountingProcedure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
