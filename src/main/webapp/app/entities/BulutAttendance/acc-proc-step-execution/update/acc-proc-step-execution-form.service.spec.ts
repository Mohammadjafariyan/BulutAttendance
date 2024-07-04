import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../acc-proc-step-execution.test-samples';

import { AccProcStepExecutionFormService } from './acc-proc-step-execution-form.service';

describe('AccProcStepExecution Form Service', () => {
  let service: AccProcStepExecutionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccProcStepExecutionFormService);
  });

  describe('Service methods', () => {
    describe('createAccProcStepExecutionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            desc: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            creditAccount: expect.any(Object),
            debitAccount: expect.any(Object),
            procedure: expect.any(Object),
            step: expect.any(Object),
          }),
        );
      });

      it('passing IAccProcStepExecution should create a new form with FormGroup', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            desc: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            creditAccount: expect.any(Object),
            debitAccount: expect.any(Object),
            procedure: expect.any(Object),
            step: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccProcStepExecution', () => {
      it('should return NewAccProcStepExecution for default AccProcStepExecution initial value', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup(sampleWithNewData);

        const accProcStepExecution = service.getAccProcStepExecution(formGroup) as any;

        expect(accProcStepExecution).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccProcStepExecution for empty AccProcStepExecution initial value', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup();

        const accProcStepExecution = service.getAccProcStepExecution(formGroup) as any;

        expect(accProcStepExecution).toMatchObject({});
      });

      it('should return IAccProcStepExecution', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup(sampleWithRequiredData);

        const accProcStepExecution = service.getAccProcStepExecution(formGroup) as any;

        expect(accProcStepExecution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccProcStepExecution should not enable id FormControl', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccProcStepExecution should disable id FormControl', () => {
        const formGroup = service.createAccProcStepExecutionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
