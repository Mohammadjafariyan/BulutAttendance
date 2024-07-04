import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountingProcedure } from '../accounting-procedure.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../accounting-procedure.test-samples';

import { AccountingProcedureService } from './accounting-procedure.service';

const requireRestSample: IAccountingProcedure = {
  ...sampleWithRequiredData,
};

describe('AccountingProcedure Service', () => {
  let service: AccountingProcedureService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountingProcedure | IAccountingProcedure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountingProcedureService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AccountingProcedure', () => {
      const accountingProcedure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountingProcedure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountingProcedure', () => {
      const accountingProcedure = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountingProcedure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountingProcedure', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountingProcedure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountingProcedure', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccountingProcedure', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addAccountingProcedureToCollectionIfMissing', () => {
      it('should add a AccountingProcedure to an empty array', () => {
        const accountingProcedure: IAccountingProcedure = sampleWithRequiredData;
        expectedResult = service.addAccountingProcedureToCollectionIfMissing([], accountingProcedure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountingProcedure);
      });

      it('should not add a AccountingProcedure to an array that contains it', () => {
        const accountingProcedure: IAccountingProcedure = sampleWithRequiredData;
        const accountingProcedureCollection: IAccountingProcedure[] = [
          {
            ...accountingProcedure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountingProcedureToCollectionIfMissing(accountingProcedureCollection, accountingProcedure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountingProcedure to an array that doesn't contain it", () => {
        const accountingProcedure: IAccountingProcedure = sampleWithRequiredData;
        const accountingProcedureCollection: IAccountingProcedure[] = [sampleWithPartialData];
        expectedResult = service.addAccountingProcedureToCollectionIfMissing(accountingProcedureCollection, accountingProcedure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountingProcedure);
      });

      it('should add only unique AccountingProcedure to an array', () => {
        const accountingProcedureArray: IAccountingProcedure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accountingProcedureCollection: IAccountingProcedure[] = [sampleWithRequiredData];
        expectedResult = service.addAccountingProcedureToCollectionIfMissing(accountingProcedureCollection, ...accountingProcedureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountingProcedure: IAccountingProcedure = sampleWithRequiredData;
        const accountingProcedure2: IAccountingProcedure = sampleWithPartialData;
        expectedResult = service.addAccountingProcedureToCollectionIfMissing([], accountingProcedure, accountingProcedure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountingProcedure);
        expect(expectedResult).toContain(accountingProcedure2);
      });

      it('should accept null and undefined values', () => {
        const accountingProcedure: IAccountingProcedure = sampleWithRequiredData;
        expectedResult = service.addAccountingProcedureToCollectionIfMissing([], null, accountingProcedure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountingProcedure);
      });

      it('should return initial array if no AccountingProcedure is added', () => {
        const accountingProcedureCollection: IAccountingProcedure[] = [sampleWithRequiredData];
        expectedResult = service.addAccountingProcedureToCollectionIfMissing(accountingProcedureCollection, undefined, null);
        expect(expectedResult).toEqual(accountingProcedureCollection);
      });
    });

    describe('compareAccountingProcedure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountingProcedure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccountingProcedure(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccountingProcedure(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccountingProcedure(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
