import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hr-letter.test-samples';

import { HrLetterFormService } from './hr-letter-form.service';

describe('HrLetter Form Service', () => {
  let service: HrLetterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HrLetterFormService);
  });

  describe('Service methods', () => {
    describe('createHrLetterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHrLetterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            uniqueNumber: expect.any(Object),
            id: expect.any(Object),
            issueDate: expect.any(Object),
            executionDate: expect.any(Object),
            bpmsApproveStatus: expect.any(Object),
            status: expect.any(Object),
            type: expect.any(Object),
            personnelId: expect.any(Object),
            orgPosition: expect.any(Object),
            orgUnit: expect.any(Object),
            personnelStatus: expect.any(Object),
            hrLetterParameter: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IHrLetter should create a new form with FormGroup', () => {
        const formGroup = service.createHrLetterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            uniqueNumber: expect.any(Object),
            id: expect.any(Object),
            issueDate: expect.any(Object),
            executionDate: expect.any(Object),
            bpmsApproveStatus: expect.any(Object),
            status: expect.any(Object),
            type: expect.any(Object),
            personnelId: expect.any(Object),
            orgPosition: expect.any(Object),
            orgUnit: expect.any(Object),
            personnelStatus: expect.any(Object),
            hrLetterParameter: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getHrLetter', () => {
      it('should return NewHrLetter for default HrLetter initial value', () => {
        const formGroup = service.createHrLetterFormGroup(sampleWithNewData);

        const hrLetter = service.getHrLetter(formGroup) as any;

        expect(hrLetter).toMatchObject(sampleWithNewData);
      });

      it('should return NewHrLetter for empty HrLetter initial value', () => {
        const formGroup = service.createHrLetterFormGroup();

        const hrLetter = service.getHrLetter(formGroup) as any;

        expect(hrLetter).toMatchObject({});
      });

      it('should return IHrLetter', () => {
        const formGroup = service.createHrLetterFormGroup(sampleWithRequiredData);

        const hrLetter = service.getHrLetter(formGroup) as any;

        expect(hrLetter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHrLetter should not enable id FormControl', () => {
        const formGroup = service.createHrLetterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHrLetter should disable id FormControl', () => {
        const formGroup = service.createHrLetterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
