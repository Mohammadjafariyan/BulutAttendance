import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personnel-status.test-samples';

import { PersonnelStatusFormService } from './personnel-status-form.service';

describe('PersonnelStatus Form Service', () => {
  let service: PersonnelStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonnelStatusFormService);
  });

  describe('Service methods', () => {
    describe('createPersonnelStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonnelStatusFormGroup();

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

      it('passing IPersonnelStatus should create a new form with FormGroup', () => {
        const formGroup = service.createPersonnelStatusFormGroup(sampleWithRequiredData);

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

    describe('getPersonnelStatus', () => {
      it('should return NewPersonnelStatus for default PersonnelStatus initial value', () => {
        const formGroup = service.createPersonnelStatusFormGroup(sampleWithNewData);

        const personnelStatus = service.getPersonnelStatus(formGroup) as any;

        expect(personnelStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonnelStatus for empty PersonnelStatus initial value', () => {
        const formGroup = service.createPersonnelStatusFormGroup();

        const personnelStatus = service.getPersonnelStatus(formGroup) as any;

        expect(personnelStatus).toMatchObject({});
      });

      it('should return IPersonnelStatus', () => {
        const formGroup = service.createPersonnelStatusFormGroup(sampleWithRequiredData);

        const personnelStatus = service.getPersonnelStatus(formGroup) as any;

        expect(personnelStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonnelStatus should not enable id FormControl', () => {
        const formGroup = service.createPersonnelStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonnelStatus should disable id FormControl', () => {
        const formGroup = service.createPersonnelStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
