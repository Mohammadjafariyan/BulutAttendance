import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPersonnelStatus, NewPersonnelStatus } from '../personnel-status.model';

export type PartialUpdatePersonnelStatus = Partial<IPersonnelStatus> & Pick<IPersonnelStatus, 'id'>;

export type EntityResponseType = HttpResponse<IPersonnelStatus>;
export type EntityArrayResponseType = HttpResponse<IPersonnelStatus[]>;

@Injectable({ providedIn: 'root' })
export class PersonnelStatusService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnel-statuses', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/personnel-statuses/_search', 'bulutattendance');

  create(personnelStatus: NewPersonnelStatus): Observable<EntityResponseType> {
    return this.http.post<IPersonnelStatus>(this.resourceUrl, personnelStatus, { observe: 'response' });
  }

  update(personnelStatus: IPersonnelStatus): Observable<EntityResponseType> {
    return this.http.put<IPersonnelStatus>(`${this.resourceUrl}/${this.getPersonnelStatusIdentifier(personnelStatus)}`, personnelStatus, {
      observe: 'response',
    });
  }

  partialUpdate(personnelStatus: PartialUpdatePersonnelStatus): Observable<EntityResponseType> {
    return this.http.patch<IPersonnelStatus>(`${this.resourceUrl}/${this.getPersonnelStatusIdentifier(personnelStatus)}`, personnelStatus, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPersonnelStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonnelStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonnelStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IPersonnelStatus[]>()], asapScheduler)));
  }

  getPersonnelStatusIdentifier(personnelStatus: Pick<IPersonnelStatus, 'id'>): string {
    return personnelStatus.id;
  }

  comparePersonnelStatus(o1: Pick<IPersonnelStatus, 'id'> | null, o2: Pick<IPersonnelStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonnelStatusIdentifier(o1) === this.getPersonnelStatusIdentifier(o2) : o1 === o2;
  }

  addPersonnelStatusToCollectionIfMissing<Type extends Pick<IPersonnelStatus, 'id'>>(
    personnelStatusCollection: Type[],
    ...personnelStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personnelStatuses: Type[] = personnelStatusesToCheck.filter(isPresent);
    if (personnelStatuses.length > 0) {
      const personnelStatusCollectionIdentifiers = personnelStatusCollection.map(personnelStatusItem =>
        this.getPersonnelStatusIdentifier(personnelStatusItem),
      );
      const personnelStatusesToAdd = personnelStatuses.filter(personnelStatusItem => {
        const personnelStatusIdentifier = this.getPersonnelStatusIdentifier(personnelStatusItem);
        if (personnelStatusCollectionIdentifiers.includes(personnelStatusIdentifier)) {
          return false;
        }
        personnelStatusCollectionIdentifiers.push(personnelStatusIdentifier);
        return true;
      });
      return [...personnelStatusesToAdd, ...personnelStatusCollection];
    }
    return personnelStatusCollection;
  }
}
