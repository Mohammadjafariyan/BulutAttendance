import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { HrLetterParameterService } from 'app/entities/BulutAttendance/hr-letter-parameter/service/hr-letter-parameter.service';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IWork } from 'app/entities/BulutAttendance/work/work.model';
import { WorkService } from 'app/entities/BulutAttendance/work/service/work.service';
import { IWorkItem } from '../work-item.model';
import { WorkItemService } from '../service/work-item.service';
import { WorkItemFormService } from './work-item-form.service';

import { WorkItemUpdateComponent } from './work-item-update.component';

describe('WorkItem Management Update Component', () => {
  let comp: WorkItemUpdateComponent;
  let fixture: ComponentFixture<WorkItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workItemFormService: WorkItemFormService;
  let workItemService: WorkItemService;
  let hrLetterParameterService: HrLetterParameterService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let workService: WorkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, WorkItemUpdateComponent],
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
      .overrideTemplate(WorkItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workItemFormService = TestBed.inject(WorkItemFormService);
    workItemService = TestBed.inject(WorkItemService);
    hrLetterParameterService = TestBed.inject(HrLetterParameterService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    workService = TestBed.inject(WorkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call hrLetterParameter query and add missing value', () => {
      const workItem: IWorkItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const hrLetterParameter: IHrLetterParameter = { id: 7249 };
      workItem.hrLetterParameter = hrLetterParameter;

      const hrLetterParameterCollection: IHrLetterParameter[] = [{ id: 6299 }];
      jest.spyOn(hrLetterParameterService, 'query').mockReturnValue(of(new HttpResponse({ body: hrLetterParameterCollection })));
      const expectedCollection: IHrLetterParameter[] = [hrLetterParameter, ...hrLetterParameterCollection];
      jest.spyOn(hrLetterParameterService, 'addHrLetterParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      expect(hrLetterParameterService.query).toHaveBeenCalled();
      expect(hrLetterParameterService.addHrLetterParameterToCollectionIfMissing).toHaveBeenCalledWith(
        hrLetterParameterCollection,
        hrLetterParameter,
      );
      expect(comp.hrLetterParametersCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const workItem: IWorkItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 18332 };
      workItem.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 6674 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const workItem: IWorkItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 10578 };
      workItem.company = company;

      const companyCollection: ICompany[] = [{ id: 2926 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Work query and add missing value', () => {
      const workItem: IWorkItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const work: IWork = { id: '69eb355a-d7d8-4abb-a7a5-6c695e75e6c4' };
      workItem.work = work;

      const workCollection: IWork[] = [{ id: 'c5a3daee-8a7b-4cb0-b524-2c9a11e2eea9' }];
      jest.spyOn(workService, 'query').mockReturnValue(of(new HttpResponse({ body: workCollection })));
      const additionalWorks = [work];
      const expectedCollection: IWork[] = [...additionalWorks, ...workCollection];
      jest.spyOn(workService, 'addWorkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      expect(workService.query).toHaveBeenCalled();
      expect(workService.addWorkToCollectionIfMissing).toHaveBeenCalledWith(
        workCollection,
        ...additionalWorks.map(expect.objectContaining),
      );
      expect(comp.worksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workItem: IWorkItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const hrLetterParameter: IHrLetterParameter = { id: 25558 };
      workItem.hrLetterParameter = hrLetterParameter;
      const internalUser: IApplicationUser = { id: 21404 };
      workItem.internalUser = internalUser;
      const company: ICompany = { id: 27386 };
      workItem.company = company;
      const work: IWork = { id: '8afc0e94-67bd-411c-a433-82a4814b4c08' };
      workItem.work = work;

      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      expect(comp.hrLetterParametersCollection).toContain(hrLetterParameter);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.worksSharedCollection).toContain(work);
      expect(comp.workItem).toEqual(workItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkItem>>();
      const workItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(workItemFormService, 'getWorkItem').mockReturnValue(workItem);
      jest.spyOn(workItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workItem }));
      saveSubject.complete();

      // THEN
      expect(workItemFormService.getWorkItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workItemService.update).toHaveBeenCalledWith(expect.objectContaining(workItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkItem>>();
      const workItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(workItemFormService, 'getWorkItem').mockReturnValue({ id: null });
      jest.spyOn(workItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workItem }));
      saveSubject.complete();

      // THEN
      expect(workItemFormService.getWorkItem).toHaveBeenCalled();
      expect(workItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkItem>>();
      const workItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(workItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

    describe('compareWork', () => {
      it('Should forward to workService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(workService, 'compareWork');
        comp.compareWork(entity, entity2);
        expect(workService.compareWork).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
