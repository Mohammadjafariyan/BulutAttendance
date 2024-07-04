import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hr-letter-type.test-samples';

import { HrLetterTypeFormService } from './hr-letter-type-form.service';

describe('HrLetterType Form Service', () => {
  let service: HrLetterTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HrLetterTypeFormService);
  });

  describe('Service methods', () => {
    describe('createHrLetterTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHrLetterTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IHrLetterType should create a new form with FormGroup', () => {
        const formGroup = service.createHrLetterTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getHrLetterType', () => {
      it('should return NewHrLetterType for default HrLetterType initial value', () => {
        const formGroup = service.createHrLetterTypeFormGroup(sampleWithNewData);

        const hrLetterType = service.getHrLetterType(formGroup) as any;

        expect(hrLetterType).toMatchObject(sampleWithNewData);
      });

      it('should return NewHrLetterType for empty HrLetterType initial value', () => {
        const formGroup = service.createHrLetterTypeFormGroup();

        const hrLetterType = service.getHrLetterType(formGroup) as any;

        expect(hrLetterType).toMatchObject({});
      });

      it('should return IHrLetterType', () => {
        const formGroup = service.createHrLetterTypeFormGroup(sampleWithRequiredData);

        const hrLetterType = service.getHrLetterType(formGroup) as any;

        expect(hrLetterType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHrLetterType should not enable id FormControl', () => {
        const formGroup = service.createHrLetterTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHrLetterType should disable id FormControl', () => {
        const formGroup = service.createHrLetterTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
