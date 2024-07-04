import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgPosition } from '../org-position.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../org-position.test-samples';

import { OrgPositionService } from './org-position.service';

const requireRestSample: IOrgPosition = {
  ...sampleWithRequiredData,
};

describe('OrgPosition Service', () => {
  let service: OrgPositionService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrgPosition | IOrgPosition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgPositionService);
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

    it('should create a OrgPosition', () => {
      const orgPosition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orgPosition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrgPosition', () => {
      const orgPosition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orgPosition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrgPosition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrgPosition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrgPosition', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a OrgPosition', () => {
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

    describe('addOrgPositionToCollectionIfMissing', () => {
      it('should add a OrgPosition to an empty array', () => {
        const orgPosition: IOrgPosition = sampleWithRequiredData;
        expectedResult = service.addOrgPositionToCollectionIfMissing([], orgPosition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgPosition);
      });

      it('should not add a OrgPosition to an array that contains it', () => {
        const orgPosition: IOrgPosition = sampleWithRequiredData;
        const orgPositionCollection: IOrgPosition[] = [
          {
            ...orgPosition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrgPositionToCollectionIfMissing(orgPositionCollection, orgPosition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrgPosition to an array that doesn't contain it", () => {
        const orgPosition: IOrgPosition = sampleWithRequiredData;
        const orgPositionCollection: IOrgPosition[] = [sampleWithPartialData];
        expectedResult = service.addOrgPositionToCollectionIfMissing(orgPositionCollection, orgPosition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgPosition);
      });

      it('should add only unique OrgPosition to an array', () => {
        const orgPositionArray: IOrgPosition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orgPositionCollection: IOrgPosition[] = [sampleWithRequiredData];
        expectedResult = service.addOrgPositionToCollectionIfMissing(orgPositionCollection, ...orgPositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orgPosition: IOrgPosition = sampleWithRequiredData;
        const orgPosition2: IOrgPosition = sampleWithPartialData;
        expectedResult = service.addOrgPositionToCollectionIfMissing([], orgPosition, orgPosition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgPosition);
        expect(expectedResult).toContain(orgPosition2);
      });

      it('should accept null and undefined values', () => {
        const orgPosition: IOrgPosition = sampleWithRequiredData;
        expectedResult = service.addOrgPositionToCollectionIfMissing([], null, orgPosition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgPosition);
      });

      it('should return initial array if no OrgPosition is added', () => {
        const orgPositionCollection: IOrgPosition[] = [sampleWithRequiredData];
        expectedResult = service.addOrgPositionToCollectionIfMissing(orgPositionCollection, undefined, null);
        expect(expectedResult).toEqual(orgPositionCollection);
      });
    });

    describe('compareOrgPosition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrgPosition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareOrgPosition(entity1, entity2);
        const compareResult2 = service.compareOrgPosition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareOrgPosition(entity1, entity2);
        const compareResult2 = service.compareOrgPosition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareOrgPosition(entity1, entity2);
        const compareResult2 = service.compareOrgPosition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
