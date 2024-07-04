import { IBank, NewBank } from './bank.model';

export const sampleWithRequiredData: IBank = {
  id: '1b343280-ad1a-4ce3-aa35-2cf1456d2638',
};

export const sampleWithPartialData: IBank = {
  code: 'لیکن خسته‌کننده cheerfully',
  id: 'd563c964-37e6-4e92-ba09-81311d547048',
};

export const sampleWithFullData: IBank = {
  title: 'unnaturally',
  code: 'تا دوست',
  id: '260685c5-0773-443c-bd4c-47699876852c',
};

export const sampleWithNewData: NewBank = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
