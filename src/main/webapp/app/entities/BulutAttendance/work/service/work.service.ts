import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWork, NewWork } from '../work.model';

export type PartialUpdateWork = Partial<IWork> & Pick<IWork, 'id'>;

type RestOf<T extends IWork | NewWork> = Omit<T, 'issueDate'> & {
  issueDate?: string | null;
};

export type RestWork = RestOf<IWork>;

export type NewRestWork = RestOf<NewWork>;

export type PartialUpdateRestWork = RestOf<PartialUpdateWork>;

export type EntityResponseType = HttpResponse<IWork>;
export type EntityArrayResponseType = HttpResponse<IWork[]>;

@Injectable({ providedIn: 'root' })
export class WorkService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/works', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/works/_search', 'bulutattendance');

  create(work: NewWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(work);
    return this.http.post<RestWork>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(work: IWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(work);
    return this.http
      .put<RestWork>(`${this.resourceUrl}/${this.getWorkIdentifier(work)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(work: PartialUpdateWork): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(work);
    return this.http
      .patch<RestWork>(`${this.resourceUrl}/${this.getWorkIdentifier(work)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestWork>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWork[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestWork[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IWork[]>()], asapScheduler)),
    );
  }

  getWorkIdentifier(work: Pick<IWork, 'id'>): string {
    return work.id;
  }

  compareWork(o1: Pick<IWork, 'id'> | null, o2: Pick<IWork, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkIdentifier(o1) === this.getWorkIdentifier(o2) : o1 === o2;
  }

  addWorkToCollectionIfMissing<Type extends Pick<IWork, 'id'>>(
    workCollection: Type[],
    ...worksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const works: Type[] = worksToCheck.filter(isPresent);
    if (works.length > 0) {
      const workCollectionIdentifiers = workCollection.map(workItem => this.getWorkIdentifier(workItem));
      const worksToAdd = works.filter(workItem => {
        const workIdentifier = this.getWorkIdentifier(workItem);
        if (workCollectionIdentifiers.includes(workIdentifier)) {
          return false;
        }
        workCollectionIdentifiers.push(workIdentifier);
        return true;
      });
      return [...worksToAdd, ...workCollection];
    }
    return workCollection;
  }

  protected convertDateFromClient<T extends IWork | NewWork | PartialUpdateWork>(work: T): RestOf<T> {
    return {
      ...work,
      issueDate: work.issueDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWork: RestWork): IWork {
    return {
      ...restWork,
      issueDate: restWork.issueDate ? dayjs(restWork.issueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWork>): HttpResponse<IWork> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWork[]>): HttpResponse<IWork[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
