import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWork } from '../work.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../work.test-samples';

import { WorkService, RestWork } from './work.service';

const requireRestSample: RestWork = {
  ...sampleWithRequiredData,
  issueDate: sampleWithRequiredData.issueDate?.toJSON(),
};

describe('Work Service', () => {
  let service: WorkService;
  let httpMock: HttpTestingController;
  let expectedResult: IWork | IWork[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkService);
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

    it('should create a Work', () => {
      const work = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(work).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Work', () => {
      const work = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(work).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Work', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Work', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Work', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Work', () => {
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

    describe('addWorkToCollectionIfMissing', () => {
      it('should add a Work to an empty array', () => {
        const work: IWork = sampleWithRequiredData;
        expectedResult = service.addWorkToCollectionIfMissing([], work);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(work);
      });

      it('should not add a Work to an array that contains it', () => {
        const work: IWork = sampleWithRequiredData;
        const workCollection: IWork[] = [
          {
            ...work,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkToCollectionIfMissing(workCollection, work);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Work to an array that doesn't contain it", () => {
        const work: IWork = sampleWithRequiredData;
        const workCollection: IWork[] = [sampleWithPartialData];
        expectedResult = service.addWorkToCollectionIfMissing(workCollection, work);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(work);
      });

      it('should add only unique Work to an array', () => {
        const workArray: IWork[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workCollection: IWork[] = [sampleWithRequiredData];
        expectedResult = service.addWorkToCollectionIfMissing(workCollection, ...workArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const work: IWork = sampleWithRequiredData;
        const work2: IWork = sampleWithPartialData;
        expectedResult = service.addWorkToCollectionIfMissing([], work, work2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(work);
        expect(expectedResult).toContain(work2);
      });

      it('should accept null and undefined values', () => {
        const work: IWork = sampleWithRequiredData;
        expectedResult = service.addWorkToCollectionIfMissing([], null, work, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(work);
      });

      it('should return initial array if no Work is added', () => {
        const workCollection: IWork[] = [sampleWithRequiredData];
        expectedResult = service.addWorkToCollectionIfMissing(workCollection, undefined, null);
        expect(expectedResult).toEqual(workCollection);
      });
    });

    describe('compareWork', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWork(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareWork(entity1, entity2);
        const compareResult2 = service.compareWork(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareWork(entity1, entity2);
        const compareResult2 = service.compareWork(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareWork(entity1, entity2);
        const compareResult2 = service.compareWork(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
