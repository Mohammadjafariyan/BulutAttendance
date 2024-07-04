import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parameter.test-samples';

import { ParameterFormService } from './parameter-form.service';

describe('Parameter Form Service', () => {
  let service: ParameterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParameterFormService);
  });

  describe('Service methods', () => {
    describe('createParameterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParameterFormGroup();

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
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IParameter should create a new form with FormGroup', () => {
        const formGroup = service.createParameterFormGroup(sampleWithRequiredData);

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
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getParameter', () => {
      it('should return NewParameter for default Parameter initial value', () => {
        const formGroup = service.createParameterFormGroup(sampleWithNewData);

        const parameter = service.getParameter(formGroup) as any;

        expect(parameter).toMatchObject(sampleWithNewData);
      });

      it('should return NewParameter for empty Parameter initial value', () => {
        const formGroup = service.createParameterFormGroup();

        const parameter = service.getParameter(formGroup) as any;

        expect(parameter).toMatchObject({});
      });

      it('should return IParameter', () => {
        const formGroup = service.createParameterFormGroup(sampleWithRequiredData);

        const parameter = service.getParameter(formGroup) as any;

        expect(parameter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParameter should not enable id FormControl', () => {
        const formGroup = service.createParameterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParameter should disable id FormControl', () => {
        const formGroup = service.createParameterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
