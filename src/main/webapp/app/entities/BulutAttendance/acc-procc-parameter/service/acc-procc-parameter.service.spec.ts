import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccProccParameter } from '../acc-procc-parameter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../acc-procc-parameter.test-samples';

import { AccProccParameterService } from './acc-procc-parameter.service';

const requireRestSample: IAccProccParameter = {
  ...sampleWithRequiredData,
};

describe('AccProccParameter Service', () => {
  let service: AccProccParameterService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccProccParameter | IAccProccParameter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccProccParameterService);
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

    it('should create a AccProccParameter', () => {
      const accProccParameter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accProccParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccProccParameter', () => {
      const accProccParameter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accProccParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccProccParameter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccProccParameter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccProccParameter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccProccParameter', () => {
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

    describe('addAccProccParameterToCollectionIfMissing', () => {
      it('should add a AccProccParameter to an empty array', () => {
        const accProccParameter: IAccProccParameter = sampleWithRequiredData;
        expectedResult = service.addAccProccParameterToCollectionIfMissing([], accProccParameter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accProccParameter);
      });

      it('should not add a AccProccParameter to an array that contains it', () => {
        const accProccParameter: IAccProccParameter = sampleWithRequiredData;
        const accProccParameterCollection: IAccProccParameter[] = [
          {
            ...accProccParameter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccProccParameterToCollectionIfMissing(accProccParameterCollection, accProccParameter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccProccParameter to an array that doesn't contain it", () => {
        const accProccParameter: IAccProccParameter = sampleWithRequiredData;
        const accProccParameterCollection: IAccProccParameter[] = [sampleWithPartialData];
        expectedResult = service.addAccProccParameterToCollectionIfMissing(accProccParameterCollection, accProccParameter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accProccParameter);
      });

      it('should add only unique AccProccParameter to an array', () => {
        const accProccParameterArray: IAccProccParameter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accProccParameterCollection: IAccProccParameter[] = [sampleWithRequiredData];
        expectedResult = service.addAccProccParameterToCollectionIfMissing(accProccParameterCollection, ...accProccParameterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accProccParameter: IAccProccParameter = sampleWithRequiredData;
        const accProccParameter2: IAccProccParameter = sampleWithPartialData;
        expectedResult = service.addAccProccParameterToCollectionIfMissing([], accProccParameter, accProccParameter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accProccParameter);
        expect(expectedResult).toContain(accProccParameter2);
      });

      it('should accept null and undefined values', () => {
        const accProccParameter: IAccProccParameter = sampleWithRequiredData;
        expectedResult = service.addAccProccParameterToCollectionIfMissing([], null, accProccParameter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accProccParameter);
      });

      it('should return initial array if no AccProccParameter is added', () => {
        const accProccParameterCollection: IAccProccParameter[] = [sampleWithRequiredData];
        expectedResult = service.addAccProccParameterToCollectionIfMissing(accProccParameterCollection, undefined, null);
        expect(expectedResult).toEqual(accProccParameterCollection);
      });
    });

    describe('compareAccProccParameter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccProccParameter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccProccParameter(entity1, entity2);
        const compareResult2 = service.compareAccProccParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccProccParameter(entity1, entity2);
        const compareResult2 = service.compareAccProccParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccProccParameter(entity1, entity2);
        const compareResult2 = service.compareAccProccParameter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
