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
import { IHrLetterParameter } from '../hr-letter-parameter.model';
import { HrLetterParameterService } from '../service/hr-letter-parameter.service';
import { HrLetterParameterFormService } from './hr-letter-parameter-form.service';

import { HrLetterParameterUpdateComponent } from './hr-letter-parameter-update.component';

describe('HrLetterParameter Management Update Component', () => {
  let comp: HrLetterParameterUpdateComponent;
  let fixture: ComponentFixture<HrLetterParameterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hrLetterParameterFormService: HrLetterParameterFormService;
  let hrLetterParameterService: HrLetterParameterService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HrLetterParameterUpdateComponent],
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
      .overrideTemplate(HrLetterParameterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HrLetterParameterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hrLetterParameterFormService = TestBed.inject(HrLetterParameterFormService);
    hrLetterParameterService = TestBed.inject(HrLetterParameterService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const hrLetterParameter: IHrLetterParameter = { id: 456 };
      const status: IRecordStatus = { id: 'a1876e4d-c1dc-4051-a29e-7ae381622fbb' };
      hrLetterParameter.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: '2bfc431e-07e6-4b4b-9815-e7e689ef1877' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const hrLetterParameter: IHrLetterParameter = { id: 456 };
      const internalUser: IApplicationUser = { id: 17686 };
      hrLetterParameter.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 9515 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const hrLetterParameter: IHrLetterParameter = { id: 456 };
      const company: ICompany = { id: 12079 };
      hrLetterParameter.company = company;

      const companyCollection: ICompany[] = [{ id: 32096 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hrLetterParameter: IHrLetterParameter = { id: 456 };
      const status: IRecordStatus = { id: '6342b85f-404c-4a10-a466-06707ff9c371' };
      hrLetterParameter.status = status;
      const internalUser: IApplicationUser = { id: 28593 };
      hrLetterParameter.internalUser = internalUser;
      const company: ICompany = { id: 796 };
      hrLetterParameter.company = company;

      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.hrLetterParameter).toEqual(hrLetterParameter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetterParameter>>();
      const hrLetterParameter = { id: 123 };
      jest.spyOn(hrLetterParameterFormService, 'getHrLetterParameter').mockReturnValue(hrLetterParameter);
      jest.spyOn(hrLetterParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hrLetterParameter }));
      saveSubject.complete();

      // THEN
      expect(hrLetterParameterFormService.getHrLetterParameter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hrLetterParameterService.update).toHaveBeenCalledWith(expect.objectContaining(hrLetterParameter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetterParameter>>();
      const hrLetterParameter = { id: 123 };
      jest.spyOn(hrLetterParameterFormService, 'getHrLetterParameter').mockReturnValue({ id: null });
      jest.spyOn(hrLetterParameterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetterParameter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hrLetterParameter }));
      saveSubject.complete();

      // THEN
      expect(hrLetterParameterFormService.getHrLetterParameter).toHaveBeenCalled();
      expect(hrLetterParameterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetterParameter>>();
      const hrLetterParameter = { id: 123 };
      jest.spyOn(hrLetterParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetterParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hrLetterParameterService.update).toHaveBeenCalled();
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
