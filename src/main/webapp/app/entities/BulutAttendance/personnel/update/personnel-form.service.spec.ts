import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personnel.test-samples';

import { PersonnelFormService } from './personnel-form.service';

describe('Personnel Form Service', () => {
  let service: PersonnelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonnelFormService);
  });

  describe('Service methods', () => {
    describe('createPersonnelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonnelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            requitmentDate: expect.any(Object),
            father: expect.any(Object),
            shenasname: expect.any(Object),
            mahalesodur: expect.any(Object),
            birthday: expect.any(Object),
            isSingle: expect.any(Object),
            lastEducation: expect.any(Object),
            educationField: expect.any(Object),
            children: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing IPersonnel should create a new form with FormGroup', () => {
        const formGroup = service.createPersonnelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            requitmentDate: expect.any(Object),
            father: expect.any(Object),
            shenasname: expect.any(Object),
            mahalesodur: expect.any(Object),
            birthday: expect.any(Object),
            isSingle: expect.any(Object),
            lastEducation: expect.any(Object),
            educationField: expect.any(Object),
            children: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getPersonnel', () => {
      it('should return NewPersonnel for default Personnel initial value', () => {
        const formGroup = service.createPersonnelFormGroup(sampleWithNewData);

        const personnel = service.getPersonnel(formGroup) as any;

        expect(personnel).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonnel for empty Personnel initial value', () => {
        const formGroup = service.createPersonnelFormGroup();

        const personnel = service.getPersonnel(formGroup) as any;

        expect(personnel).toMatchObject({});
      });

      it('should return IPersonnel', () => {
        const formGroup = service.createPersonnelFormGroup(sampleWithRequiredData);

        const personnel = service.getPersonnel(formGroup) as any;

        expect(personnel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonnel should not enable id FormControl', () => {
        const formGroup = service.createPersonnelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonnel should disable id FormControl', () => {
        const formGroup = service.createPersonnelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
