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
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { AccountHesabService } from 'app/entities/BulutAttendance/account-hesab/service/account-hesab.service';
import { ITransaction } from 'app/entities/BulutAttendance/transaction/transaction.model';
import { TransactionService } from 'app/entities/BulutAttendance/transaction/service/transaction.service';
import { ITransactionAccount } from '../transaction-account.model';
import { TransactionAccountService } from '../service/transaction-account.service';
import { TransactionAccountFormService } from './transaction-account-form.service';

import { TransactionAccountUpdateComponent } from './transaction-account-update.component';

describe('TransactionAccount Management Update Component', () => {
  let comp: TransactionAccountUpdateComponent;
  let fixture: ComponentFixture<TransactionAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionAccountFormService: TransactionAccountFormService;
  let transactionAccountService: TransactionAccountService;
  let applicationUserService: ApplicationUserService;
  let companyService: CompanyService;
  let accountHesabService: AccountHesabService;
  let transactionService: TransactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TransactionAccountUpdateComponent],
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
      .overrideTemplate(TransactionAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionAccountFormService = TestBed.inject(TransactionAccountFormService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    companyService = TestBed.inject(CompanyService);
    accountHesabService = TestBed.inject(AccountHesabService);
    transactionService = TestBed.inject(TransactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const internalUser: IApplicationUser = { id: 15091 };
      transactionAccount.internalUser = internalUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 32 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [internalUser];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const company: ICompany = { id: 8188 };
      transactionAccount.company = company;

      const companyCollection: ICompany[] = [{ id: 10684 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountHesab query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const account: IAccountHesab = { id: '8a833888-18c7-4a91-9738-dbaa91f76b29' };
      transactionAccount.account = account;

      const accountHesabCollection: IAccountHesab[] = [{ id: '8b45849d-5d30-4d11-a316-c4772bae9882' }];
      jest.spyOn(accountHesabService, 'query').mockReturnValue(of(new HttpResponse({ body: accountHesabCollection })));
      const additionalAccountHesabs = [account];
      const expectedCollection: IAccountHesab[] = [...additionalAccountHesabs, ...accountHesabCollection];
      jest.spyOn(accountHesabService, 'addAccountHesabToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(accountHesabService.query).toHaveBeenCalled();
      expect(accountHesabService.addAccountHesabToCollectionIfMissing).toHaveBeenCalledWith(
        accountHesabCollection,
        ...additionalAccountHesabs.map(expect.objectContaining),
      );
      expect(comp.accountHesabsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transaction query and add missing value', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const transaction: ITransaction = { id: 1686 };
      transactionAccount.transaction = transaction;

      const transactionCollection: ITransaction[] = [{ id: 11105 }];
      jest.spyOn(transactionService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionCollection })));
      const additionalTransactions = [transaction];
      const expectedCollection: ITransaction[] = [...additionalTransactions, ...transactionCollection];
      jest.spyOn(transactionService, 'addTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(transactionService.query).toHaveBeenCalled();
      expect(transactionService.addTransactionToCollectionIfMissing).toHaveBeenCalledWith(
        transactionCollection,
        ...additionalTransactions.map(expect.objectContaining),
      );
      expect(comp.transactionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionAccount: ITransactionAccount = { id: 456 };
      const internalUser: IApplicationUser = { id: 21021 };
      transactionAccount.internalUser = internalUser;
      const company: ICompany = { id: 1718 };
      transactionAccount.company = company;
      const account: IAccountHesab = { id: 'd67db54a-0f0b-4960-af74-a8704b4926a8' };
      transactionAccount.account = account;
      const transaction: ITransaction = { id: 10214 };
      transactionAccount.transaction = transaction;

      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(internalUser);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.accountHesabsSharedCollection).toContain(account);
      expect(comp.transactionsSharedCollection).toContain(transaction);
      expect(comp.transactionAccount).toEqual(transactionAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionAccount>>();
      const transactionAccount = { id: 123 };
      jest.spyOn(transactionAccountFormService, 'getTransactionAccount').mockReturnValue(transactionAccount);
      jest.spyOn(transactionAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccount }));
      saveSubject.complete();

      // THEN
      expect(transactionAccountFormService.getTransactionAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionAccountService.update).toHaveBeenCalledWith(expect.objectContaining(transactionAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionAccount>>();
      const transactionAccount = { id: 123 };
      jest.spyOn(transactionAccountFormService, 'getTransactionAccount').mockReturnValue({ id: null });
      jest.spyOn(transactionAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionAccount }));
      saveSubject.complete();

      // THEN
      expect(transactionAccountFormService.getTransactionAccount).toHaveBeenCalled();
      expect(transactionAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransactionAccount>>();
      const transactionAccount = { id: 123 };
      jest.spyOn(transactionAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionAccountService.update).toHaveBeenCalled();
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

    describe('compareAccountHesab', () => {
      it('Should forward to accountHesabService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(accountHesabService, 'compareAccountHesab');
        comp.compareAccountHesab(entity, entity2);
        expect(accountHesabService.compareAccountHesab).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransaction', () => {
      it('Should forward to transactionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionService, 'compareTransaction');
        comp.compareTransaction(entity, entity2);
        expect(transactionService.compareTransaction).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
