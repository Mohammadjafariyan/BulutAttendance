import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountTemplate } from '../account-template.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../account-template.test-samples';

import { AccountTemplateService } from './account-template.service';

const requireRestSample: IAccountTemplate = {
  ...sampleWithRequiredData,
};

describe('AccountTemplate Service', () => {
  let service: AccountTemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountTemplate | IAccountTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountTemplateService);
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

    it('should create a AccountTemplate', () => {
      const accountTemplate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountTemplate', () => {
      const accountTemplate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountTemplate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountTemplate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountTemplate', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a AccountTemplate', () => {
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

    describe('addAccountTemplateToCollectionIfMissing', () => {
      it('should add a AccountTemplate to an empty array', () => {
        const accountTemplate: IAccountTemplate = sampleWithRequiredData;
        expectedResult = service.addAccountTemplateToCollectionIfMissing([], accountTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountTemplate);
      });

      it('should not add a AccountTemplate to an array that contains it', () => {
        const accountTemplate: IAccountTemplate = sampleWithRequiredData;
        const accountTemplateCollection: IAccountTemplate[] = [
          {
            ...accountTemplate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountTemplateToCollectionIfMissing(accountTemplateCollection, accountTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountTemplate to an array that doesn't contain it", () => {
        const accountTemplate: IAccountTemplate = sampleWithRequiredData;
        const accountTemplateCollection: IAccountTemplate[] = [sampleWithPartialData];
        expectedResult = service.addAccountTemplateToCollectionIfMissing(accountTemplateCollection, accountTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountTemplate);
      });

      it('should add only unique AccountTemplate to an array', () => {
        const accountTemplateArray: IAccountTemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accountTemplateCollection: IAccountTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addAccountTemplateToCollectionIfMissing(accountTemplateCollection, ...accountTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountTemplate: IAccountTemplate = sampleWithRequiredData;
        const accountTemplate2: IAccountTemplate = sampleWithPartialData;
        expectedResult = service.addAccountTemplateToCollectionIfMissing([], accountTemplate, accountTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountTemplate);
        expect(expectedResult).toContain(accountTemplate2);
      });

      it('should accept null and undefined values', () => {
        const accountTemplate: IAccountTemplate = sampleWithRequiredData;
        expectedResult = service.addAccountTemplateToCollectionIfMissing([], null, accountTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountTemplate);
      });

      it('should return initial array if no AccountTemplate is added', () => {
        const accountTemplateCollection: IAccountTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addAccountTemplateToCollectionIfMissing(accountTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(accountTemplateCollection);
      });
    });

    describe('compareAccountTemplate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareAccountTemplate(entity1, entity2);
        const compareResult2 = service.compareAccountTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareAccountTemplate(entity1, entity2);
        const compareResult2 = service.compareAccountTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareAccountTemplate(entity1, entity2);
        const compareResult2 = service.compareAccountTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
