import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../acc-proc-step.test-samples';

import { AccProcStepFormService } from './acc-proc-step-form.service';

describe('AccProcStep Form Service', () => {
  let service: AccProcStepFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccProcStepFormService);
  });

  describe('Service methods', () => {
    describe('createAccProcStepFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccProcStepFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            creditAccount: expect.any(Object),
            debitAccount: expect.any(Object),
            parameter: expect.any(Object),
            procedure: expect.any(Object),
          }),
        );
      });

      it('passing IAccProcStep should create a new form with FormGroup', () => {
        const formGroup = service.createAccProcStepFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            internalUser: expect.any(Object),
            company: expect.any(Object),
            creditAccount: expect.any(Object),
            debitAccount: expect.any(Object),
            parameter: expect.any(Object),
            procedure: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccProcStep', () => {
      it('should return NewAccProcStep for default AccProcStep initial value', () => {
        const formGroup = service.createAccProcStepFormGroup(sampleWithNewData);

        const accProcStep = service.getAccProcStep(formGroup) as any;

        expect(accProcStep).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccProcStep for empty AccProcStep initial value', () => {
        const formGroup = service.createAccProcStepFormGroup();

        const accProcStep = service.getAccProcStep(formGroup) as any;

        expect(accProcStep).toMatchObject({});
      });

      it('should return IAccProcStep', () => {
        const formGroup = service.createAccProcStepFormGroup(sampleWithRequiredData);

        const accProcStep = service.getAccProcStep(formGroup) as any;

        expect(accProcStep).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccProcStep should not enable id FormControl', () => {
        const formGroup = service.createAccProcStepFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccProcStep should disable id FormControl', () => {
        const formGroup = service.createAccProcStepFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
