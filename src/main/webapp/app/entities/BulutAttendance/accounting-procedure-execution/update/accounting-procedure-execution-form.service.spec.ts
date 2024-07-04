import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../accounting-procedure-execution.test-samples';

import { AccountingProcedureExecutionFormService } from './accounting-procedure-execution-form.service';

describe('AccountingProcedureExecution Form Service', () => {
  let service: AccountingProcedureExecutionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountingProcedureExecutionFormService);
  });

  describe('Service methods', () => {
    describe('createAccountingProcedureExecutionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateTime: expect.any(Object),
            desc: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            procedure: expect.any(Object),
          }),
        );
      });

      it('passing IAccountingProcedureExecution should create a new form with FormGroup', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateTime: expect.any(Object),
            desc: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            procedure: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccountingProcedureExecution', () => {
      it('should return NewAccountingProcedureExecution for default AccountingProcedureExecution initial value', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup(sampleWithNewData);

        const accountingProcedureExecution = service.getAccountingProcedureExecution(formGroup) as any;

        expect(accountingProcedureExecution).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountingProcedureExecution for empty AccountingProcedureExecution initial value', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup();

        const accountingProcedureExecution = service.getAccountingProcedureExecution(formGroup) as any;

        expect(accountingProcedureExecution).toMatchObject({});
      });

      it('should return IAccountingProcedureExecution', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup(sampleWithRequiredData);

        const accountingProcedureExecution = service.getAccountingProcedureExecution(formGroup) as any;

        expect(accountingProcedureExecution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountingProcedureExecution should not enable id FormControl', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountingProcedureExecution should disable id FormControl', () => {
        const formGroup = service.createAccountingProcedureExecutionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
