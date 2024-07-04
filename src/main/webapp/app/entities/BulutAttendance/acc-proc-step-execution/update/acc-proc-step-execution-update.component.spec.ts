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
import { IAccountingProcedureExecution } from 'app/entities/BulutAttendance/accounting-procedure-execution/accounting-procedure-execution.model';
import { AccountingProcedureExecutionService } from 'app/entities/BulutAttendance/accounting-procedure-execution/service/accounting-procedure-execution.service';
import { IAccProcStep } from 'app/entities/BulutAttendance/acc-proc-step/acc-proc-step.model';
import { AccProcStepService } from 'app/entities/BulutAttendance/acc-proc-step/service/acc-proc-step.service';
import { IAccProcStepExecution } from '../acc-proc-step-execution.model';
import { AccProcStepExecutionService } from '../service/acc-proc-step-execution.service';
import { AccProcStepExecutionFormService } from './acc-proc-step-execution-form.service';

import { AccProcStepExecutionUpdateComponent } from './acc-proc-step-execution-update.component';

describe('AccProcStepExecution Management Update Component', () => {
  let comp: AccProcStepExecutionUpdateComponent;
  let fixture: ComponentFixture<AccProcStepExecutionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accProcStepExecutionFormService: AccProcStepExecutionFormService;
  let accProcStepExecutionService: AccProcStepExecutionService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let accountHesabService: AccountHesabService;
  let accountingProcedureExecutionService: AccountingProcedureExecutionService;
  let accProcStepService: AccProcStepService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccProcStepExecutionUpdateComponent],
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
      .overrideTemplate(AccProcStepExecutionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccProcStepExecutionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accProcStepExecutionFormService = TestBed.inject(AccProcStepExecutionFormService);
    accProcStepExecutionService = TestBed.inject(AccProcStepExecutionService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    accountHesabService = TestBed.inject(AccountHesabService);
    accountingProcedureExecutionService = TestBed.inject(AccountingProcedureExecutionService);
    accProcStepService = TestBed.inject(AccProcStepService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const internalUser: IApplicationUser = { id: 650 };
      accProcStepExecution.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 25188 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const company: ICompany = { id: 24834 };
      accProcStepExecution.company = company;

      const companyCollection: ICompany[] = [{ id: 897 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountHesab query and add missing value', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const creditAccount: IAccountHesab = { id: '7e70628f-0f56-4852-8d69-e0669c8e5854' };
      accProcStepExecution.creditAccount = creditAccount;
      const debitAccount: IAccountHesab = { id: '6d2da6a3-14cc-476c-a549-6896604f369c' };
      accProcStepExecution.debitAccount = debitAccount;

      const accountHesabCollection: IAccountHesab[] = [{ id: '31d53239-0434-4146-8d41-c21d7667f848' }];
      jest.spyOn(accountHesabService, 'query').mockReturnValue(of(new HttpResponse({ body: accountHesabCollection })));
      const additionalAccountHesabs = [creditAccount, debitAccount];
      const expectedCollection: IAccountHesab[] = [...additionalAccountHesabs, ...accountHesabCollection];
      jest.spyOn(accountHesabService, 'addAccountHesabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(accountHesabService.query).toHaveBeenCalled();
      expect(accountHesabService.addAccountHesabToCollectionIfMissing).toHaveBeenCalledWith(
        accountHesabCollection,
        ...additionalAccountHesabs.map(expect.objectContaining),
      );
      expect(comp.accountHesabsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountingProcedureExecution query and add missing value', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const procedure: IAccountingProcedureExecution = { id: 12526 };
      accProcStepExecution.procedure = procedure;

      const accountingProcedureExecutionCollection: IAccountingProcedureExecution[] = [{ id: 25820 }];
      jest
        .spyOn(accountingProcedureExecutionService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: accountingProcedureExecutionCollection })));
      const additionalAccountingProcedureExecutions = [procedure];
      const expectedCollection: IAccountingProcedureExecution[] = [
        ...additionalAccountingProcedureExecutions,
        ...accountingProcedureExecutionCollection,
      ];
      jest
        .spyOn(accountingProcedureExecutionService, 'addAccountingProcedureExecutionToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(accountingProcedureExecutionService.query).toHaveBeenCalled();
      expect(accountingProcedureExecutionService.addAccountingProcedureExecutionToCollectionIfMissing).toHaveBeenCalledWith(
        accountingProcedureExecutionCollection,
        ...additionalAccountingProcedureExecutions.map(expect.objectContaining),
      );
      expect(comp.accountingProcedureExecutionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccProcStep query and add missing value', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const step: IAccProcStep = { id: 31045 };
      accProcStepExecution.step = step;

      const accProcStepCollection: IAccProcStep[] = [{ id: 10175 }];
      jest.spyOn(accProcStepService, 'query').mockReturnValue(of(new HttpResponse({ body: accProcStepCollection })));
      const additionalAccProcSteps = [step];
      const expectedCollection: IAccProcStep[] = [...additionalAccProcSteps, ...accProcStepCollection];
      jest.spyOn(accProcStepService, 'addAccProcStepToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(accProcStepService.query).toHaveBeenCalled();
      expect(accProcStepService.addAccProcStepToCollectionIfMissing).toHaveBeenCalledWith(
        accProcStepCollection,
        ...additionalAccProcSteps.map(expect.objectContaining),
      );
      expect(comp.accProcStepsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accProcStepExecution: IAccProcStepExecution = { id: 456 };
      const internalUser: IApplicationUser = { id: 10495 };
      accProcStepExecution.internalUser = internalUser;
      const company: ICompany = { id: 13980 };
      accProcStepExecution.company = company;
      const creditAccount: IAccountHesab = { id: 'b445fc91-3eae-47e7-b383-3cd7d0aa94cf' };
      accProcStepExecution.creditAccount = creditAccount;
      const debitAccount: IAccountHesab = { id: '53a7126d-3d81-49b4-8a4d-d6c2370df626' };
      accProcStepExecution.debitAccount = debitAccount;
      const procedure: IAccountingProcedureExecution = { id: 15762 };
      accProcStepExecution.procedure = procedure;
      const step: IAccProcStep = { id: 7888 };
      accProcStepExecution.step = step;

      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountHesabsSharedCollection).toContain(creditAccount);
      expect(comp.accountHesabsSharedCollection).toContain(debitAccount);
      expect(comp.accountingProcedureExecutionsSharedCollection).toContain(procedure);
      expect(comp.accProcStepsSharedCollection).toContain(step);
      expect(comp.accProcStepExecution).toEqual(accProcStepExecution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStepExecution>>();
      const accProcStepExecution = { id: 123 };
      jest.spyOn(accProcStepExecutionFormService, 'getAccProcStepExecution').mockReturnValue(accProcStepExecution);
      jest.spyOn(accProcStepExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProcStepExecution }));
      saveSubject.complete();

      // THEN
      expect(accProcStepExecutionFormService.getAccProcStepExecution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accProcStepExecutionService.update).toHaveBeenCalledWith(expect.objectContaining(accProcStepExecution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStepExecution>>();
      const accProcStepExecution = { id: 123 };
      jest.spyOn(accProcStepExecutionFormService, 'getAccProcStepExecution').mockReturnValue({ id: null });
      jest.spyOn(accProcStepExecutionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStepExecution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProcStepExecution }));
      saveSubject.complete();

      // THEN
      expect(accProcStepExecutionFormService.getAccProcStepExecution).toHaveBeenCalled();
      expect(accProcStepExecutionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProcStepExecution>>();
      const accProcStepExecution = { id: 123 };
      jest.spyOn(accProcStepExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProcStepExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accProcStepExecutionService.update).toHaveBeenCalled();
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

    describe('compareAccountingProcedureExecution', () => {
      it('Should forward to accountingProcedureExecutionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accountingProcedureExecutionService, 'compareAccountingProcedureExecution');
        comp.compareAccountingProcedureExecution(entity, entity2);
        expect(accountingProcedureExecutionService.compareAccountingProcedureExecution).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAccProcStep', () => {
      it('Should forward to accProcStepService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accProcStepService, 'compareAccProcStep');
        comp.compareAccProcStep(entity, entity2);
        expect(accProcStepService.compareAccProcStep).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
