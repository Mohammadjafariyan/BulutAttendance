import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sys-config.test-samples';

import { SysConfigFormService } from './sys-config-form.service';

describe('SysConfig Form Service', () => {
  let service: SysConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SysConfigFormService);
  });

  describe('Service methods', () => {
    describe('createSysConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSysConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            taxFormula: expect.any(Object),
            sanavatFormula: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });

      it('passing ISysConfig should create a new form with FormGroup', () => {
        const formGroup = service.createSysConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            taxFormula: expect.any(Object),
            sanavatFormula: expect.any(Object),
            year: expect.any(Object),
            id: expect.any(Object),
            status: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
          }),
        );
      });
    });

    describe('getSysConfig', () => {
      it('should return NewSysConfig for default SysConfig initial value', () => {
        const formGroup = service.createSysConfigFormGroup(sampleWithNewData);

        const sysConfig = service.getSysConfig(formGroup) as any;

        expect(sysConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewSysConfig for empty SysConfig initial value', () => {
        const formGroup = service.createSysConfigFormGroup();

        const sysConfig = service.getSysConfig(formGroup) as any;

        expect(sysConfig).toMatchObject({});
      });

      it('should return ISysConfig', () => {
        const formGroup = service.createSysConfigFormGroup(sampleWithRequiredData);

        const sysConfig = service.getSysConfig(formGroup) as any;

        expect(sysConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISysConfig should not enable id FormControl', () => {
        const formGroup = service.createSysConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSysConfig should disable id FormControl', () => {
        const formGroup = service.createSysConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
