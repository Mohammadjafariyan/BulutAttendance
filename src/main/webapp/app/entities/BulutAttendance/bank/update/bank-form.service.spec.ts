import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bank.test-samples';

import { BankFormService } from './bank-form.service';

describe('Bank Form Service', () => {
  let service: BankFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BankFormService);
  });

  describe('Service methods', () => {
    describe('createBankFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBankFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            code: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IBank should create a new form with FormGroup', () => {
        const formGroup = service.createBankFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            code: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getBank', () => {
      it('should return NewBank for default Bank initial value', () => {
        const formGroup = service.createBankFormGroup(sampleWithNewData);

        const bank = service.getBank(formGroup) as any;

        expect(bank).toMatchObject(sampleWithNewData);
      });

      it('should return NewBank for empty Bank initial value', () => {
        const formGroup = service.createBankFormGroup();

        const bank = service.getBank(formGroup) as any;

        expect(bank).toMatchObject({});
      });

      it('should return IBank', () => {
        const formGroup = service.createBankFormGroup(sampleWithRequiredData);

        const bank = service.getBank(formGroup) as any;

        expect(bank).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBank should not enable id FormControl', () => {
        const formGroup = service.createBankFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBank should disable id FormControl', () => {
        const formGroup = service.createBankFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
