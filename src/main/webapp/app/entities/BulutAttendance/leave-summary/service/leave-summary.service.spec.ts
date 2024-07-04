import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeaveSummary } from '../leave-summary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../leave-summary.test-samples';

import { LeaveSummaryService } from './leave-summary.service';

const requireRestSample: ILeaveSummary = {
  ...sampleWithRequiredData,
};

describe('LeaveSummary Service', () => {
  let service: LeaveSummaryService;
  let httpMock: HttpTestingController;
  let expectedResult: ILeaveSummary | ILeaveSummary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaveSummaryService);
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

    it('should create a LeaveSummary', () => {
      const leaveSummary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(leaveSummary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaveSummary', () => {
      const leaveSummary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(leaveSummary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaveSummary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaveSummary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LeaveSummary', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a LeaveSummary', () => {
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

    describe('addLeaveSummaryToCollectionIfMissing', () => {
      it('should add a LeaveSummary to an empty array', () => {
        const leaveSummary: ILeaveSummary = sampleWithRequiredData;
        expectedResult = service.addLeaveSummaryToCollectionIfMissing([], leaveSummary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveSummary);
      });

      it('should not add a LeaveSummary to an array that contains it', () => {
        const leaveSummary: ILeaveSummary = sampleWithRequiredData;
        const leaveSummaryCollection: ILeaveSummary[] = [
          {
            ...leaveSummary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLeaveSummaryToCollectionIfMissing(leaveSummaryCollection, leaveSummary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaveSummary to an array that doesn't contain it", () => {
        const leaveSummary: ILeaveSummary = sampleWithRequiredData;
        const leaveSummaryCollection: ILeaveSummary[] = [sampleWithPartialData];
        expectedResult = service.addLeaveSummaryToCollectionIfMissing(leaveSummaryCollection, leaveSummary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveSummary);
      });

      it('should add only unique LeaveSummary to an array', () => {
        const leaveSummaryArray: ILeaveSummary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const leaveSummaryCollection: ILeaveSummary[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveSummaryToCollectionIfMissing(leaveSummaryCollection, ...leaveSummaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaveSummary: ILeaveSummary = sampleWithRequiredData;
        const leaveSummary2: ILeaveSummary = sampleWithPartialData;
        expectedResult = service.addLeaveSummaryToCollectionIfMissing([], leaveSummary, leaveSummary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaveSummary);
        expect(expectedResult).toContain(leaveSummary2);
      });

      it('should accept null and undefined values', () => {
        const leaveSummary: ILeaveSummary = sampleWithRequiredData;
        expectedResult = service.addLeaveSummaryToCollectionIfMissing([], null, leaveSummary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaveSummary);
      });

      it('should return initial array if no LeaveSummary is added', () => {
        const leaveSummaryCollection: ILeaveSummary[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveSummaryToCollectionIfMissing(leaveSummaryCollection, undefined, null);
        expect(expectedResult).toEqual(leaveSummaryCollection);
      });
    });

    describe('compareLeaveSummary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLeaveSummary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareLeaveSummary(entity1, entity2);
        const compareResult2 = service.compareLeaveSummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareLeaveSummary(entity1, entity2);
        const compareResult2 = service.compareLeaveSummary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareLeaveSummary(entity1, entity2);
        const compareResult2 = service.compareLeaveSummary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
