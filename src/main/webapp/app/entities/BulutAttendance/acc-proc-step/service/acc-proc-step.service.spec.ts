import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccProcStep } from '../acc-proc-step.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../acc-proc-step.test-samples';

import { AccProcStepService } from './acc-proc-step.service';

const requireRestSample: IAccProcStep = {
  ...sampleWithRequiredData,
};

describe('AccProcStep Service', () => {
  let service: AccProcStepService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccProcStep | IAccProcStep[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccProcStepService);
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

    it('should create a AccProcStep', () => {
      const accProcStep = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accProcStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccProcStep', () => {
      const accProcStep = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accProcStep).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccProcStep', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccProcStep', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccProcStep', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccProcStep', () => {
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

    describe('addAccProcStepToCollectionIfMissing', () => {
      it('should add a AccProcStep to an empty array', () => {
        const accProcStep: IAccProcStep = sampleWithRequiredData;
        expectedResult = service.addAccProcStepToCollectionIfMissing([], accProcStep);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accProcStep);
      });

      it('should not add a AccProcStep to an array that contains it', () => {
        const accProcStep: IAccProcStep = sampleWithRequiredData;
        const accProcStepCollection: IAccProcStep[] = [
          {
            ...accProcStep,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccProcStepToCollectionIfMissing(accProcStepCollection, accProcStep);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccProcStep to an array that doesn't contain it", () => {
        const accProcStep: IAccProcStep = sampleWithRequiredData;
        const accProcStepCollection: IAccProcStep[] = [sampleWithPartialData];
        expectedResult = service.addAccProcStepToCollectionIfMissing(accProcStepCollection, accProcStep);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accProcStep);
      });

      it('should add only unique AccProcStep to an array', () => {
        const accProcStepArray: IAccProcStep[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accProcStepCollection: IAccProcStep[] = [sampleWithRequiredData];
        expectedResult = service.addAccProcStepToCollectionIfMissing(accProcStepCollection, ...accProcStepArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accProcStep: IAccProcStep = sampleWithRequiredData;
        const accProcStep2: IAccProcStep = sampleWithPartialData;
        expectedResult = service.addAccProcStepToCollectionIfMissing([], accProcStep, accProcStep2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accProcStep);
        expect(expectedResult).toContain(accProcStep2);
      });

      it('should accept null and undefined values', () => {
        const accProcStep: IAccProcStep = sampleWithRequiredData;
        expectedResult = service.addAccProcStepToCollectionIfMissing([], null, accProcStep, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accProcStep);
      });

      it('should return initial array if no AccProcStep is added', () => {
        const accProcStepCollection: IAccProcStep[] = [sampleWithRequiredData];
        expectedResult = service.addAccProcStepToCollectionIfMissing(accProcStepCollection, undefined, null);
        expect(expectedResult).toEqual(accProcStepCollection);
      });
    });

    describe('compareAccProcStep', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccProcStep(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAccProcStep(entity1, entity2);
        const compareResult2 = service.compareAccProcStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAccProcStep(entity1, entity2);
        const compareResult2 = service.compareAccProcStep(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAccProcStep(entity1, entity2);
        const compareResult2 = service.compareAccProcStep(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
