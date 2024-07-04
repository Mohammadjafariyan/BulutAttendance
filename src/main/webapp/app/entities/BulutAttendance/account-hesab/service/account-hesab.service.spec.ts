import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountHesab } from '../account-hesab.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../account-hesab.test-samples';

import { AccountHesabService } from './account-hesab.service';

const requireRestSample: IAccountHesab = {
  ...sampleWithRequiredData,
};

describe('AccountHesab Service', () => {
  let service: AccountHesabService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountHesab | IAccountHesab[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountHesabService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AccountHesab', () => {
      const accountHesab = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountHesab).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountHesab', () => {
      const accountHesab = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountHesab).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountHesab', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountHesab', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountHesab', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccountHesab', () => {
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

    describe('addAccountHesabToCollectionIfMissing', () => {
      it('should add a AccountHesab to an empty array', () => {
        const accountHesab: IAccountHesab = sampleWithRequiredData;
        expectedResult = service.addAccountHesabToCollectionIfMissing([], accountHesab);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountHesab);
      });

      it('should not add a AccountHesab to an array that contains it', () => {
        const accountHesab: IAccountHesab = sampleWithRequiredData;
        const accountHesabCollection: IAccountHesab[] = [
          {
            ...accountHesab,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountHesabToCollectionIfMissing(accountHesabCollection, accountHesab);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountHesab to an array that doesn't contain it", () => {
        const accountHesab: IAccountHesab = sampleWithRequiredData;
        const accountHesabCollection: IAccountHesab[] = [sampleWithPartialData];
        expectedResult = service.addAccountHesabToCollectionIfMissing(accountHesabCollection, accountHesab);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountHesab);
      });

      it('should add only unique AccountHesab to an array', () => {
        const accountHesabArray: IAccountHesab[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accountHesabCollection: IAccountHesab[] = [sampleWithRequiredData];
        expectedResult = service.addAccountHesabToCollectionIfMissing(accountHesabCollection, ...accountHesabArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountHesab: IAccountHesab = sampleWithRequiredData;
        const accountHesab2: IAccountHesab = sampleWithPartialData;
        expectedResult = service.addAccountHesabToCollectionIfMissing([], accountHesab, accountHesab2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountHesab);
        expect(expectedResult).toContain(accountHesab2);
      });

      it('should accept null and undefined values', () => {
        const accountHesab: IAccountHesab = sampleWithRequiredData;
        expectedResult = service.addAccountHesabToCollectionIfMissing([], null, accountHesab, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountHesab);
      });

      it('should return initial array if no AccountHesab is added', () => {
        const accountHesabCollection: IAccountHesab[] = [sampleWithRequiredData];
        expectedResult = service.addAccountHesabToCollectionIfMissing(accountHesabCollection, undefined, null);
        expect(expectedResult).toEqual(accountHesabCollection);
      });
    });

    describe('compareAccountHesab', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountHesab(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareAccountHesab(entity1, entity2);
        const compareResult2 = service.compareAccountHesab(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareAccountHesab(entity1, entity2);
        const compareResult2 = service.compareAccountHesab(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareAccountHesab(entity1, entity2);
        const compareResult2 = service.compareAccountHesab(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
