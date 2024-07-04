import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tax-template.test-samples';

import { TaxTemplateFormService } from './tax-template-form.service';

describe('TaxTemplate Form Service', () => {
  let service: TaxTemplateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaxTemplateFormService);
  });

  describe('Service methods', () => {
    describe('createTaxTemplateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxTemplateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rangeFrom: expect.any(Object),
            rangeTo: expect.any(Object),
            percent: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing ITaxTemplate should create a new form with FormGroup', () => {
        const formGroup = service.createTaxTemplateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            rangeFrom: expect.any(Object),
            rangeTo: expect.any(Object),
            percent: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getTaxTemplate', () => {
      it('should return NewTaxTemplate for default TaxTemplate initial value', () => {
        const formGroup = service.createTaxTemplateFormGroup(sampleWithNewData);

        const taxTemplate = service.getTaxTemplate(formGroup) as any;

        expect(taxTemplate).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxTemplate for empty TaxTemplate initial value', () => {
        const formGroup = service.createTaxTemplateFormGroup();

        const taxTemplate = service.getTaxTemplate(formGroup) as any;

        expect(taxTemplate).toMatchObject({});
      });

      it('should return ITaxTemplate', () => {
        const formGroup = service.createTaxTemplateFormGroup(sampleWithRequiredData);

        const taxTemplate = service.getTaxTemplate(formGroup) as any;

        expect(taxTemplate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxTemplate should not enable id FormControl', () => {
        const formGroup = service.createTaxTemplateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxTemplate should disable id FormControl', () => {
        const formGroup = service.createTaxTemplateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
