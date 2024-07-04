import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../acc-procc-parameter.test-samples';

import { AccProccParameterFormService } from './acc-procc-parameter-form.service';

describe('AccProccParameter Form Service', () => {
  let service: AccProccParameterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccProccParameterFormService);
  });

  describe('Service methods', () => {
    describe('createAccProccParameterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccProccParameterFormGroup();

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
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IAccProccParameter should create a new form with FormGroup', () => {
        const formGroup = service.createAccProccParameterFormGroup(sampleWithRequiredData);

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
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccProccParameter', () => {
      it('should return NewAccProccParameter for default AccProccParameter initial value', () => {
        const formGroup = service.createAccProccParameterFormGroup(sampleWithNewData);

        const accProccParameter = service.getAccProccParameter(formGroup) as any;

        expect(accProccParameter).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccProccParameter for empty AccProccParameter initial value', () => {
        const formGroup = service.createAccProccParameterFormGroup();

        const accProccParameter = service.getAccProccParameter(formGroup) as any;

        expect(accProccParameter).toMatchObject({});
      });

      it('should return IAccProccParameter', () => {
        const formGroup = service.createAccProccParameterFormGroup(sampleWithRequiredData);

        const accProccParameter = service.getAccProccParameter(formGroup) as any;

        expect(accProccParameter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccProccParameter should not enable id FormControl', () => {
        const formGroup = service.createAccProccParameterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccProccParameter should disable id FormControl', () => {
        const formGroup = service.createAccProccParameterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
