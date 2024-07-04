import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../leave.test-samples';

import { LeaveFormService } from './leave-form.service';

describe('Leave Form Service', () => {
  let service: LeaveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveFormService);
  });

  describe('Service methods', () => {
    describe('createLeaveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeaveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            start: expect.any(Object),
            end: expect.any(Object),
            id: expect.any(Object),
            bpmsApproveStatus: expect.any(Object),
            status: expect.any(Object),
            personnelId: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing ILeave should create a new form with FormGroup', () => {
        const formGroup = service.createLeaveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            start: expect.any(Object),
            end: expect.any(Object),
            id: expect.any(Object),
            bpmsApproveStatus: expect.any(Object),
            status: expect.any(Object),
            personnelId: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getLeave', () => {
      it('should return NewLeave for default Leave initial value', () => {
        const formGroup = service.createLeaveFormGroup(sampleWithNewData);

        const leave = service.getLeave(formGroup) as any;

        expect(leave).toMatchObject(sampleWithNewData);
      });

      it('should return NewLeave for empty Leave initial value', () => {
        const formGroup = service.createLeaveFormGroup();

        const leave = service.getLeave(formGroup) as any;

        expect(leave).toMatchObject({});
      });

      it('should return ILeave', () => {
        const formGroup = service.createLeaveFormGroup(sampleWithRequiredData);

        const leave = service.getLeave(formGroup) as any;

        expect(leave).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILeave should not enable id FormControl', () => {
        const formGroup = service.createLeaveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLeave should disable id FormControl', () => {
        const formGroup = service.createLeaveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
