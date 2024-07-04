import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { RecordStatusService } from 'app/entities/BulutAttendance/record-status/service/record-status.service';
import { IHrLetterType } from 'app/entities/BulutAttendance/hr-letter-type/hr-letter-type.model';
import { HrLetterTypeService } from 'app/entities/BulutAttendance/hr-letter-type/service/hr-letter-type.service';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IOrgPosition } from 'app/entities/BulutAttendance/org-position/org-position.model';
import { OrgPositionService } from 'app/entities/BulutAttendance/org-position/service/org-position.service';
import { IOrgUnit } from 'app/entities/BulutAttendance/org-unit/org-unit.model';
import { OrgUnitService } from 'app/entities/BulutAttendance/org-unit/service/org-unit.service';
import { IPersonnelStatus } from 'app/entities/BulutAttendance/personnel-status/personnel-status.model';
import { PersonnelStatusService } from 'app/entities/BulutAttendance/personnel-status/service/personnel-status.service';
import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { HrLetterParameterService } from 'app/entities/BulutAttendance/hr-letter-parameter/service/hr-letter-parameter.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IHrLetter } from '../hr-letter.model';
import { HrLetterService } from '../service/hr-letter.service';
import { HrLetterFormService } from './hr-letter-form.service';

import { HrLetterUpdateComponent } from './hr-letter-update.component';

describe('HrLetter Management Update Component', () => {
  let comp: HrLetterUpdateComponent;
  let fixture: ComponentFixture<HrLetterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hrLetterFormService: HrLetterFormService;
  let hrLetterService: HrLetterService;
  let recordStatusService: RecordStatusService;
  let hrLetterTypeService: HrLetterTypeService;
  let personnelService: PersonnelService;
  let orgPositionService: OrgPositionService;
  let orgUnitService: OrgUnitService;
  let personnelStatusService: PersonnelStatusService;
  let hrLetterParameterService: HrLetterParameterService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, HrLetterUpdateComponent],
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
      .overrideTemplate(HrLetterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HrLetterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hrLetterFormService = TestBed.inject(HrLetterFormService);
    hrLetterService = TestBed.inject(HrLetterService);
    recordStatusService = TestBed.inject(RecordStatusService);
    hrLetterTypeService = TestBed.inject(HrLetterTypeService);
    personnelService = TestBed.inject(PersonnelService);
    orgPositionService = TestBed.inject(OrgPositionService);
    orgUnitService = TestBed.inject(OrgUnitService);
    personnelStatusService = TestBed.inject(PersonnelStatusService);
    hrLetterParameterService = TestBed.inject(HrLetterParameterService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '58153340-445d-449b-b1c4-7c94dfa414ee' };
      hrLetter.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: '129e65c8-bc83-465e-8c9a-3b094a409e22' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HrLetterType query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const type: IHrLetterType = { id: 'd015f63c-c434-4c31-a1d1-f899a058419d' };
      hrLetter.type = type;

      const hrLetterTypeCollection: IHrLetterType[] = [{ id: '0a92c3a5-d371-4eb3-a5fb-cd725043addb' }];
      jest.spyOn(hrLetterTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: hrLetterTypeCollection })));
      const additionalHrLetterTypes = [type];
      const expectedCollection: IHrLetterType[] = [...additionalHrLetterTypes, ...hrLetterTypeCollection];
      jest.spyOn(hrLetterTypeService, 'addHrLetterTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(hrLetterTypeService.query).toHaveBeenCalled();
      expect(hrLetterTypeService.addHrLetterTypeToCollectionIfMissing).toHaveBeenCalledWith(
        hrLetterTypeCollection,
        ...additionalHrLetterTypes.map(expect.objectContaining),
      );
      expect(comp.hrLetterTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personnel query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const personnelId: IPersonnel = { id: '1240eb17-233a-4a68-9adc-09b43b18fbe0' };
      hrLetter.personnelId = personnelId;

      const personnelCollection: IPersonnel[] = [{ id: 'bd8e6f78-42fe-4dad-97c1-f7af1ef053f1' }];
      jest.spyOn(personnelService, 'query').mockReturnValue(of(new HttpResponse({ body: personnelCollection })));
      const additionalPersonnel = [personnelId];
      const expectedCollection: IPersonnel[] = [...additionalPersonnel, ...personnelCollection];
      jest.spyOn(personnelService, 'addPersonnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(personnelService.query).toHaveBeenCalled();
      expect(personnelService.addPersonnelToCollectionIfMissing).toHaveBeenCalledWith(
        personnelCollection,
        ...additionalPersonnel.map(expect.objectContaining),
      );
      expect(comp.personnelSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrgPosition query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orgPosition: IOrgPosition = { id: 'a3521de9-9dfb-49d1-a34b-44b394d18dee' };
      hrLetter.orgPosition = orgPosition;

      const orgPositionCollection: IOrgPosition[] = [{ id: '9f92ae0d-9e4b-454b-8b92-e583f6cbd564' }];
      jest.spyOn(orgPositionService, 'query').mockReturnValue(of(new HttpResponse({ body: orgPositionCollection })));
      const additionalOrgPositions = [orgPosition];
      const expectedCollection: IOrgPosition[] = [...additionalOrgPositions, ...orgPositionCollection];
      jest.spyOn(orgPositionService, 'addOrgPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(orgPositionService.query).toHaveBeenCalled();
      expect(orgPositionService.addOrgPositionToCollectionIfMissing).toHaveBeenCalledWith(
        orgPositionCollection,
        ...additionalOrgPositions.map(expect.objectContaining),
      );
      expect(comp.orgPositionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrgUnit query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orgUnit: IOrgUnit = { id: '2b5df888-8cdb-4d0a-9072-c0559728afd9' };
      hrLetter.orgUnit = orgUnit;

      const orgUnitCollection: IOrgUnit[] = [{ id: '5f0e93ad-6d4d-49e6-beb1-cf6312f0336a' }];
      jest.spyOn(orgUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: orgUnitCollection })));
      const additionalOrgUnits = [orgUnit];
      const expectedCollection: IOrgUnit[] = [...additionalOrgUnits, ...orgUnitCollection];
      jest.spyOn(orgUnitService, 'addOrgUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(orgUnitService.query).toHaveBeenCalled();
      expect(orgUnitService.addOrgUnitToCollectionIfMissing).toHaveBeenCalledWith(
        orgUnitCollection,
        ...additionalOrgUnits.map(expect.objectContaining),
      );
      expect(comp.orgUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PersonnelStatus query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const personnelStatus: IPersonnelStatus = { id: 'ae066dce-f755-477c-9ce9-d554f6b2b28f' };
      hrLetter.personnelStatus = personnelStatus;

      const personnelStatusCollection: IPersonnelStatus[] = [{ id: 'b1181be2-0ced-457f-909a-e80dccf1d96b' }];
      jest.spyOn(personnelStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: personnelStatusCollection })));
      const additionalPersonnelStatuses = [personnelStatus];
      const expectedCollection: IPersonnelStatus[] = [...additionalPersonnelStatuses, ...personnelStatusCollection];
      jest.spyOn(personnelStatusService, 'addPersonnelStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(personnelStatusService.query).toHaveBeenCalled();
      expect(personnelStatusService.addPersonnelStatusToCollectionIfMissing).toHaveBeenCalledWith(
        personnelStatusCollection,
        ...additionalPersonnelStatuses.map(expect.objectContaining),
      );
      expect(comp.personnelStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HrLetterParameter query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const hrLetterParameter: IHrLetterParameter = { id: 4430 };
      hrLetter.hrLetterParameter = hrLetterParameter;

      const hrLetterParameterCollection: IHrLetterParameter[] = [{ id: 8069 }];
      jest.spyOn(hrLetterParameterService, 'query').mockReturnValue(of(new HttpResponse({ body: hrLetterParameterCollection })));
      const additionalHrLetterParameters = [hrLetterParameter];
      const expectedCollection: IHrLetterParameter[] = [...additionalHrLetterParameters, ...hrLetterParameterCollection];
      jest.spyOn(hrLetterParameterService, 'addHrLetterParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(hrLetterParameterService.query).toHaveBeenCalled();
      expect(hrLetterParameterService.addHrLetterParameterToCollectionIfMissing).toHaveBeenCalledWith(
        hrLetterParameterCollection,
        ...additionalHrLetterParameters.map(expect.objectContaining),
      );
      expect(comp.hrLetterParametersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 13718 };
      hrLetter.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 16726 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 2481 };
      hrLetter.company = company;

      const companyCollection: ICompany[] = [{ id: 2078 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hrLetter: IHrLetter = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '63b7fa92-f68b-4c8a-a761-fd5e02da9794' };
      hrLetter.status = status;
      const type: IHrLetterType = { id: '8f09147d-ea20-482e-8f33-ceb749956b29' };
      hrLetter.type = type;
      const personnelId: IPersonnel = { id: '129fe0ef-7d05-4109-a09b-abfa13ed2ea2' };
      hrLetter.personnelId = personnelId;
      const orgPosition: IOrgPosition = { id: '51db3e27-c957-482c-b428-179e7c6d68eb' };
      hrLetter.orgPosition = orgPosition;
      const orgUnit: IOrgUnit = { id: '3002f307-7979-49c9-8f20-41dd3ba64acc' };
      hrLetter.orgUnit = orgUnit;
      const personnelStatus: IPersonnelStatus = { id: '86572078-9990-4f36-b6c0-88bc606f31f9' };
      hrLetter.personnelStatus = personnelStatus;
      const hrLetterParameter: IHrLetterParameter = { id: 4633 };
      hrLetter.hrLetterParameter = hrLetterParameter;
      const internalUser: IApplicationUser = { id: 29306 };
      hrLetter.internalUser = internalUser;
      const company: ICompany = { id: 32055 };
      hrLetter.company = company;

      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.hrLetterTypesSharedCollection).toContain(type);
      expect(comp.personnelSharedCollection).toContain(personnelId);
      expect(comp.orgPositionsSharedCollection).toContain(orgPosition);
      expect(comp.orgUnitsSharedCollection).toContain(orgUnit);
      expect(comp.personnelStatusesSharedCollection).toContain(personnelStatus);
      expect(comp.hrLetterParametersSharedCollection).toContain(hrLetterParameter);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.hrLetter).toEqual(hrLetter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetter>>();
      const hrLetter = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hrLetterFormService, 'getHrLetter').mockReturnValue(hrLetter);
      jest.spyOn(hrLetterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hrLetter }));
      saveSubject.complete();

      // THEN
      expect(hrLetterFormService.getHrLetter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hrLetterService.update).toHaveBeenCalledWith(expect.objectContaining(hrLetter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetter>>();
      const hrLetter = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hrLetterFormService, 'getHrLetter').mockReturnValue({ id: null });
      jest.spyOn(hrLetterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hrLetter }));
      saveSubject.complete();

      // THEN
      expect(hrLetterFormService.getHrLetter).toHaveBeenCalled();
      expect(hrLetterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHrLetter>>();
      const hrLetter = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(hrLetterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hrLetter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hrLetterService.update).toHaveBeenCalled();
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

    describe('compareHrLetterType', () => {
      it('Should forward to hrLetterTypeService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(hrLetterTypeService, 'compareHrLetterType');
        comp.compareHrLetterType(entity, entity2);
        expect(hrLetterTypeService.compareHrLetterType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareOrgPosition', () => {
      it('Should forward to orgPositionService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orgPositionService, 'compareOrgPosition');
        comp.compareOrgPosition(entity, entity2);
        expect(orgPositionService.compareOrgPosition).toHaveBeenCalledWith(entity, entity2);
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

    describe('comparePersonnelStatus', () => {
      it('Should forward to personnelStatusService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(personnelStatusService, 'comparePersonnelStatus');
        comp.comparePersonnelStatus(entity, entity2);
        expect(personnelStatusService.comparePersonnelStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareHrLetterParameter', () => {
      it('Should forward to hrLetterParameterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hrLetterParameterService, 'compareHrLetterParameter');
        comp.compareHrLetterParameter(entity, entity2);
        expect(hrLetterParameterService.compareHrLetterParameter).toHaveBeenCalledWith(entity, entity2);
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
