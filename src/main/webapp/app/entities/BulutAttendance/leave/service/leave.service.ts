import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeave, NewLeave } from '../leave.model';

export type PartialUpdateLeave = Partial<ILeave> & Pick<ILeave, 'id'>;

type RestOf<T extends ILeave | NewLeave> = Omit<T, 'start' | 'end'> & {
  start?: string | null;
  end?: string | null;
};

export type RestLeave = RestOf<ILeave>;

export type NewRestLeave = RestOf<NewLeave>;

export type PartialUpdateRestLeave = RestOf<PartialUpdateLeave>;

export type EntityResponseType = HttpResponse<ILeave>;
export type EntityArrayResponseType = HttpResponse<ILeave[]>;

@Injectable({ providedIn: 'root' })
export class LeaveService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leaves', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leaves/_search', 'bulutattendance');

  create(leave: NewLeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http.post<RestLeave>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(leave: ILeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http
      .put<RestLeave>(`${this.resourceUrl}/${this.getLeaveIdentifier(leave)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(leave: PartialUpdateLeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http
      .patch<RestLeave>(`${this.resourceUrl}/${this.getLeaveIdentifier(leave)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestLeave>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLeave[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestLeave[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ILeave[]>()], asapScheduler)),
    );
  }

  getLeaveIdentifier(leave: Pick<ILeave, 'id'>): string {
    return leave.id;
  }

  compareLeave(o1: Pick<ILeave, 'id'> | null, o2: Pick<ILeave, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeaveIdentifier(o1) === this.getLeaveIdentifier(o2) : o1 === o2;
  }

  addLeaveToCollectionIfMissing<Type extends Pick<ILeave, 'id'>>(
    leaveCollection: Type[],
    ...leavesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leaves: Type[] = leavesToCheck.filter(isPresent);
    if (leaves.length > 0) {
      const leaveCollectionIdentifiers = leaveCollection.map(leaveItem => this.getLeaveIdentifier(leaveItem));
      const leavesToAdd = leaves.filter(leaveItem => {
        const leaveIdentifier = this.getLeaveIdentifier(leaveItem);
        if (leaveCollectionIdentifiers.includes(leaveIdentifier)) {
          return false;
        }
        leaveCollectionIdentifiers.push(leaveIdentifier);
        return true;
      });
      return [...leavesToAdd, ...leaveCollection];
    }
    return leaveCollection;
  }

  protected convertDateFromClient<T extends ILeave | NewLeave | PartialUpdateLeave>(leave: T): RestOf<T> {
    return {
      ...leave,
      start: leave.start?.toJSON() ?? null,
      end: leave.end?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLeave: RestLeave): ILeave {
    return {
      ...restLeave,
      start: restLeave.start ? dayjs(restLeave.start) : undefined,
      end: restLeave.end ? dayjs(restLeave.end) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLeave>): HttpResponse<ILeave> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLeave[]>): HttpResponse<ILeave[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
