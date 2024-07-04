import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../account-template.test-samples';

import { AccountTemplateFormService } from './account-template-form.service';

describe('AccountTemplate Form Service', () => {
  let service: AccountTemplateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountTemplateFormService);
  });

  describe('Service methods', () => {
    describe('createAccountTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountTemplateFormGroup();

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
          }),
        );
      });

      it('passing IAccountTemplate should create a new form with FormGroup', () => {
        const formGroup = service.createAccountTemplateFormGroup(sampleWithRequiredData);

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
          }),
        );
      });
    });

    describe('getAccountTemplate', () => {
      it('should return NewAccountTemplate for default AccountTemplate initial value', () => {
        const formGroup = service.createAccountTemplateFormGroup(sampleWithNewData);

        const accountTemplate = service.getAccountTemplate(formGroup) as any;

        expect(accountTemplate).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountTemplate for empty AccountTemplate initial value', () => {
        const formGroup = service.createAccountTemplateFormGroup();

        const accountTemplate = service.getAccountTemplate(formGroup) as any;

        expect(accountTemplate).toMatchObject({});
      });

      it('should return IAccountTemplate', () => {
        const formGroup = service.createAccountTemplateFormGroup(sampleWithRequiredData);

        const accountTemplate = service.getAccountTemplate(formGroup) as any;

        expect(accountTemplate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountTemplate should not enable id FormControl', () => {
        const formGroup = service.createAccountTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountTemplate should disable id FormControl', () => {
        const formGroup = service.createAccountTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
