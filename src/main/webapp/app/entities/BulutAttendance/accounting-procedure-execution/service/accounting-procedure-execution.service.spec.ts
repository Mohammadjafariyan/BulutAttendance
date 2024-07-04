import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../accounting-procedure-execution.test-samples';

import { AccountingProcedureExecutionService, RestAccountingProcedureExecution } from './accounting-procedure-execution.service';

const requireRestSample: RestAccountingProcedureExecution = {
  ...sampleWithRequiredData,
  dateTime: sampleWithRequiredData.dateTime?.toJSON(),
};

describe('AccountingProcedureExecution Service', () => {
  let service: AccountingProcedureExecutionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountingProcedureExecution | IAccountingProcedureExecution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountingProcedureExecutionService);
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

    it('should create a AccountingProcedureExecution', () => {
      const accountingProcedureExecution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountingProcedureExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountingProcedureExecution', () => {
      const accountingProcedureExecution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountingProcedureExecution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountingProcedureExecution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountingProcedureExecution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountingProcedureExecution', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccountingProcedureExecution', () => {
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

    describe('addAccountingProcedureExecutionToCollectionIfMissing', () => {
      it('should add a AccountingProcedureExecution to an empty array', () => {
        const accountingProcedureExecution: IAccountingProcedureExecution = sampleWithRequiredData;
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing([], accountingProcedureExecution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountingProcedureExecution);
      });

      it('should not add a AccountingProcedureExecution to an array that contains it', () => {
        const accountingProcedureExecution: IAccountingProcedureExecution = sampleWithRequiredData;
        const accountingProcedureExecutionCollection: IAccountingProcedureExecution[] = [
          {
            ...accountingProcedureExecution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing(
          accountingProcedureExecutionCollection,
          accountingProcedureExecution,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountingProcedureExecution to an array that doesn't contain it", () => {
        const accountingProcedureExecution: IAccountingProcedureExecution = sampleWithRequiredData;
        const accountingProcedureExecutionCollection: IAccountingProcedureExecution[] = [sampleWithPartialData];
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing(
          accountingProcedureExecutionCollection,
          accountingProcedureExecution,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountingProcedureExecution);
      });

      it('should add only unique AccountingProcedureExecution to an array', () => {
        const accountingProcedureExecutionArray: IAccountingProcedureExecution[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const accountingProcedureExecutionCollection: IAccountingProcedureExecution[] = [sampleWithRequiredData];
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing(
          accountingProcedureExecutionCollection,
          ...accountingProcedureExecutionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountingProcedureExecution: IAccountingProcedureExecution = sampleWithRequiredData;
        const accountingProcedureExecution2: IAccountingProcedureExecution = sampleWithPartialData;
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing(
          [],
          accountingProcedureExecution,
          accountingProcedureExecution2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountingProcedureExecution);
        expect(expectedResult).toContain(accountingProcedureExecution2);
      });

      it('should accept null and undefined values', () => {
        const accountingProcedureExecution: IAccountingProcedureExecution = sampleWithRequiredData;
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing([], null, accountingProcedureExecution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountingProcedureExecution);
      });

      it('should return initial array if no AccountingProcedureExecution is added', () => {
        const accountingProcedureExecutionCollection: IAccountingProcedureExecution[] = [sampleWithRequiredData];
        expectedResult = service.addAccountingProcedureExecutionToCollectionIfMissing(
          accountingProcedureExecutionCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(accountingProcedureExecutionCollection);
      });
    });

    describe('compareAccountingProcedureExecution', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountingProcedureExecution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccountingProcedureExecution(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedureExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccountingProcedureExecution(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedureExecution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccountingProcedureExecution(entity1, entity2);
        const compareResult2 = service.compareAccountingProcedureExecution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
