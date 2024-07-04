import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonnelStatus } from '../personnel-status.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../personnel-status.test-samples';

import { PersonnelStatusService } from './personnel-status.service';

const requireRestSample: IPersonnelStatus = {
  ...sampleWithRequiredData,
};

describe('PersonnelStatus Service', () => {
  let service: PersonnelStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonnelStatus | IPersonnelStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonnelStatusService);
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

    it('should create a PersonnelStatus', () => {
      const personnelStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personnelStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonnelStatus', () => {
      const personnelStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personnelStatus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PersonnelStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonnelStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PersonnelStatus', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a PersonnelStatus', () => {
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

    describe('addPersonnelStatusToCollectionIfMissing', () => {
      it('should add a PersonnelStatus to an empty array', () => {
        const personnelStatus: IPersonnelStatus = sampleWithRequiredData;
        expectedResult = service.addPersonnelStatusToCollectionIfMissing([], personnelStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnelStatus);
      });

      it('should not add a PersonnelStatus to an array that contains it', () => {
        const personnelStatus: IPersonnelStatus = sampleWithRequiredData;
        const personnelStatusCollection: IPersonnelStatus[] = [
          {
            ...personnelStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonnelStatusToCollectionIfMissing(personnelStatusCollection, personnelStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonnelStatus to an array that doesn't contain it", () => {
        const personnelStatus: IPersonnelStatus = sampleWithRequiredData;
        const personnelStatusCollection: IPersonnelStatus[] = [sampleWithPartialData];
        expectedResult = service.addPersonnelStatusToCollectionIfMissing(personnelStatusCollection, personnelStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnelStatus);
      });

      it('should add only unique PersonnelStatus to an array', () => {
        const personnelStatusArray: IPersonnelStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personnelStatusCollection: IPersonnelStatus[] = [sampleWithRequiredData];
        expectedResult = service.addPersonnelStatusToCollectionIfMissing(personnelStatusCollection, ...personnelStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personnelStatus: IPersonnelStatus = sampleWithRequiredData;
        const personnelStatus2: IPersonnelStatus = sampleWithPartialData;
        expectedResult = service.addPersonnelStatusToCollectionIfMissing([], personnelStatus, personnelStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnelStatus);
        expect(expectedResult).toContain(personnelStatus2);
      });

      it('should accept null and undefined values', () => {
        const personnelStatus: IPersonnelStatus = sampleWithRequiredData;
        expectedResult = service.addPersonnelStatusToCollectionIfMissing([], null, personnelStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnelStatus);
      });

      it('should return initial array if no PersonnelStatus is added', () => {
        const personnelStatusCollection: IPersonnelStatus[] = [sampleWithRequiredData];
        expectedResult = service.addPersonnelStatusToCollectionIfMissing(personnelStatusCollection, undefined, null);
        expect(expectedResult).toEqual(personnelStatusCollection);
      });
    });

    describe('comparePersonnelStatus', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonnelStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.comparePersonnelStatus(entity1, entity2);
        const compareResult2 = service.comparePersonnelStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.comparePersonnelStatus(entity1, entity2);
        const compareResult2 = service.comparePersonnelStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.comparePersonnelStatus(entity1, entity2);
        const compareResult2 = service.comparePersonnelStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
