import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISysConfig } from '../sys-config.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sys-config.test-samples';

import { SysConfigService } from './sys-config.service';

const requireRestSample: ISysConfig = {
  ...sampleWithRequiredData,
};

describe('SysConfig Service', () => {
  let service: SysConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: ISysConfig | ISysConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SysConfigService);
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

    it('should create a SysConfig', () => {
      const sysConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sysConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SysConfig', () => {
      const sysConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sysConfig).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SysConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SysConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SysConfig', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a SysConfig', () => {
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

    describe('addSysConfigToCollectionIfMissing', () => {
      it('should add a SysConfig to an empty array', () => {
        const sysConfig: ISysConfig = sampleWithRequiredData;
        expectedResult = service.addSysConfigToCollectionIfMissing([], sysConfig);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sysConfig);
      });

      it('should not add a SysConfig to an array that contains it', () => {
        const sysConfig: ISysConfig = sampleWithRequiredData;
        const sysConfigCollection: ISysConfig[] = [
          {
            ...sysConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSysConfigToCollectionIfMissing(sysConfigCollection, sysConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SysConfig to an array that doesn't contain it", () => {
        const sysConfig: ISysConfig = sampleWithRequiredData;
        const sysConfigCollection: ISysConfig[] = [sampleWithPartialData];
        expectedResult = service.addSysConfigToCollectionIfMissing(sysConfigCollection, sysConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sysConfig);
      });

      it('should add only unique SysConfig to an array', () => {
        const sysConfigArray: ISysConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sysConfigCollection: ISysConfig[] = [sampleWithRequiredData];
        expectedResult = service.addSysConfigToCollectionIfMissing(sysConfigCollection, ...sysConfigArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sysConfig: ISysConfig = sampleWithRequiredData;
        const sysConfig2: ISysConfig = sampleWithPartialData;
        expectedResult = service.addSysConfigToCollectionIfMissing([], sysConfig, sysConfig2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sysConfig);
        expect(expectedResult).toContain(sysConfig2);
      });

      it('should accept null and undefined values', () => {
        const sysConfig: ISysConfig = sampleWithRequiredData;
        expectedResult = service.addSysConfigToCollectionIfMissing([], null, sysConfig, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sysConfig);
      });

      it('should return initial array if no SysConfig is added', () => {
        const sysConfigCollection: ISysConfig[] = [sampleWithRequiredData];
        expectedResult = service.addSysConfigToCollectionIfMissing(sysConfigCollection, undefined, null);
        expect(expectedResult).toEqual(sysConfigCollection);
      });
    });

    describe('compareSysConfig', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSysConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareSysConfig(entity1, entity2);
        const compareResult2 = service.compareSysConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareSysConfig(entity1, entity2);
        const compareResult2 = service.compareSysConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareSysConfig(entity1, entity2);
        const compareResult2 = service.compareSysConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
