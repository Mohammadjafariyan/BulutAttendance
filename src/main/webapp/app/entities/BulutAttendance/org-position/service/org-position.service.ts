import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOrgPosition, NewOrgPosition } from '../org-position.model';

export type PartialUpdateOrgPosition = Partial<IOrgPosition> & Pick<IOrgPosition, 'id'>;

export type EntityResponseType = HttpResponse<IOrgPosition>;
export type EntityArrayResponseType = HttpResponse<IOrgPosition[]>;

@Injectable({ providedIn: 'root' })
export class OrgPositionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-positions', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/org-positions/_search', 'bulutattendance');

  create(orgPosition: NewOrgPosition): Observable<EntityResponseType> {
    return this.http.post<IOrgPosition>(this.resourceUrl, orgPosition, { observe: 'response' });
  }

  update(orgPosition: IOrgPosition): Observable<EntityResponseType> {
    return this.http.put<IOrgPosition>(`${this.resourceUrl}/${this.getOrgPositionIdentifier(orgPosition)}`, orgPosition, {
      observe: 'response',
    });
  }

  partialUpdate(orgPosition: PartialUpdateOrgPosition): Observable<EntityResponseType> {
    return this.http.patch<IOrgPosition>(`${this.resourceUrl}/${this.getOrgPositionIdentifier(orgPosition)}`, orgPosition, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IOrgPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgPosition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrgPosition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IOrgPosition[]>()], asapScheduler)));
  }

  getOrgPositionIdentifier(orgPosition: Pick<IOrgPosition, 'id'>): string {
    return orgPosition.id;
  }

  compareOrgPosition(o1: Pick<IOrgPosition, 'id'> | null, o2: Pick<IOrgPosition, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrgPositionIdentifier(o1) === this.getOrgPositionIdentifier(o2) : o1 === o2;
  }

  addOrgPositionToCollectionIfMissing<Type extends Pick<IOrgPosition, 'id'>>(
    orgPositionCollection: Type[],
    ...orgPositionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orgPositions: Type[] = orgPositionsToCheck.filter(isPresent);
    if (orgPositions.length > 0) {
      const orgPositionCollectionIdentifiers = orgPositionCollection.map(orgPositionItem => this.getOrgPositionIdentifier(orgPositionItem));
      const orgPositionsToAdd = orgPositions.filter(orgPositionItem => {
        const orgPositionIdentifier = this.getOrgPositionIdentifier(orgPositionItem);
        if (orgPositionCollectionIdentifiers.includes(orgPositionIdentifier)) {
          return false;
        }
        orgPositionCollectionIdentifiers.push(orgPositionIdentifier);
        return true;
      });
      return [...orgPositionsToAdd, ...orgPositionCollection];
    }
    return orgPositionCollection;
  }
}
