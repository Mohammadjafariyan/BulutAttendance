import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeave } from '../leave.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../leave.test-samples';

import { LeaveService, RestLeave } from './leave.service';

const requireRestSample: RestLeave = {
  ...sampleWithRequiredData,
  start: sampleWithRequiredData.start?.toJSON(),
  end: sampleWithRequiredData.end?.toJSON(),
};

describe('Leave Service', () => {
  let service: LeaveService;
  let httpMock: HttpTestingController;
  let expectedResult: ILeave | ILeave[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaveService);
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

    it('should create a Leave', () => {
      const leave = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(leave).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Leave', () => {
      const leave = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(leave).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Leave', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Leave', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Leave', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Leave', () => {
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

    describe('addLeaveToCollectionIfMissing', () => {
      it('should add a Leave to an empty array', () => {
        const leave: ILeave = sampleWithRequiredData;
        expectedResult = service.addLeaveToCollectionIfMissing([], leave);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leave);
      });

      it('should not add a Leave to an array that contains it', () => {
        const leave: ILeave = sampleWithRequiredData;
        const leaveCollection: ILeave[] = [
          {
            ...leave,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, leave);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Leave to an array that doesn't contain it", () => {
        const leave: ILeave = sampleWithRequiredData;
        const leaveCollection: ILeave[] = [sampleWithPartialData];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, leave);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leave);
      });

      it('should add only unique Leave to an array', () => {
        const leaveArray: ILeave[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const leaveCollection: ILeave[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, ...leaveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leave: ILeave = sampleWithRequiredData;
        const leave2: ILeave = sampleWithPartialData;
        expectedResult = service.addLeaveToCollectionIfMissing([], leave, leave2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leave);
        expect(expectedResult).toContain(leave2);
      });

      it('should accept null and undefined values', () => {
        const leave: ILeave = sampleWithRequiredData;
        expectedResult = service.addLeaveToCollectionIfMissing([], null, leave, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leave);
      });

      it('should return initial array if no Leave is added', () => {
        const leaveCollection: ILeave[] = [sampleWithRequiredData];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, undefined, null);
        expect(expectedResult).toEqual(leaveCollection);
      });
    });

    describe('compareLeave', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLeave(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareLeave(entity1, entity2);
        const compareResult2 = service.compareLeave(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareLeave(entity1, entity2);
        const compareResult2 = service.compareLeave(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareLeave(entity1, entity2);
        const compareResult2 = service.compareLeave(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
