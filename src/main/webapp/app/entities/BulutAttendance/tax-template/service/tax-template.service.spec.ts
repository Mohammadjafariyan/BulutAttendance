import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITaxTemplate } from '../tax-template.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tax-template.test-samples';

import { TaxTemplateService } from './tax-template.service';

const requireRestSample: ITaxTemplate = {
  ...sampleWithRequiredData,
};

describe('TaxTemplate Service', () => {
  let service: TaxTemplateService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxTemplate | ITaxTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TaxTemplateService);
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

    it('should create a TaxTemplate', () => {
      const taxTemplate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxTemplate', () => {
      const taxTemplate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxTemplate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaxTemplate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxTemplate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TaxTemplate', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TaxTemplate', () => {
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

    describe('addTaxTemplateToCollectionIfMissing', () => {
      it('should add a TaxTemplate to an empty array', () => {
        const taxTemplate: ITaxTemplate = sampleWithRequiredData;
        expectedResult = service.addTaxTemplateToCollectionIfMissing([], taxTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxTemplate);
      });

      it('should not add a TaxTemplate to an array that contains it', () => {
        const taxTemplate: ITaxTemplate = sampleWithRequiredData;
        const taxTemplateCollection: ITaxTemplate[] = [
          {
            ...taxTemplate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxTemplateToCollectionIfMissing(taxTemplateCollection, taxTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxTemplate to an array that doesn't contain it", () => {
        const taxTemplate: ITaxTemplate = sampleWithRequiredData;
        const taxTemplateCollection: ITaxTemplate[] = [sampleWithPartialData];
        expectedResult = service.addTaxTemplateToCollectionIfMissing(taxTemplateCollection, taxTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxTemplate);
      });

      it('should add only unique TaxTemplate to an array', () => {
        const taxTemplateArray: ITaxTemplate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const taxTemplateCollection: ITaxTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addTaxTemplateToCollectionIfMissing(taxTemplateCollection, ...taxTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxTemplate: ITaxTemplate = sampleWithRequiredData;
        const taxTemplate2: ITaxTemplate = sampleWithPartialData;
        expectedResult = service.addTaxTemplateToCollectionIfMissing([], taxTemplate, taxTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxTemplate);
        expect(expectedResult).toContain(taxTemplate2);
      });

      it('should accept null and undefined values', () => {
        const taxTemplate: ITaxTemplate = sampleWithRequiredData;
        expectedResult = service.addTaxTemplateToCollectionIfMissing([], null, taxTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxTemplate);
      });

      it('should return initial array if no TaxTemplate is added', () => {
        const taxTemplateCollection: ITaxTemplate[] = [sampleWithRequiredData];
        expectedResult = service.addTaxTemplateToCollectionIfMissing(taxTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(taxTemplateCollection);
      });
    });

    describe('compareTaxTemplate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxTemplate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareTaxTemplate(entity1, entity2);
        const compareResult2 = service.compareTaxTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareTaxTemplate(entity1, entity2);
        const compareResult2 = service.compareTaxTemplate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareTaxTemplate(entity1, entity2);
        const compareResult2 = service.compareTaxTemplate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
