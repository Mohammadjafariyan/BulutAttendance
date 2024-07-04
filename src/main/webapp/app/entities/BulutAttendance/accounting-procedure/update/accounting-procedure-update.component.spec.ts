import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IAccountingProcedure } from '../accounting-procedure.model';
import { AccountingProcedureService } from '../service/accounting-procedure.service';
import { AccountingProcedureFormService } from './accounting-procedure-form.service';

import { AccountingProcedureUpdateComponent } from './accounting-procedure-update.component';

describe('AccountingProcedure Management Update Component', () => {
  let comp: AccountingProcedureUpdateComponent;
  let fixture: ComponentFixture<AccountingProcedureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountingProcedureFormService: AccountingProcedureFormService;
  let accountingProcedureService: AccountingProcedureService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccountingProcedureUpdateComponent],
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
      .overrideTemplate(AccountingProcedureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountingProcedureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountingProcedureFormService = TestBed.inject(AccountingProcedureFormService);
    accountingProcedureService = TestBed.inject(AccountingProcedureService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const accountingProcedure: IAccountingProcedure = { id: 456 };
      const status: IRecordStatus = { id: '34f14fe2-2c55-4d9b-b137-aa33f667e0ad' };
      accountingProcedure.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'b7d26826-fc91-486f-8b58-14b5439631c2' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const accountingProcedure: IAccountingProcedure = { id: 456 };
      const internalUser: IApplicationUser = { id: 22714 };
      accountingProcedure.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 5494 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accountingProcedure: IAccountingProcedure = { id: 456 };
      const company: ICompany = { id: 4008 };
      accountingProcedure.company = company;

      const companyCollection: ICompany[] = [{ id: 999 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountingProcedure query and add missing value', () => {
      const accountingProcedure: IAccountingProcedure = { id: 456 };
      const executeAfter: IAccountingProcedure = { id: 1967 };
      accountingProcedure.executeAfter = executeAfter;

      const accountingProcedureCollection: IAccountingProcedure[] = [{ id: 27721 }];
      jest.spyOn(accountingProcedureService, 'query').mockReturnValue(of(new HttpResponse({ body: accountingProcedureCollection })));
      const additionalAccountingProcedures = [executeAfter];
      const expectedCollection: IAccountingProcedure[] = [...additionalAccountingProcedures, ...accountingProcedureCollection];
      jest.spyOn(accountingProcedureService, 'addAccountingProcedureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      expect(accountingProcedureService.query).toHaveBeenCalled();
      expect(accountingProcedureService.addAccountingProcedureToCollectionIfMissing).toHaveBeenCalledWith(
        accountingProcedureCollection,
        ...additionalAccountingProcedures.map(expect.objectContaining),
      );
      expect(comp.accountingProceduresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountingProcedure: IAccountingProcedure = { id: 456 };
      const status: IRecordStatus = { id: '78c78756-7af8-4574-b47f-f74bf4da1f9b' };
      accountingProcedure.status = status;
      const internalUser: IApplicationUser = { id: 10939 };
      accountingProcedure.internalUser = internalUser;
      const company: ICompany = { id: 30130 };
      accountingProcedure.company = company;
      const executeAfter: IAccountingProcedure = { id: 15065 };
      accountingProcedure.executeAfter = executeAfter;

      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountingProceduresSharedCollection).toContain(executeAfter);
      expect(comp.accountingProcedure).toEqual(accountingProcedure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedure>>();
      const accountingProcedure = { id: 123 };
      jest.spyOn(accountingProcedureFormService, 'getAccountingProcedure').mockReturnValue(accountingProcedure);
      jest.spyOn(accountingProcedureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountingProcedure }));
      saveSubject.complete();

      // THEN
      expect(accountingProcedureFormService.getAccountingProcedure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountingProcedureService.update).toHaveBeenCalledWith(expect.objectContaining(accountingProcedure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedure>>();
      const accountingProcedure = { id: 123 };
      jest.spyOn(accountingProcedureFormService, 'getAccountingProcedure').mockReturnValue({ id: null });
      jest.spyOn(accountingProcedureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountingProcedure }));
      saveSubject.complete();

      // THEN
      expect(accountingProcedureFormService.getAccountingProcedure).toHaveBeenCalled();
      expect(accountingProcedureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountingProcedure>>();
      const accountingProcedure = { id: 123 };
      jest.spyOn(accountingProcedureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountingProcedure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountingProcedureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRecordStatus', () => {
      it('Should forward to recordStatusService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(recordStatusService, 'compareRecordStatus');
        comp.compareRecordStatus(entity, entity2);
        expect(recordStatusService.compareRecordStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
