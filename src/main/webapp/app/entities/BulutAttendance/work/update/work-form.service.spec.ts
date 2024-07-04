import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../work.test-samples';

import { WorkFormService } from './work-form.service';

describe('Work Form Service', () => {
  let service: WorkFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkFormService);
  });

  describe('Service methods', () => {
    describe('createWorkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueDate: expect.any(Object),
            desc: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IWork should create a new form with FormGroup', () => {
        const formGroup = service.createWorkFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issueDate: expect.any(Object),
            desc: expect.any(Object),
            year: expect.any(Object),
            month: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getWork', () => {
      it('should return NewWork for default Work initial value', () => {
        const formGroup = service.createWorkFormGroup(sampleWithNewData);

        const work = service.getWork(formGroup) as any;

        expect(work).toMatchObject(sampleWithNewData);
      });

      it('should return NewWork for empty Work initial value', () => {
        const formGroup = service.createWorkFormGroup();

        const work = service.getWork(formGroup) as any;

        expect(work).toMatchObject({});
      });

      it('should return IWork', () => {
        const formGroup = service.createWorkFormGroup(sampleWithRequiredData);

        const work = service.getWork(formGroup) as any;

        expect(work).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWork should not enable id FormControl', () => {
        const formGroup = service.createWorkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWork should disable id FormControl', () => {
        const formGroup = service.createWorkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
