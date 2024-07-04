import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHrLetterParameter } from '../hr-letter-parameter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hr-letter-parameter.test-samples';

import { HrLetterParameterService } from './hr-letter-parameter.service';

const requireRestSample: IHrLetterParameter = {
  ...sampleWithRequiredData,
};

describe('HrLetterParameter Service', () => {
  let service: HrLetterParameterService;
  let httpMock: HttpTestingController;
  let expectedResult: IHrLetterParameter | IHrLetterParameter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HrLetterParameterService);
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

    it('should create a HrLetterParameter', () => {
      const hrLetterParameter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hrLetterParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HrLetterParameter', () => {
      const hrLetterParameter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hrLetterParameter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HrLetterParameter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HrLetterParameter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HrLetterParameter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a HrLetterParameter', () => {
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

    describe('addHrLetterParameterToCollectionIfMissing', () => {
      it('should add a HrLetterParameter to an empty array', () => {
        const hrLetterParameter: IHrLetterParameter = sampleWithRequiredData;
        expectedResult = service.addHrLetterParameterToCollectionIfMissing([], hrLetterParameter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hrLetterParameter);
      });

      it('should not add a HrLetterParameter to an array that contains it', () => {
        const hrLetterParameter: IHrLetterParameter = sampleWithRequiredData;
        const hrLetterParameterCollection: IHrLetterParameter[] = [
          {
            ...hrLetterParameter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHrLetterParameterToCollectionIfMissing(hrLetterParameterCollection, hrLetterParameter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HrLetterParameter to an array that doesn't contain it", () => {
        const hrLetterParameter: IHrLetterParameter = sampleWithRequiredData;
        const hrLetterParameterCollection: IHrLetterParameter[] = [sampleWithPartialData];
        expectedResult = service.addHrLetterParameterToCollectionIfMissing(hrLetterParameterCollection, hrLetterParameter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hrLetterParameter);
      });

      it('should add only unique HrLetterParameter to an array', () => {
        const hrLetterParameterArray: IHrLetterParameter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hrLetterParameterCollection: IHrLetterParameter[] = [sampleWithRequiredData];
        expectedResult = service.addHrLetterParameterToCollectionIfMissing(hrLetterParameterCollection, ...hrLetterParameterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hrLetterParameter: IHrLetterParameter = sampleWithRequiredData;
        const hrLetterParameter2: IHrLetterParameter = sampleWithPartialData;
        expectedResult = service.addHrLetterParameterToCollectionIfMissing([], hrLetterParameter, hrLetterParameter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hrLetterParameter);
        expect(expectedResult).toContain(hrLetterParameter2);
      });

      it('should accept null and undefined values', () => {
        const hrLetterParameter: IHrLetterParameter = sampleWithRequiredData;
        expectedResult = service.addHrLetterParameterToCollectionIfMissing([], null, hrLetterParameter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hrLetterParameter);
      });

      it('should return initial array if no HrLetterParameter is added', () => {
        const hrLetterParameterCollection: IHrLetterParameter[] = [sampleWithRequiredData];
        expectedResult = service.addHrLetterParameterToCollectionIfMissing(hrLetterParameterCollection, undefined, null);
        expect(expectedResult).toEqual(hrLetterParameterCollection);
      });
    });

    describe('compareHrLetterParameter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHrLetterParameter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHrLetterParameter(entity1, entity2);
        const compareResult2 = service.compareHrLetterParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHrLetterParameter(entity1, entity2);
        const compareResult2 = service.compareHrLetterParameter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHrLetterParameter(entity1, entity2);
        const compareResult2 = service.compareHrLetterParameter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
