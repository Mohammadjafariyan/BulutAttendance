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
import { ITaxTemplate } from '../tax-template.model';
import { TaxTemplateService } from '../service/tax-template.service';
import { TaxTemplateFormService } from './tax-template-form.service';

import { TaxTemplateUpdateComponent } from './tax-template-update.component';

describe('TaxTemplate Management Update Component', () => {
  let comp: TaxTemplateUpdateComponent;
  let fixture: ComponentFixture<TaxTemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taxTemplateFormService: TaxTemplateFormService;
  let taxTemplateService: TaxTemplateService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TaxTemplateUpdateComponent],
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
      .overrideTemplate(TaxTemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaxTemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxTemplateFormService = TestBed.inject(TaxTemplateFormService);
    taxTemplateService = TestBed.inject(TaxTemplateService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const taxTemplate: ITaxTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '26416734-3621-4f3f-a326-27a8291f8500' };
      taxTemplate.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'ad08b19c-b24b-4e3e-ad53-4de5004e4a8b' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const taxTemplate: ITaxTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 10857 };
      taxTemplate.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 3146 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const taxTemplate: ITaxTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 32228 };
      taxTemplate.company = company;

      const companyCollection: ICompany[] = [{ id: 25717 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const taxTemplate: ITaxTemplate = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '16ddb099-a8eb-441f-bdc5-1b0c9566a871' };
      taxTemplate.status = status;
      const internalUser: IApplicationUser = { id: 26978 };
      taxTemplate.internalUser = internalUser;
      const company: ICompany = { id: 455 };
      taxTemplate.company = company;

      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.taxTemplate).toEqual(taxTemplate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxTemplate>>();
      const taxTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(taxTemplateFormService, 'getTaxTemplate').mockReturnValue(taxTemplate);
      jest.spyOn(taxTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxTemplate }));
      saveSubject.complete();

      // THEN
      expect(taxTemplateFormService.getTaxTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxTemplateService.update).toHaveBeenCalledWith(expect.objectContaining(taxTemplate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxTemplate>>();
      const taxTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(taxTemplateFormService, 'getTaxTemplate').mockReturnValue({ id: null });
      jest.spyOn(taxTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxTemplate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: taxTemplate }));
      saveSubject.complete();

      // THEN
      expect(taxTemplateFormService.getTaxTemplate).toHaveBeenCalled();
      expect(taxTemplateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITaxTemplate>>();
      const taxTemplate = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(taxTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxTemplateService.update).toHaveBeenCalled();
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
