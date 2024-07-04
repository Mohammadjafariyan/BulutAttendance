import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../record-status.test-samples';

import { RecordStatusFormService } from './record-status-form.service';

describe('RecordStatus Form Service', () => {
  let service: RecordStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecordStatusFormService);
  });

  describe('Service methods', () => {
    describe('createRecordStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecordStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            fromDateTime: expect.any(Object),
            toDateTime: expect.any(Object),
            isDeleted: expect.any(Object),
            id: expect.any(Object),
          }),
        );
      });

      it('passing IRecordStatus should create a new form with FormGroup', () => {
        const formGroup = service.createRecordStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            fromDateTime: expect.any(Object),
            toDateTime: expect.any(Object),
            isDeleted: expect.any(Object),
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecordStatus', () => {
      it('should return NewRecordStatus for default RecordStatus initial value', () => {
        const formGroup = service.createRecordStatusFormGroup(sampleWithNewData);

        const recordStatus = service.getRecordStatus(formGroup) as any;

        expect(recordStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecordStatus for empty RecordStatus initial value', () => {
        const formGroup = service.createRecordStatusFormGroup();

        const recordStatus = service.getRecordStatus(formGroup) as any;

        expect(recordStatus).toMatchObject({});
      });

      it('should return IRecordStatus', () => {
        const formGroup = service.createRecordStatusFormGroup(sampleWithRequiredData);

        const recordStatus = service.getRecordStatus(formGroup) as any;

        expect(recordStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecordStatus should not enable id FormControl', () => {
        const formGroup = service.createRecordStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecordStatus should disable id FormControl', () => {
        const formGroup = service.createRecordStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
