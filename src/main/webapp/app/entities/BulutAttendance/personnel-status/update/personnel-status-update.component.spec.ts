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
import { IPersonnelStatus } from '../personnel-status.model';
import { PersonnelStatusService } from '../service/personnel-status.service';
import { PersonnelStatusFormService } from './personnel-status-form.service';

import { PersonnelStatusUpdateComponent } from './personnel-status-update.component';

describe('PersonnelStatus Management Update Component', () => {
  let comp: PersonnelStatusUpdateComponent;
  let fixture: ComponentFixture<PersonnelStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personnelStatusFormService: PersonnelStatusFormService;
  let personnelStatusService: PersonnelStatusService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PersonnelStatusUpdateComponent],
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
      .overrideTemplate(PersonnelStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonnelStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personnelStatusFormService = TestBed.inject(PersonnelStatusFormService);
    personnelStatusService = TestBed.inject(PersonnelStatusService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const personnelStatus: IPersonnelStatus = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '6d5553bc-fecb-42f4-85ba-34bf52a9fadb' };
      personnelStatus.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: '78909ae0-1fc5-470f-b4a7-e8467a7994d4' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const personnelStatus: IPersonnelStatus = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 13766 };
      personnelStatus.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 11486 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const personnelStatus: IPersonnelStatus = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 20110 };
      personnelStatus.company = company;

      const companyCollection: ICompany[] = [{ id: 20330 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personnelStatus: IPersonnelStatus = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'ed82775a-89f3-4f24-a8e5-9a57ec09c98c' };
      personnelStatus.status = status;
      const internalUser: IApplicationUser = { id: 28569 };
      personnelStatus.internalUser = internalUser;
      const company: ICompany = { id: 11520 };
      personnelStatus.company = company;

      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.personnelStatus).toEqual(personnelStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonnelStatus>>();
      const personnelStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(personnelStatusFormService, 'getPersonnelStatus').mockReturnValue(personnelStatus);
      jest.spyOn(personnelStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnelStatus }));
      saveSubject.complete();

      // THEN
      expect(personnelStatusFormService.getPersonnelStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personnelStatusService.update).toHaveBeenCalledWith(expect.objectContaining(personnelStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonnelStatus>>();
      const personnelStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(personnelStatusFormService, 'getPersonnelStatus').mockReturnValue({ id: null });
      jest.spyOn(personnelStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnelStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnelStatus }));
      saveSubject.complete();

      // THEN
      expect(personnelStatusFormService.getPersonnelStatus).toHaveBeenCalled();
      expect(personnelStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonnelStatus>>();
      const personnelStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(personnelStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnelStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personnelStatusService.update).toHaveBeenCalled();
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
