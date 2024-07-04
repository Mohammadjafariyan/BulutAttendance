import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../account-hesab.test-samples';

import { AccountHesabFormService } from './account-hesab-form.service';

describe('AccountHesab Form Service', () => {
  let service: AccountHesabFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountHesabFormService);
  });

  describe('Service methods', () => {
    describe('createAccountHesabFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountHesabFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            code: expect.any(Object),
            level: expect.any(Object),
            levelTitle: expect.any(Object),
            id: expect.any(Object),
            type: expect.any(Object),
            levelInTree: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            typeInFormula: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            parentAccountId: expect.any(Object),
            personnelId: expect.any(Object),
            bank: expect.any(Object),
          }),
        );
      });

      it('passing IAccountHesab should create a new form with FormGroup', () => {
        const formGroup = service.createAccountHesabFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            code: expect.any(Object),
            level: expect.any(Object),
            levelTitle: expect.any(Object),
            id: expect.any(Object),
            type: expect.any(Object),
            levelInTree: expect.any(Object),
            debitAmount: expect.any(Object),
            creditAmount: expect.any(Object),
            typeInFormula: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            parentAccountId: expect.any(Object),
            personnelId: expect.any(Object),
            bank: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccountHesab', () => {
      it('should return NewAccountHesab for default AccountHesab initial value', () => {
        const formGroup = service.createAccountHesabFormGroup(sampleWithNewData);

        const accountHesab = service.getAccountHesab(formGroup) as any;

        expect(accountHesab).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountHesab for empty AccountHesab initial value', () => {
        const formGroup = service.createAccountHesabFormGroup();

        const accountHesab = service.getAccountHesab(formGroup) as any;

        expect(accountHesab).toMatchObject({});
      });

      it('should return IAccountHesab', () => {
        const formGroup = service.createAccountHesabFormGroup(sampleWithRequiredData);

        const accountHesab = service.getAccountHesab(formGroup) as any;

        expect(accountHesab).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountHesab should not enable id FormControl', () => {
        const formGroup = service.createAccountHesabFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountHesab should disable id FormControl', () => {
        const formGroup = service.createAccountHesabFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
