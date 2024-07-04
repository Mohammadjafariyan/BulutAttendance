import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../work-item.test-samples';

import { WorkItemFormService } from './work-item-form.service';

describe('WorkItem Form Service', () => {
  let service: WorkItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkItemFormService);
  });

  describe('Service methods', () => {
    describe('createWorkItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            hrLetterParameter: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            work: expect.any(Object),
          }),
        );
      });

      it('passing IWorkItem should create a new form with FormGroup', () => {
        const formGroup = service.createWorkItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            amount: expect.any(Object),
            hrLetterParameter: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            work: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkItem', () => {
      it('should return NewWorkItem for default WorkItem initial value', () => {
        const formGroup = service.createWorkItemFormGroup(sampleWithNewData);

        const workItem = service.getWorkItem(formGroup) as any;

        expect(workItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkItem for empty WorkItem initial value', () => {
        const formGroup = service.createWorkItemFormGroup();

        const workItem = service.getWorkItem(formGroup) as any;

        expect(workItem).toMatchObject({});
      });

      it('should return IWorkItem', () => {
        const formGroup = service.createWorkItemFormGroup(sampleWithRequiredData);

        const workItem = service.getWorkItem(formGroup) as any;

        expect(workItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkItem should not enable id FormControl', () => {
        const formGroup = service.createWorkItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkItem should disable id FormControl', () => {
        const formGroup = service.createWorkItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
