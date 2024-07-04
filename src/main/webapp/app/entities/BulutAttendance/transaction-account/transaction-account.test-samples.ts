import { ITransactionAccount, NewTransactionAccount } from './transaction-account.model';

export const sampleWithRequiredData: ITransactionAccount = {
  id: 17614,
};

export const sampleWithPartialData: ITransactionAccount = {
  id: 17252,
  debitAmount: 9385.64,
};

export const sampleWithFullData: ITransactionAccount = {
  id: 14370,
  debitAmount: 2883.51,
  creditAmount: 27462.22,
};

export const sampleWithNewData: NewTransactionAccount = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
