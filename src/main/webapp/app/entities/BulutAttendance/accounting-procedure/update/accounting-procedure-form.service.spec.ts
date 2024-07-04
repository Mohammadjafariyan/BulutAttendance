import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../accounting-procedure.test-samples';

import { AccountingProcedureFormService } from './accounting-procedure-form.service';

describe('AccountingProcedure Form Service', () => {
  let service: AccountingProcedureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountingProcedureFormService);
  });

  describe('Service methods', () => {
    describe('createAccountingProcedureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountingProcedureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            executeAfter: expect.any(Object),
          }),
        );
      });

      it('passing IAccountingProcedure should create a new form with FormGroup', () => {
        const formGroup = service.createAccountingProcedureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            executeAfter: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccountingProcedure', () => {
      it('should return NewAccountingProcedure for default AccountingProcedure initial value', () => {
        const formGroup = service.createAccountingProcedureFormGroup(sampleWithNewData);

        const accountingProcedure = service.getAccountingProcedure(formGroup) as any;

        expect(accountingProcedure).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountingProcedure for empty AccountingProcedure initial value', () => {
        const formGroup = service.createAccountingProcedureFormGroup();

        const accountingProcedure = service.getAccountingProcedure(formGroup) as any;

        expect(accountingProcedure).toMatchObject({});
      });

      it('should return IAccountingProcedure', () => {
        const formGroup = service.createAccountingProcedureFormGroup(sampleWithRequiredData);

        const accountingProcedure = service.getAccountingProcedure(formGroup) as any;

        expect(accountingProcedure).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountingProcedure should not enable id FormControl', () => {
        const formGroup = service.createAccountingProcedureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountingProcedure should disable id FormControl', () => {
        const formGroup = service.createAccountingProcedureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
