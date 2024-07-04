import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../leave-summary.test-samples';

import { LeaveSummaryFormService } from './leave-summary-form.service';

describe('LeaveSummary Form Service', () => {
  let service: LeaveSummaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveSummaryFormService);
  });

  describe('Service methods', () => {
    describe('createLeaveSummaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeaveSummaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            remainHours: expect.any(Object),
            remainDays: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing ILeaveSummary should create a new form with FormGroup', () => {
        const formGroup = service.createLeaveSummaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            remainHours: expect.any(Object),
            remainDays: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getLeaveSummary', () => {
      it('should return NewLeaveSummary for default LeaveSummary initial value', () => {
        const formGroup = service.createLeaveSummaryFormGroup(sampleWithNewData);

        const leaveSummary = service.getLeaveSummary(formGroup) as any;

        expect(leaveSummary).toMatchObject(sampleWithNewData);
      });

      it('should return NewLeaveSummary for empty LeaveSummary initial value', () => {
        const formGroup = service.createLeaveSummaryFormGroup();

        const leaveSummary = service.getLeaveSummary(formGroup) as any;

        expect(leaveSummary).toMatchObject({});
      });

      it('should return ILeaveSummary', () => {
        const formGroup = service.createLeaveSummaryFormGroup(sampleWithRequiredData);

        const leaveSummary = service.getLeaveSummary(formGroup) as any;

        expect(leaveSummary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILeaveSummary should not enable id FormControl', () => {
        const formGroup = service.createLeaveSummaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLeaveSummary should disable id FormControl', () => {
        const formGroup = service.createLeaveSummaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
