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
import { IOrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';
import { OrgUnitFormService } from './org-unit-form.service';

import { OrgUnitUpdateComponent } from './org-unit-update.component';

describe('OrgUnit Management Update Component', () => {
  let comp: OrgUnitUpdateComponent;
  let fixture: ComponentFixture<OrgUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgUnitFormService: OrgUnitFormService;
  let orgUnitService: OrgUnitService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, OrgUnitUpdateComponent],
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
      .overrideTemplate(OrgUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgUnitFormService = TestBed.inject(OrgUnitFormService);
    orgUnitService = TestBed.inject(OrgUnitService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const orgUnit: IOrgUnit = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '432a1538-c56c-4358-b7dc-06ce97bae790' };
      orgUnit.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: 'e9b07338-c406-4002-a020-e1c5056abb9e' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const orgUnit: IOrgUnit = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 866 };
      orgUnit.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 16018 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const orgUnit: IOrgUnit = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 31763 };
      orgUnit.company = company;

      const companyCollection: ICompany[] = [{ id: 10682 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrgUnit query and add missing value', () => {
      const orgUnit: IOrgUnit = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const parent: IOrgUnit = { id: '134c2e0d-814b-4e37-bc4b-b3bb0225b8b4' };
      orgUnit.parent = parent;

      const orgUnitCollection: IOrgUnit[] = [{ id: 'efc92124-dd51-46e4-991d-f9c7bb5c02cc' }];
      jest.spyOn(orgUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: orgUnitCollection })));
      const additionalOrgUnits = [parent];
      const expectedCollection: IOrgUnit[] = [...additionalOrgUnits, ...orgUnitCollection];
      jest.spyOn(orgUnitService, 'addOrgUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(orgUnitService.query).toHaveBeenCalled();
      expect(orgUnitService.addOrgUnitToCollectionIfMissing).toHaveBeenCalledWith(
        orgUnitCollection,
        ...additionalOrgUnits.map(expect.objectContaining),
      );
      expect(comp.orgUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orgUnit: IOrgUnit = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '9cdf9fe6-797f-4609-9003-4640c407f13c' };
      orgUnit.status = status;
      const internalUser: IApplicationUser = { id: 23972 };
      orgUnit.internalUser = internalUser;
      const company: ICompany = { id: 9304 };
      orgUnit.company = company;
      const parent: IOrgUnit = { id: 'dc2c0c10-cee9-4e92-b49a-67400cbad2fb' };
      orgUnit.parent = parent;

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.orgUnitsSharedCollection).toContain(parent);
      expect(comp.orgUnit).toEqual(orgUnit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUnit>>();
      const orgUnit = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgUnitFormService, 'getOrgUnit').mockReturnValue(orgUnit);
      jest.spyOn(orgUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUnit }));
      saveSubject.complete();

      // THEN
      expect(orgUnitFormService.getOrgUnit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgUnitService.update).toHaveBeenCalledWith(expect.objectContaining(orgUnit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUnit>>();
      const orgUnit = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgUnitFormService, 'getOrgUnit').mockReturnValue({ id: null });
      jest.spyOn(orgUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUnit }));
      saveSubject.complete();

      // THEN
      expect(orgUnitFormService.getOrgUnit).toHaveBeenCalled();
      expect(orgUnitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUnit>>();
      const orgUnit = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(orgUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgUnitService.update).toHaveBeenCalled();
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

    describe('compareOrgUnit', () => {
      it('Should forward to orgUnitService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orgUnitService, 'compareOrgUnit');
        comp.compareOrgUnit(entity, entity2);
        expect(orgUnitService.compareOrgUnit).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
