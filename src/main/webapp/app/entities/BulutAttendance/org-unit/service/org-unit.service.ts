import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOrgUnit, NewOrgUnit } from '../org-unit.model';

export type PartialUpdateOrgUnit = Partial<IOrgUnit> & Pick<IOrgUnit, 'id'>;

export type EntityResponseType = HttpResponse<IOrgUnit>;
export type EntityArrayResponseType = HttpResponse<IOrgUnit[]>;

@Injectable({ providedIn: 'root' })
export class OrgUnitService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-units', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/org-units/_search', 'bulutattendance');

  create(orgUnit: NewOrgUnit): Observable<EntityResponseType> {
    return this.http.post<IOrgUnit>(this.resourceUrl, orgUnit, { observe: 'response' });
  }

  update(orgUnit: IOrgUnit): Observable<EntityResponseType> {
    return this.http.put<IOrgUnit>(`${this.resourceUrl}/${this.getOrgUnitIdentifier(orgUnit)}`, orgUnit, { observe: 'response' });
  }

  partialUpdate(orgUnit: PartialUpdateOrgUnit): Observable<EntityResponseType> {
    return this.http.patch<IOrgUnit>(`${this.resourceUrl}/${this.getOrgUnitIdentifier(orgUnit)}`, orgUnit, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IOrgUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrgUnit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IOrgUnit[]>()], asapScheduler)));
  }

  getOrgUnitIdentifier(orgUnit: Pick<IOrgUnit, 'id'>): string {
    return orgUnit.id;
  }

  compareOrgUnit(o1: Pick<IOrgUnit, 'id'> | null, o2: Pick<IOrgUnit, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrgUnitIdentifier(o1) === this.getOrgUnitIdentifier(o2) : o1 === o2;
  }

  addOrgUnitToCollectionIfMissing<Type extends Pick<IOrgUnit, 'id'>>(
    orgUnitCollection: Type[],
    ...orgUnitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orgUnits: Type[] = orgUnitsToCheck.filter(isPresent);
    if (orgUnits.length > 0) {
      const orgUnitCollectionIdentifiers = orgUnitCollection.map(orgUnitItem => this.getOrgUnitIdentifier(orgUnitItem));
      const orgUnitsToAdd = orgUnits.filter(orgUnitItem => {
        const orgUnitIdentifier = this.getOrgUnitIdentifier(orgUnitItem);
        if (orgUnitCollectionIdentifiers.includes(orgUnitIdentifier)) {
          return false;
        }
        orgUnitCollectionIdentifiers.push(orgUnitIdentifier);
        return true;
      });
      return [...orgUnitsToAdd, ...orgUnitCollection];
    }
    return orgUnitCollection;
  }
}
