import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgUnit } from '../org-unit.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../org-unit.test-samples';

import { OrgUnitService } from './org-unit.service';

const requireRestSample: IOrgUnit = {
  ...sampleWithRequiredData,
};

describe('OrgUnit Service', () => {
  let service: OrgUnitService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrgUnit | IOrgUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgUnitService);
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

    it('should create a OrgUnit', () => {
      const orgUnit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orgUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrgUnit', () => {
      const orgUnit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orgUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrgUnit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrgUnit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrgUnit', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a OrgUnit', () => {
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

    describe('addOrgUnitToCollectionIfMissing', () => {
      it('should add a OrgUnit to an empty array', () => {
        const orgUnit: IOrgUnit = sampleWithRequiredData;
        expectedResult = service.addOrgUnitToCollectionIfMissing([], orgUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should not add a OrgUnit to an array that contains it', () => {
        const orgUnit: IOrgUnit = sampleWithRequiredData;
        const orgUnitCollection: IOrgUnit[] = [
          {
            ...orgUnit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, orgUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrgUnit to an array that doesn't contain it", () => {
        const orgUnit: IOrgUnit = sampleWithRequiredData;
        const orgUnitCollection: IOrgUnit[] = [sampleWithPartialData];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, orgUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should add only unique OrgUnit to an array', () => {
        const orgUnitArray: IOrgUnit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orgUnitCollection: IOrgUnit[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, ...orgUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orgUnit: IOrgUnit = sampleWithRequiredData;
        const orgUnit2: IOrgUnit = sampleWithPartialData;
        expectedResult = service.addOrgUnitToCollectionIfMissing([], orgUnit, orgUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUnit);
        expect(expectedResult).toContain(orgUnit2);
      });

      it('should accept null and undefined values', () => {
        const orgUnit: IOrgUnit = sampleWithRequiredData;
        expectedResult = service.addOrgUnitToCollectionIfMissing([], null, orgUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should return initial array if no OrgUnit is added', () => {
        const orgUnitCollection: IOrgUnit[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, undefined, null);
        expect(expectedResult).toEqual(orgUnitCollection);
      });
    });

    describe('compareOrgUnit', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrgUnit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareOrgUnit(entity1, entity2);
        const compareResult2 = service.compareOrgUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareOrgUnit(entity1, entity2);
        const compareResult2 = service.compareOrgUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareOrgUnit(entity1, entity2);
        const compareResult2 = service.compareOrgUnit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
