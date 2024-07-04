import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IRecordStatus, NewRecordStatus } from '../record-status.model';

export type PartialUpdateRecordStatus = Partial<IRecordStatus> & Pick<IRecordStatus, 'id'>;

type RestOf<T extends IRecordStatus | NewRecordStatus> = Omit<T, 'fromDateTime' | 'toDateTime'> & {
  fromDateTime?: string | null;
  toDateTime?: string | null;
};

export type RestRecordStatus = RestOf<IRecordStatus>;

export type NewRestRecordStatus = RestOf<NewRecordStatus>;

export type PartialUpdateRestRecordStatus = RestOf<PartialUpdateRecordStatus>;

export type EntityResponseType = HttpResponse<IRecordStatus>;
export type EntityArrayResponseType = HttpResponse<IRecordStatus[]>;

@Injectable({ providedIn: 'root' })
export class RecordStatusService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/record-statuses', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/record-statuses/_search', 'bulutattendance');

  create(recordStatus: NewRecordStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordStatus);
    return this.http
      .post<RestRecordStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recordStatus: IRecordStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordStatus);
    return this.http
      .put<RestRecordStatus>(`${this.resourceUrl}/${this.getRecordStatusIdentifier(recordStatus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recordStatus: PartialUpdateRecordStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recordStatus);
    return this.http
      .patch<RestRecordStatus>(`${this.resourceUrl}/${this.getRecordStatusIdentifier(recordStatus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestRecordStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecordStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestRecordStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IRecordStatus[]>()], asapScheduler)),
    );
  }

  getRecordStatusIdentifier(recordStatus: Pick<IRecordStatus, 'id'>): string {
    return recordStatus.id;
  }

  compareRecordStatus(o1: Pick<IRecordStatus, 'id'> | null, o2: Pick<IRecordStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecordStatusIdentifier(o1) === this.getRecordStatusIdentifier(o2) : o1 === o2;
  }

  addRecordStatusToCollectionIfMissing<Type extends Pick<IRecordStatus, 'id'>>(
    recordStatusCollection: Type[],
    ...recordStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recordStatuses: Type[] = recordStatusesToCheck.filter(isPresent);
    if (recordStatuses.length > 0) {
      const recordStatusCollectionIdentifiers = recordStatusCollection.map(recordStatusItem =>
        this.getRecordStatusIdentifier(recordStatusItem),
      );
      const recordStatusesToAdd = recordStatuses.filter(recordStatusItem => {
        const recordStatusIdentifier = this.getRecordStatusIdentifier(recordStatusItem);
        if (recordStatusCollectionIdentifiers.includes(recordStatusIdentifier)) {
          return false;
        }
        recordStatusCollectionIdentifiers.push(recordStatusIdentifier);
        return true;
      });
      return [...recordStatusesToAdd, ...recordStatusCollection];
    }
    return recordStatusCollection;
  }

  protected convertDateFromClient<T extends IRecordStatus | NewRecordStatus | PartialUpdateRecordStatus>(recordStatus: T): RestOf<T> {
    return {
      ...recordStatus,
      fromDateTime: recordStatus.fromDateTime?.toJSON() ?? null,
      toDateTime: recordStatus.toDateTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRecordStatus: RestRecordStatus): IRecordStatus {
    return {
      ...restRecordStatus,
      fromDateTime: restRecordStatus.fromDateTime ? dayjs(restRecordStatus.fromDateTime) : undefined,
      toDateTime: restRecordStatus.toDateTime ? dayjs(restRecordStatus.toDateTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRecordStatus>): HttpResponse<IRecordStatus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRecordStatus[]>): HttpResponse<IRecordStatus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
