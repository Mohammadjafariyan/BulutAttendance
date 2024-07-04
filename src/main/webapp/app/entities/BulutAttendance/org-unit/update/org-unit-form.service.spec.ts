import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../org-unit.test-samples';

import { OrgUnitFormService } from './org-unit-form.service';

describe('OrgUnit Form Service', () => {
  let service: OrgUnitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgUnitFormService);
  });

  describe('Service methods', () => {
    describe('createOrgUnitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrgUnitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IOrgUnit should create a new form with FormGroup', () => {
        const formGroup = service.createOrgUnitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            title: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrgUnit', () => {
      it('should return NewOrgUnit for default OrgUnit initial value', () => {
        const formGroup = service.createOrgUnitFormGroup(sampleWithNewData);

        const orgUnit = service.getOrgUnit(formGroup) as any;

        expect(orgUnit).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrgUnit for empty OrgUnit initial value', () => {
        const formGroup = service.createOrgUnitFormGroup();

        const orgUnit = service.getOrgUnit(formGroup) as any;

        expect(orgUnit).toMatchObject({});
      });

      it('should return IOrgUnit', () => {
        const formGroup = service.createOrgUnitFormGroup(sampleWithRequiredData);

        const orgUnit = service.getOrgUnit(formGroup) as any;

        expect(orgUnit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrgUnit should not enable id FormControl', () => {
        const formGroup = service.createOrgUnitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrgUnit should disable id FormControl', () => {
        const formGroup = service.createOrgUnitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
