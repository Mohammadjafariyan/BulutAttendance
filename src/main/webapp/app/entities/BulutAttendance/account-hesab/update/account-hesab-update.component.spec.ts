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
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { PersonnelService } from 'app/entities/BulutAttendance/personnel/service/personnel.service';
import { IBank } from 'app/entities/BulutAttendance/bank/bank.model';
import { BankService } from 'app/entities/BulutAttendance/bank/service/bank.service';
import { IAccountHesab } from '../account-hesab.model';
import { AccountHesabService } from '../service/account-hesab.service';
import { AccountHesabFormService } from './account-hesab-form.service';

import { AccountHesabUpdateComponent } from './account-hesab-update.component';

describe('AccountHesab Management Update Component', () => {
  let comp: AccountHesabUpdateComponent;
  let fixture: ComponentFixture<AccountHesabUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountHesabFormService: AccountHesabFormService;
  let accountHesabService: AccountHesabService;
  let recordStatusService: RecordStatusService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let personnelService: PersonnelService;
  let bankService: BankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AccountHesabUpdateComponent],
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
      .overrideTemplate(AccountHesabUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountHesabUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountHesabFormService = TestBed.inject(AccountHesabFormService);
    accountHesabService = TestBed.inject(AccountHesabService);
    recordStatusService = TestBed.inject(RecordStatusService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    personnelService = TestBed.inject(PersonnelService);
    bankService = TestBed.inject(BankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RecordStatus query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: '20f0daac-ffb9-4be5-9e28-2766f306909f' };
      accountHesab.status = status;

      const recordStatusCollection: IRecordStatus[] = [{ id: '5c2aa5be-4aff-4aac-8bdf-12ba8a99f351' }];
      jest.spyOn(recordStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: recordStatusCollection })));
      const additionalRecordStatuses = [status];
      const expectedCollection: IRecordStatus[] = [...additionalRecordStatuses, ...recordStatusCollection];
      jest.spyOn(recordStatusService, 'addRecordStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(recordStatusService.query).toHaveBeenCalled();
      expect(recordStatusService.addRecordStatusToCollectionIfMissing).toHaveBeenCalledWith(
        recordStatusCollection,
        ...additionalRecordStatuses.map(expect.objectContaining),
      );
      expect(comp.recordStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const internalUser: IApplicationUser = { id: 5397 };
      accountHesab.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 7918 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const company: ICompany = { id: 26792 };
      accountHesab.company = company;

      const companyCollection: ICompany[] = [{ id: 28335 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountHesab query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const parentAccountId: IAccountHesab = { id: '490f822f-d8b4-4cc9-b33c-a4af0f8633a1' };
      accountHesab.parentAccountId = parentAccountId;

      const accountHesabCollection: IAccountHesab[] = [{ id: '00e07e78-7731-4cc8-930c-441e6e863d5b' }];
      jest.spyOn(accountHesabService, 'query').mockReturnValue(of(new HttpResponse({ body: accountHesabCollection })));
      const additionalAccountHesabs = [parentAccountId];
      const expectedCollection: IAccountHesab[] = [...additionalAccountHesabs, ...accountHesabCollection];
      jest.spyOn(accountHesabService, 'addAccountHesabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(accountHesabService.query).toHaveBeenCalled();
      expect(accountHesabService.addAccountHesabToCollectionIfMissing).toHaveBeenCalledWith(
        accountHesabCollection,
        ...additionalAccountHesabs.map(expect.objectContaining),
      );
      expect(comp.accountHesabsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Personnel query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const personnelId: IPersonnel = { id: '5affc048-0f97-4f92-9fe4-938d0136b45c' };
      accountHesab.personnelId = personnelId;

      const personnelCollection: IPersonnel[] = [{ id: '0c2c7198-fd3d-4e3c-9afc-5c83606906a8' }];
      jest.spyOn(personnelService, 'query').mockReturnValue(of(new HttpResponse({ body: personnelCollection })));
      const additionalPersonnel = [personnelId];
      const expectedCollection: IPersonnel[] = [...additionalPersonnel, ...personnelCollection];
      jest.spyOn(personnelService, 'addPersonnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(personnelService.query).toHaveBeenCalled();
      expect(personnelService.addPersonnelToCollectionIfMissing).toHaveBeenCalledWith(
        personnelCollection,
        ...additionalPersonnel.map(expect.objectContaining),
      );
      expect(comp.personnelSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bank query and add missing value', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const bank: IBank = { id: 'cb61d944-2e3b-4374-bb78-9675488410a6' };
      accountHesab.bank = bank;

      const bankCollection: IBank[] = [{ id: 'c11fc966-c3d8-4e01-979a-7b998599f37c' }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [bank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining),
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountHesab: IAccountHesab = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const status: IRecordStatus = { id: 'e8e02b37-f556-4def-b3be-ba942fb6c782' };
      accountHesab.status = status;
      const internalUser: IApplicationUser = { id: 4053 };
      accountHesab.internalUser = internalUser;
      const company: ICompany = { id: 2385 };
      accountHesab.company = company;
      const parentAccountId: IAccountHesab = { id: 'b9f359f1-a4df-42e2-9cda-a659c63ecd5f' };
      accountHesab.parentAccountId = parentAccountId;
      const personnelId: IPersonnel = { id: 'c8a8e7f1-6d2d-4e88-8b87-767047687336' };
      accountHesab.personnelId = personnelId;
      const bank: IBank = { id: '9e00570b-1d74-4e14-8cda-4f6e2d36d401' };
      accountHesab.bank = bank;

      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      expect(comp.recordStatusesSharedCollection).toContain(status);
      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountHesabsSharedCollection).toContain(parentAccountId);
      expect(comp.personnelSharedCollection).toContain(personnelId);
      expect(comp.banksSharedCollection).toContain(bank);
      expect(comp.accountHesab).toEqual(accountHesab);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountHesab>>();
      const accountHesab = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountHesabFormService, 'getAccountHesab').mockReturnValue(accountHesab);
      jest.spyOn(accountHesabService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountHesab }));
      saveSubject.complete();

      // THEN
      expect(accountHesabFormService.getAccountHesab).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountHesabService.update).toHaveBeenCalledWith(expect.objectContaining(accountHesab));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountHesab>>();
      const accountHesab = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountHesabFormService, 'getAccountHesab').mockReturnValue({ id: null });
      jest.spyOn(accountHesabService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountHesab: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountHesab }));
      saveSubject.complete();

      // THEN
      expect(accountHesabFormService.getAccountHesab).toHaveBeenCalled();
      expect(accountHesabService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountHesab>>();
      const accountHesab = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(accountHesabService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountHesab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountHesabService.update).toHaveBeenCalled();
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

    describe('compareAccountHesab', () => {
      it('Should forward to accountHesabService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(accountHesabService, 'compareAccountHesab');
        comp.compareAccountHesab(entity, entity2);
        expect(accountHesabService.compareAccountHesab).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareBank', () => {
      it('Should forward to bankService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(bankService, 'compareBank');
        comp.compareBank(entity, entity2);
        expect(bankService.compareBank).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
