import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/BulutAttendance/application-user/service/application-user.service';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CompanyService } from 'app/entities/BulutAttendance/company/service/company.service';
import { IAccProccParameter } from '../acc-procc-parameter.model';
import { AccProccParameterService } from '../service/acc-procc-parameter.service';
import { AccProccParameterFormService } from './acc-procc-parameter-form.service';

import { AccProccParameterUpdateComponent } from './acc-procc-parameter-update.component';

describe('AccProccParameter Management Update Component', () => {
  let comp: AccProccParameterUpdateComponent;
  let fixture: ComponentFixture<AccProccParameterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accProccParameterFormService: AccProccParameterFormService;
  let accProccParameterService: AccProccParameterService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccProccParameterUpdateComponent],
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
      .overrideTemplate(AccProccParameterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccProccParameterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accProccParameterFormService = TestBed.inject(AccProccParameterFormService);
    accProccParameterService = TestBed.inject(AccProccParameterService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const accProccParameter: IAccProccParameter = { id: 456 };
      const internalUser: IApplicationUser = { id: 9964 };
      accProccParameter.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 3786 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProccParameter });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accProccParameter: IAccProccParameter = { id: 456 };
      const company: ICompany = { id: 31705 };
      accProccParameter.company = company;

      const companyCollection: ICompany[] = [{ id: 7122 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accProccParameter });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accProccParameter: IAccProccParameter = { id: 456 };
      const internalUser: IApplicationUser = { id: 28984 };
      accProccParameter.internalUser = internalUser;
      const company: ICompany = { id: 18679 };
      accProccParameter.company = company;

      activatedRoute.data = of({ accProccParameter });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accProccParameter).toEqual(accProccParameter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProccParameter>>();
      const accProccParameter = { id: 123 };
      jest.spyOn(accProccParameterFormService, 'getAccProccParameter').mockReturnValue(accProccParameter);
      jest.spyOn(accProccParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProccParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProccParameter }));
      saveSubject.complete();

      // THEN
      expect(accProccParameterFormService.getAccProccParameter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accProccParameterService.update).toHaveBeenCalledWith(expect.objectContaining(accProccParameter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProccParameter>>();
      const accProccParameter = { id: 123 };
      jest.spyOn(accProccParameterFormService, 'getAccProccParameter').mockReturnValue({ id: null });
      jest.spyOn(accProccParameterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProccParameter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accProccParameter }));
      saveSubject.complete();

      // THEN
      expect(accProccParameterFormService.getAccProccParameter).toHaveBeenCalled();
      expect(accProccParameterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccProccParameter>>();
      const accProccParameter = { id: 123 };
      jest.spyOn(accProccParameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accProccParameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accProccParameterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
