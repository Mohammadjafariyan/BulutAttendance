import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../org-position.test-samples';

import { OrgPositionFormService } from './org-position-form.service';

describe('OrgPosition Form Service', () => {
  let service: OrgPositionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgPositionFormService);
  });

  describe('Service methods', () => {
    describe('createOrgPositionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrgPositionFormGroup();

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

      it('passing IOrgPosition should create a new form with FormGroup', () => {
        const formGroup = service.createOrgPositionFormGroup(sampleWithRequiredData);

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

    describe('getOrgPosition', () => {
      it('should return NewOrgPosition for default OrgPosition initial value', () => {
        const formGroup = service.createOrgPositionFormGroup(sampleWithNewData);

        const orgPosition = service.getOrgPosition(formGroup) as any;

        expect(orgPosition).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrgPosition for empty OrgPosition initial value', () => {
        const formGroup = service.createOrgPositionFormGroup();

        const orgPosition = service.getOrgPosition(formGroup) as any;

        expect(orgPosition).toMatchObject({});
      });

      it('should return IOrgPosition', () => {
        const formGroup = service.createOrgPositionFormGroup(sampleWithRequiredData);

        const orgPosition = service.getOrgPosition(formGroup) as any;

        expect(orgPosition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrgPosition should not enable id FormControl', () => {
        const formGroup = service.createOrgPositionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrgPosition should disable id FormControl', () => {
        const formGroup = service.createOrgPositionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
