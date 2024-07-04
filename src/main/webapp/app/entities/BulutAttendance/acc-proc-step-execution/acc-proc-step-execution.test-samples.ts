import { IAccProcStepExecution, NewAccProcStepExecution } from './acc-proc-step-execution.model';

export const sampleWithRequiredData: IAccProcStepExecution = {
  id: 22628,
};

export const sampleWithPartialData: IAccProcStepExecution = {
  id: 6743,
  creditAmount: 19718.67,
  desc: 'مادر مدیر پرنده',
};

export const sampleWithFullData: IAccProcStepExecution = {
  id: 9814,
  debitAmount: 5549.99,
  creditAmount: 5214.18,
  desc: 'در پزشک majestically',
};

export const sampleWithNewData: NewAccProcStepExecution = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
