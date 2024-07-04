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
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';
import { AccountingProcedureService } from 'app/entities/BulutAttendance/accounting-procedure/service/accounting-procedure.service';
import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';
import { AccountingProcedureExecutionService } from '../service/accounting-procedure-execution.service';
import { AccountingProcedureExecutionFormService } from './accounting-procedure-execution-form.service';

import { AccountingProcedureExecutionUpdateComponent } from './accounting-procedure-execution-update.component';

describe('AccountingProcedureExecution Management Update Component', () => {
  let comp: AccountingProcedureExecutionUpdateComponent;
  let fixture: ComponentFixture<AccountingProcedureExecutionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountingProcedureExecutionFormService: AccountingProcedureExecutionFormService;
  let accountingProcedureExecutionService: AccountingProcedureExecutionService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let accountingProcedureService: AccountingProcedureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccountingProcedureExecutionUpdateComponent],
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
      .overrideTemplate(AccountingProcedureExecutionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountingProcedureExecutionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountingProcedureExecutionFormService = TestBed.inject(AccountingProcedureExecutionFormService);
    accountingProcedureExecutionService = TestBed.inject(AccountingProcedureExecutionService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    accountingProcedureService = TestBed.inject(AccountingProcedureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const accountingProcedureExecution: IAccountingProcedureExecution = { id: 456 };
      const internalUser: IApplicationUser = { id: 8022 };
      accountingProcedureExecution.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 9673 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accountingProcedureExecution: IAccountingProcedureExecution = { id: 456 };
      const company: ICompany = { id: 23137 };
      accountingProcedureExecution.company = company;

      const companyCollection: ICompany[] = [{ id: 25742 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountingProcedure query and add missing value', () => {
      const accountingProcedureExecution: IAccountingProcedureExecution = { id: 456 };
      const procedure: IAccountingProcedure = { id: 22681 };
      accountingProcedureExecution.procedure = procedure;

      const accountingProcedureCollection: IAccountingProcedure[] = [{ id: 1356 }];
      jest.spyOn(accountingProcedureService, 'query').mockReturnValue(of(new HttpResponse({ body: accountingProcedureCollection })));
      const additionalAccountingProcedures = [procedure];
      const expectedCollection: IAccountingProcedure[] = [...additionalAccountingProcedures, ...accountingProcedureCollection];
      jest.spyOn(accountingProcedureService, 'addAccountingProcedureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      expect(accountingProcedureService.query).toHaveBeenCalled();
      expect(accountingProcedureService.addAccountingProcedureToCollectionIfMissing).toHaveBeenCalledWith(
        accountingProcedureCollection,
        ...additionalAccountingProcedures.map(expect.objectContaining),
      );
      expect(comp.accountingProceduresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountingProcedureExecution: IAccountingProcedureExecution = { id: 456 };
      const internalUser: IApplicationUser = { id: 11456 };
      accountingProcedureExecution.internalUser = internalUser;
      const company: ICompany = { id: 1729 };
      accountingProcedureExecution.company = company;
      const procedure: IAccountingProcedure = { id: 16469 };
      accountingProcedureExecution.procedure = procedure;

      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountingProceduresSharedCollection).toContain(procedure);
      expect(comp.accountingProcedureExecution).toEqual(accountingProcedureExecution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedureExecution>>();
      const accountingProcedureExecution = { id: 123 };
      jest.spyOn(accountingProcedureExecutionFormService, 'getAccountingProcedureExecution').mockReturnValue(accountingProcedureExecution);
      jest.spyOn(accountingProcedureExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountingProcedureExecution }));
      saveSubject.complete();

      // THEN
      expect(accountingProcedureExecutionFormService.getAccountingProcedureExecution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountingProcedureExecutionService.update).toHaveBeenCalledWith(expect.objectContaining(accountingProcedureExecution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedureExecution>>();
      const accountingProcedureExecution = { id: 123 };
      jest.spyOn(accountingProcedureExecutionFormService, 'getAccountingProcedureExecution').mockReturnValue({ id: null });
      jest.spyOn(accountingProcedureExecutionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedureExecution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountingProcedureExecution }));
      saveSubject.complete();

      // THEN
      expect(accountingProcedureExecutionFormService.getAccountingProcedureExecution).toHaveBeenCalled();
      expect(accountingProcedureExecutionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedureExecution>>();
      const accountingProcedureExecution = { id: 123 };
      jest.spyOn(accountingProcedureExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedureExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountingProcedureExecutionService.update).toHaveBeenCalled();
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
