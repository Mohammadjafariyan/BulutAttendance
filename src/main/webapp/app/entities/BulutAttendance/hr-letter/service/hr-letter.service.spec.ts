import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHrLetter } from '../hr-letter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hr-letter.test-samples';

import { HrLetterService, RestHrLetter } from './hr-letter.service';

const requireRestSample: RestHrLetter = {
  ...sampleWithRequiredData,
  issueDate: sampleWithRequiredData.issueDate?.toJSON(),
  executionDate: sampleWithRequiredData.executionDate?.toJSON(),
};

describe('HrLetter Service', () => {
  let service: HrLetterService;
  let httpMock: HttpTestingController;
  let expectedResult: IHrLetter | IHrLetter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HrLetterService);
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

    it('should create a HrLetter', () => {
      const hrLetter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hrLetter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HrLetter', () => {
      const hrLetter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hrLetter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HrLetter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HrLetter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HrLetter', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a HrLetter', () => {
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

    describe('addHrLetterToCollectionIfMissing', () => {
      it('should add a HrLetter to an empty array', () => {
        const hrLetter: IHrLetter = sampleWithRequiredData;
        expectedResult = service.addHrLetterToCollectionIfMissing([], hrLetter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hrLetter);
      });

      it('should not add a HrLetter to an array that contains it', () => {
        const hrLetter: IHrLetter = sampleWithRequiredData;
        const hrLetterCollection: IHrLetter[] = [
          {
            ...hrLetter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHrLetterToCollectionIfMissing(hrLetterCollection, hrLetter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HrLetter to an array that doesn't contain it", () => {
        const hrLetter: IHrLetter = sampleWithRequiredData;
        const hrLetterCollection: IHrLetter[] = [sampleWithPartialData];
        expectedResult = service.addHrLetterToCollectionIfMissing(hrLetterCollection, hrLetter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hrLetter);
      });

      it('should add only unique HrLetter to an array', () => {
        const hrLetterArray: IHrLetter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hrLetterCollection: IHrLetter[] = [sampleWithRequiredData];
        expectedResult = service.addHrLetterToCollectionIfMissing(hrLetterCollection, ...hrLetterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hrLetter: IHrLetter = sampleWithRequiredData;
        const hrLetter2: IHrLetter = sampleWithPartialData;
        expectedResult = service.addHrLetterToCollectionIfMissing([], hrLetter, hrLetter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hrLetter);
        expect(expectedResult).toContain(hrLetter2);
      });

      it('should accept null and undefined values', () => {
        const hrLetter: IHrLetter = sampleWithRequiredData;
        expectedResult = service.addHrLetterToCollectionIfMissing([], null, hrLetter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hrLetter);
      });

      it('should return initial array if no HrLetter is added', () => {
        const hrLetterCollection: IHrLetter[] = [sampleWithRequiredData];
        expectedResult = service.addHrLetterToCollectionIfMissing(hrLetterCollection, undefined, null);
        expect(expectedResult).toEqual(hrLetterCollection);
      });
    });

    describe('compareHrLetter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHrLetter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareHrLetter(entity1, entity2);
        const compareResult2 = service.compareHrLetter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareHrLetter(entity1, entity2);
        const compareResult2 = service.compareHrLetter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareHrLetter(entity1, entity2);
        const compareResult2 = service.compareHrLetter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
