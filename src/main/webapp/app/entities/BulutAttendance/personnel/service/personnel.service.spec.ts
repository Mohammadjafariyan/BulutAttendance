import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonnel } from '../personnel.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../personnel.test-samples';

import { PersonnelService } from './personnel.service';

const requireRestSample: IPersonnel = {
  ...sampleWithRequiredData,
};

describe('Personnel Service', () => {
  let service: PersonnelService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonnel | IPersonnel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonnelService);
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

    it('should create a Personnel', () => {
      const personnel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personnel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Personnel', () => {
      const personnel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personnel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Personnel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Personnel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Personnel', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Personnel', () => {
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

    describe('addPersonnelToCollectionIfMissing', () => {
      it('should add a Personnel to an empty array', () => {
        const personnel: IPersonnel = sampleWithRequiredData;
        expectedResult = service.addPersonnelToCollectionIfMissing([], personnel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnel);
      });

      it('should not add a Personnel to an array that contains it', () => {
        const personnel: IPersonnel = sampleWithRequiredData;
        const personnelCollection: IPersonnel[] = [
          {
            ...personnel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, personnel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Personnel to an array that doesn't contain it", () => {
        const personnel: IPersonnel = sampleWithRequiredData;
        const personnelCollection: IPersonnel[] = [sampleWithPartialData];
        expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, personnel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnel);
      });

      it('should add only unique Personnel to an array', () => {
        const personnelArray: IPersonnel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personnelCollection: IPersonnel[] = [sampleWithRequiredData];
        expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, ...personnelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personnel: IPersonnel = sampleWithRequiredData;
        const personnel2: IPersonnel = sampleWithPartialData;
        expectedResult = service.addPersonnelToCollectionIfMissing([], personnel, personnel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personnel);
        expect(expectedResult).toContain(personnel2);
      });

      it('should accept null and undefined values', () => {
        const personnel: IPersonnel = sampleWithRequiredData;
        expectedResult = service.addPersonnelToCollectionIfMissing([], null, personnel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personnel);
      });

      it('should return initial array if no Personnel is added', () => {
        const personnelCollection: IPersonnel[] = [sampleWithRequiredData];
        expectedResult = service.addPersonnelToCollectionIfMissing(personnelCollection, undefined, null);
        expect(expectedResult).toEqual(personnelCollection);
      });
    });

    describe('comparePersonnel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonnel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.comparePersonnel(entity1, entity2);
        const compareResult2 = service.comparePersonnel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.comparePersonnel(entity1, entity2);
        const compareResult2 = service.comparePersonnel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.comparePersonnel(entity1, entity2);
        const compareResult2 = service.comparePersonnel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
