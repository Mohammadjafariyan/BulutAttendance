import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hr-letter-parameter.test-samples';

import { HrLetterParameterFormService } from './hr-letter-parameter-form.service';

describe('HrLetterParameter Form Service', () => {
  let service: HrLetterParameterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HrLetterParameterFormService);
  });

  describe('Service methods', () => {
    describe('createHrLetterParameterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHrLetterParameterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            manualOrAuto: expect.any(Object),
            formula: expect.any(Object),
            unit: expect.any(Object),
            isDeducTax: expect.any(Object),
            isDeducInsurance: expect.any(Object),
            laborTime: expect.any(Object),
            hokm: expect.any(Object),
            earnings: expect.any(Object),
            deduction: expect.any(Object),
            other: expect.any(Object),
            isEnabled: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IHrLetterParameter should create a new form with FormGroup', () => {
        const formGroup = service.createHrLetterParameterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            manualOrAuto: expect.any(Object),
            formula: expect.any(Object),
            unit: expect.any(Object),
            isDeducTax: expect.any(Object),
            isDeducInsurance: expect.any(Object),
            laborTime: expect.any(Object),
            hokm: expect.any(Object),
            earnings: expect.any(Object),
            deduction: expect.any(Object),
            other: expect.any(Object),
            isEnabled: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getHrLetterParameter', () => {
      it('should return NewHrLetterParameter for default HrLetterParameter initial value', () => {
        const formGroup = service.createHrLetterParameterFormGroup(sampleWithNewData);

        const hrLetterParameter = service.getHrLetterParameter(formGroup) as any;

        expect(hrLetterParameter).toMatchObject(sampleWithNewData);
      });

      it('should return NewHrLetterParameter for empty HrLetterParameter initial value', () => {
        const formGroup = service.createHrLetterParameterFormGroup();

        const hrLetterParameter = service.getHrLetterParameter(formGroup) as any;

        expect(hrLetterParameter).toMatchObject({});
      });

      it('should return IHrLetterParameter', () => {
        const formGroup = service.createHrLetterParameterFormGroup(sampleWithRequiredData);

        const hrLetterParameter = service.getHrLetterParameter(formGroup) as any;

        expect(hrLetterParameter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHrLetterParameter should not enable id FormControl', () => {
        const formGroup = service.createHrLetterParameterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHrLetterParameter should disable id FormControl', () => {
        const formGroup = service.createHrLetterParameterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
