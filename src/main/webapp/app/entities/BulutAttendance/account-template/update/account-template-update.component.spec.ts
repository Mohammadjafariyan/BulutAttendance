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
import { IAccountTemplate } from '../account-template.model';
import { AccountTemplateService } from '../service/account-template.service';
import { AccountTemplateFormService } from './account-template-form.service';

import { AccountTemplateUpdateComponent } from './account-template-update.component';

describe('AccountTemplate Management Update Component', () => {
  let comp: AccountTemplateUpdateComponent;
  let fixture: ComponentFixture<AccountTemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountTemplateFormService: AccountTemplateFormService;
  let accountTemplateService: AccountTemplateService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccountTemplateUpdateComponent],
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
      .overrideTemplate(AccountTemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountTemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountTemplateFormService = TestBed.inject(AccountTemplateFormService);
    accountTemplateService = TestBed.inject(AccountTemplateService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const accountTemplate: IAccountTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'af392b39-0c71-4cd5-bd08-12678b775490' };
      accountTemplate.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'df07cf3a-04ca-4c63-a362-8c97dacd8b35' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const accountTemplate: IAccountTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 1780 };
      accountTemplate.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 24939 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accountTemplate: IAccountTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 8326 };
      accountTemplate.company = company;

      const companyCollection: ICompany[] = [{ id: 17135 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountTemplate: IAccountTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'cce6f88f-2748-419a-8556-08c8a42cfdd3' };
      accountTemplate.status = status;
      const internalUser: IApplicationUser = { id: 10278 };
      accountTemplate.internalUser = internalUser;
      const company: ICompany = { id: 22561 };
      accountTemplate.company = company;

      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountTemplate).toEqual(accountTemplate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountTemplate>>();
      const accountTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountTemplateFormService, 'getAccountTemplate').mockReturnValue(accountTemplate);
      jest.spyOn(accountTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountTemplate }));
      saveSubject.complete();

      // THEN
      expect(accountTemplateFormService.getAccountTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountTemplateService.update).toHaveBeenCalledWith(expect.objectContaining(accountTemplate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountTemplate>>();
      const accountTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountTemplateFormService, 'getAccountTemplate').mockReturnValue({ id: null });
      jest.spyOn(accountTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountTemplate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountTemplate }));
      saveSubject.complete();

      // THEN
      expect(accountTemplateFormService.getAccountTemplate).toHaveBeenCalled();
      expect(accountTemplateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountTemplate>>();
      const accountTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountTemplateService.update).toHaveBeenCalled();
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
  });
});
