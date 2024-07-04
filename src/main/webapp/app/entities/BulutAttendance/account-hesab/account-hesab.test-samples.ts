import { IAccountHesab, NewAccountHesab } from './account-hesab.model';

export const sampleWithRequiredData: IAccountHesab = {
  id: '4fedc3b4-c587-406b-90dd-b2ba00a8ead7',
};

export const sampleWithPartialData: IAccountHesab = {
  title: 'و رسیدن برنده شدن',
  id: '512aeeef-2e14-4403-aaea-c7bbbe1f9793',
  levelInTree: 'TAFZIL_3',
  typeInFormula: 'REVENUE',
};

export const sampleWithFullData: IAccountHesab = {
  title: 'daintily هورا عادی',
  code: 'وایسا ضخیم مرده',
  level: 1941,
  levelTitle: 'پشت گربه',
  id: '0b682a5d-979b-44e3-9106-029f8d19b0cf',
  type: 'Credit',
  levelInTree: 'KOL',
  debitAmount: 5241.44,
  creditAmount: 931.62,
  typeInFormula: 'OWNERS_EQUITY',
};

export const sampleWithNewData: NewAccountHesab = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
