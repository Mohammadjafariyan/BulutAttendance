import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { AccountHesabService } from 'app/entities/BulutAttendance/account-hesab/service/account-hesab.service';
import { IAccProccParameter } from 'app/entities/BulutAttendance/acc-procc-parameter/acc-procc-parameter.model';
import { AccProccParameterService } from 'app/entities/BulutAttendance/acc-procc-parameter/service/acc-procc-parameter.service';
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';
import { AccountingProcedureService } from 'app/entities/BulutAttendance/accounting-procedure/service/accounting-procedure.service';
import { IAccProcStep } from '../acc-proc-step.model';
import { AccProcStepService } from '../service/acc-proc-step.service';
import { AccProcStepFormService } from './acc-proc-step-form.service';

import { AccProcStepUpdateComponent } from './acc-proc-step-update.component';

describe('AccProcStep Management Update Component', () => {
  let comp: AccProcStepUpdateComponent;
  let fixture: ComponentFixture<AccProcStepUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accProcStepFormService: AccProcStepFormService;
  let accProcStepService: AccProcStepService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let accountHesabService: AccountHesabService;
  let accProccParameterService: AccProccParameterService;
  let accountingProcedureService: AccountingProcedureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccProcStepUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AccProcStepUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccProcStepUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accProcStepFormService = TestBed.inject(AccProcStepFormService);
    accProcStepService = TestBed.inject(AccProcStepService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    accountHesabService = TestBed.inject(AccountHesabService);
    accProccParameterService = TestBed.inject(AccProccParameterService);
    accountingProcedureService = TestBed.inject(AccountingProcedureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const internalUser: IApplicationUser = { id: 17663 };
      accProcStep.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 11530 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const company: ICompany = { id: 10632 };
      accProcStep.company = company;

      const companyCollection: ICompany[] = [{ id: 5616 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountHesab query and add missing value', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const creditAccount: IAccountHesab = { id: '28c0bc1c-eeb7-413b-885a-37d384d1a009' };
      accProcStep.creditAccount = creditAccount;
      const debitAccount: IAccountHesab = { id: '630d866b-8f4d-40ea-8ce9-d4c7203096f9' };
      accProcStep.debitAccount = debitAccount;

      const accountHesabCollection: IAccountHesab[] = [{ id: 'b83b02b9-872c-4416-a4c2-bb52b01c9cfa' }];
      jest.spyOn(accountHesabService, 'query').mockReturnValue(of(new HttpResponse({ body: accountHesabCollection })));
      const additionalAccountHesabs = [creditAccount, debitAccount];
      const expectedCollection: IAccountHesab[] = [...additionalAccountHesabs, ...accountHesabCollection];
      jest.spyOn(accountHesabService, 'addAccountHesabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(accountHesabService.query).toHaveBeenCalled();
      expect(accountHesabService.addAccountHesabToCollectionIfMissing).toHaveBeenCalledWith(
        accountHesabCollection,
        ...additionalAccountHesabs.map(expect.objectContaining),
      );
      expect(comp.accountHesabsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccProccParameter query and add missing value', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const parameter: IAccProccParameter = { id: 32569 };
      accProcStep.parameter = parameter;

      const accProccParameterCollection: IAccProccParameter[] = [{ id: 17488 }];
      jest.spyOn(accProccParameterService, 'query').mockReturnValue(of(new HttpResponse({ body: accProccParameterCollection })));
      const additionalAccProccParameters = [parameter];
      const expectedCollection: IAccProccParameter[] = [...additionalAccProccParameters, ...accProccParameterCollection];
      jest.spyOn(accProccParameterService, 'addAccProccParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(accProccParameterService.query).toHaveBeenCalled();
      expect(accProccParameterService.addAccProccParameterToCollectionIfMissing).toHaveBeenCalledWith(
        accProccParameterCollection,
        ...additionalAccProccParameters.map(expect.objectContaining),
      );
      expect(comp.accProccParametersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountingProcedure query and add missing value', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const procedure: IAccountingProcedure = { id: 22020 };
      accProcStep.procedure = procedure;

      const accountingProcedureCollection: IAccountingProcedure[] = [{ id: 1734 }];
      jest.spyOn(accountingProcedureService, 'query').mockReturnValue(of(new HttpResponse({ body: accountingProcedureCollection })));
      const additionalAccountingProcedures = [procedure];
      const expectedCollection: IAccountingProcedure[] = [...additionalAccountingProcedures, ...accountingProcedureCollection];
      jest.spyOn(accountingProcedureService, 'addAccountingProcedureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(accountingProcedureService.query).toHaveBeenCalled();
      expect(accountingProcedureService.addAccountingProcedureToCollectionIfMissing).toHaveBeenCalledWith(
        accountingProcedureCollection,
        ...additionalAccountingProcedures.map(expect.objectContaining),
      );
      expect(comp.accountingProceduresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accProcStep: IAccProcStep = { id: 456 };
      const internalUser: IApplicationUser = { id: 22328 };
      accProcStep.internalUser = internalUser;
      const company: ICompany = { id: 6576 };
      accProcStep.company = company;
      const creditAccount: IAccountHesab = { id: '49434ed3-2a61-48ae-b5ea-d0f1ab760ac3' };
      accProcStep.creditAccount = creditAccount;
      const debitAccount: IAccountHesab = { id: 'a8a0cf68-ed74-4843-8262-d997e0d55041' };
      accProcStep.debitAccount = debitAccount;
      const parameter: IAccProccParameter = { id: 12011 };
      accProcStep.parameter = parameter;
      const procedure: IAccountingProcedure = { id: 18779 };
      accProcStep.procedure = procedure;

      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountHesabsSharedCollection).toContain(creditAccount);
      expect(comp.accountHesabsSharedCollection).toContain(debitAccount);
      expect(comp.accProccParametersSharedCollection).toContain(parameter);
      expect(comp.accountingProceduresSharedCollection).toContain(procedure);
      expect(comp.accProcStep).toEqual(accProcStep);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStep>>();
      const accProcStep = { id: 123 };
      jest.spyOn(accProcStepFormService, 'getAccProcStep').mockReturnValue(accProcStep);
      jest.spyOn(accProcStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProcStep }));
      saveSubject.complete();

      // THEN
      expect(accProcStepFormService.getAccProcStep).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accProcStepService.update).toHaveBeenCalledWith(expect.objectContaining(accProcStep));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStep>>();
      const accProcStep = { id: 123 };
      jest.spyOn(accProcStepFormService, 'getAccProcStep').mockReturnValue({ id: null });
      jest.spyOn(accProcStepService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStep: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProcStep }));
      saveSubject.complete();

      // THEN
      expect(accProcStepFormService.getAccProcStep).toHaveBeenCalled();
      expect(accProcStepService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStep>>();
      const accProcStep = { id: 123 };
      jest.spyOn(accProcStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accProcStepService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompany', () => {
      it('Should forward to companyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAccountHesab', () => {
      it('Should forward to accountHesabService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(accountHesabService, 'compareAccountHesab');
        comp.compareAccountHesab(entity, entity2);
        expect(accountHesabService.compareAccountHesab).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAccProccParameter', () => {
      it('Should forward to accProccParameterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accProccParameterService, 'compareAccProccParameter');
        comp.compareAccProccParameter(entity, entity2);
        expect(accProccParameterService.compareAccProccParameter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAccountingProcedure', () => {
      it('Should forward to accountingProcedureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accountingProcedureService, 'compareAccountingProcedure');
        comp.compareAccountingProcedure(entity, entity2);
        expect(accountingProcedureService.compareAccountingProcedure).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
