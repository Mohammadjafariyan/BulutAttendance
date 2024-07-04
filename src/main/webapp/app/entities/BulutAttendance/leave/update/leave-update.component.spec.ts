import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { ILeave } from '../leave.model';
import { LeaveService } from '../service/leave.service';
import { LeaveFormService } from './leave-form.service';

import { LeaveUpdateComponent } from './leave-update.component';

describe('Leave Management Update Component', () => {
  let comp: LeaveUpdateComponent;
  let fixture: ComponentFixture<LeaveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaveFormService: LeaveFormService;
  let leaveService: LeaveService;
  let recordStatusService: RecordStatusService;
  let personnelService: PersonnelService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, LeaveUpdateComponent],
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
      .overrideTemplate(LeaveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaveFormService = TestBed.inject(LeaveFormService);
    leaveService = TestBed.inject(LeaveService);
    recordStatusService = TestBed.inject(RecordStatusService);
    personnelService = TestBed.inject(PersonnelService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const leave: ILeave = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '197687e1-5f62-4007-ba0a-3cf59258980e' };
      leave.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'fee9224f-6196-4c91-b4d5-0a191e8d20aa' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personnel query and add missing value', () => {
      const leave: ILeave = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const personnelId: IPersonnel = { id: 'b8638bd8-20e5-4452-a772-c6ba5428a091' };
      leave.personnelId = personnelId;

      const personnelCollection: IPersonnel[] = [{ id: 'c7613f5e-5634-4d88-9d6f-bcc71160e4aa' }];
      jest.spyOn(personnelService, 'query').mockReturnValue(of(new HttpResponse({ body: personnelCollection })));
      const additionalPersonnel = [personnelId];
      const expectedCollection: IPersonnel[] = [...additionalPersonnel, ...personnelCollection];
      jest.spyOn(personnelService, 'addPersonnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(personnelService.query).toHaveBeenCalled();
      expect(personnelService.addPersonnelToCollectionIfMissing).toHaveBeenCalledWith(
        personnelCollection,
        ...additionalPersonnel.map(expect.objectContaining),
      );
      expect(comp.personnelSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const leave: ILeave = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 29458 };
      leave.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 10065 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const leave: ILeave = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 7393 };
      leave.company = company;

      const companyCollection: ICompany[] = [{ id: 1623 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leave: ILeave = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '45c3f355-7f48-4b83-8563-f48a5be5eaa5' };
      leave.status = status;
      const personnelId: IPersonnel = { id: '3a7e7459-eeb1-4a3a-8676-3c5942ca498d' };
      leave.personnelId = personnelId;
      const internalUser: IApplicationUser = { id: 12240 };
      leave.internalUser = internalUser;
      const company: ICompany = { id: 204 };
      leave.company = company;

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.personnelSharedCollection).toContain(personnelId);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.leave).toEqual(leave);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeave>>();
      const leave = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(leaveFormService, 'getLeave').mockReturnValue(leave);
      jest.spyOn(leaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leave }));
      saveSubject.complete();

      // THEN
      expect(leaveFormService.getLeave).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaveService.update).toHaveBeenCalledWith(expect.objectContaining(leave));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeave>>();
      const leave = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(leaveFormService, 'getLeave').mockReturnValue({ id: null });
      jest.spyOn(leaveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leave }));
      saveSubject.complete();

      // THEN
      expect(leaveFormService.getLeave).toHaveBeenCalled();
      expect(leaveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeave>>();
      const leave = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(leaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaveService.update).toHaveBeenCalled();
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

    describe('comparePersonnel', () => {
      it('Should forward to personnelService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(personnelService, 'comparePersonnel');
        comp.comparePersonnel(entity, entity2);
        expect(personnelService.comparePersonnel).toHaveBeenCalledWith(entity, entity2);
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
