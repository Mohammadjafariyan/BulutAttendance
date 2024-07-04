import { IAccProcStep, NewAccProcStep } from './acc-proc-step.model';

export const sampleWithRequiredData: IAccProcStep = {
  id: 31644,
};

export const sampleWithPartialData: IAccProcStep = {
  id: 29191,
};

export const sampleWithFullData: IAccProcStep = {
  id: 304,
};

export const sampleWithNewData: NewAccProcStep = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
