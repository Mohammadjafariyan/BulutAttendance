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
import { IOrgPosition } from '../org-position.model';
import { OrgPositionService } from '../service/org-position.service';
import { OrgPositionFormService } from './org-position-form.service';

import { OrgPositionUpdateComponent } from './org-position-update.component';

describe('OrgPosition Management Update Component', () => {
  let comp: OrgPositionUpdateComponent;
  let fixture: ComponentFixture<OrgPositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgPositionFormService: OrgPositionFormService;
  let orgPositionService: OrgPositionService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, OrgPositionUpdateComponent],
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
      .overrideTemplate(OrgPositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgPositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgPositionFormService = TestBed.inject(OrgPositionFormService);
    orgPositionService = TestBed.inject(OrgPositionService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const orgPosition: IOrgPosition = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'd3ff2b6a-eb52-44e5-acc6-3539b5108e9e' };
      orgPosition.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'a2d98231-e475-41af-b678-0723eeb3e023' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const orgPosition: IOrgPosition = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 28234 };
      orgPosition.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 14812 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const orgPosition: IOrgPosition = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 21235 };
      orgPosition.company = company;

      const companyCollection: ICompany[] = [{ id: 1274 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orgPosition: IOrgPosition = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'ef064139-9d2c-4b71-9792-c31a1aa9f559' };
      orgPosition.status = status;
      const internalUser: IApplicationUser = { id: 16771 };
      orgPosition.internalUser = internalUser;
      const company: ICompany = { id: 27425 };
      orgPosition.company = company;

      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.orgPosition).toEqual(orgPosition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgPosition>>();
      const orgPosition = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgPositionFormService, 'getOrgPosition').mockReturnValue(orgPosition);
      jest.spyOn(orgPositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgPosition }));
      saveSubject.complete();

      // THEN
      expect(orgPositionFormService.getOrgPosition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgPositionService.update).toHaveBeenCalledWith(expect.objectContaining(orgPosition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgPosition>>();
      const orgPosition = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgPositionFormService, 'getOrgPosition').mockReturnValue({ id: null });
      jest.spyOn(orgPositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgPosition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgPosition }));
      saveSubject.complete();

      // THEN
      expect(orgPositionFormService.getOrgPosition).toHaveBeenCalled();
      expect(orgPositionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgPosition>>();
      const orgPosition = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgPositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgPosition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgPositionService.update).toHaveBeenCalled();
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
