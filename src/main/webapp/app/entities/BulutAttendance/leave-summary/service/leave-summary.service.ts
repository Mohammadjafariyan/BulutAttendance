import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaveSummary, NewLeaveSummary } from '../leave-summary.model';

export type PartialUpdateLeaveSummary = Partial<ILeaveSummary> & Pick<ILeaveSummary, 'id'>;

export type EntityResponseType = HttpResponse<ILeaveSummary>;
export type EntityArrayResponseType = HttpResponse<ILeaveSummary[]>;

@Injectable({ providedIn: 'root' })
export class LeaveSummaryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-summaries', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leave-summaries/_search', 'bulutattendance');

  create(leaveSummary: NewLeaveSummary): Observable<EntityResponseType> {
    return this.http.post<ILeaveSummary>(this.resourceUrl, leaveSummary, { observe: 'response' });
  }

  update(leaveSummary: ILeaveSummary): Observable<EntityResponseType> {
    return this.http.put<ILeaveSummary>(`${this.resourceUrl}/${this.getLeaveSummaryIdentifier(leaveSummary)}`, leaveSummary, {
      observe: 'response',
    });
  }

  partialUpdate(leaveSummary: PartialUpdateLeaveSummary): Observable<EntityResponseType> {
    return this.http.patch<ILeaveSummary>(`${this.resourceUrl}/${this.getLeaveSummaryIdentifier(leaveSummary)}`, leaveSummary, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ILeaveSummary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveSummary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveSummary[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ILeaveSummary[]>()], asapScheduler)));
  }

  getLeaveSummaryIdentifier(leaveSummary: Pick<ILeaveSummary, 'id'>): string {
    return leaveSummary.id;
  }

  compareLeaveSummary(o1: Pick<ILeaveSummary, 'id'> | null, o2: Pick<ILeaveSummary, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeaveSummaryIdentifier(o1) === this.getLeaveSummaryIdentifier(o2) : o1 === o2;
  }

  addLeaveSummaryToCollectionIfMissing<Type extends Pick<ILeaveSummary, 'id'>>(
    leaveSummaryCollection: Type[],
    ...leaveSummariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leaveSummaries: Type[] = leaveSummariesToCheck.filter(isPresent);
    if (leaveSummaries.length > 0) {
      const leaveSummaryCollectionIdentifiers = leaveSummaryCollection.map(leaveSummaryItem =>
        this.getLeaveSummaryIdentifier(leaveSummaryItem),
      );
      const leaveSummariesToAdd = leaveSummaries.filter(leaveSummaryItem => {
        const leaveSummaryIdentifier = this.getLeaveSummaryIdentifier(leaveSummaryItem);
        if (leaveSummaryCollectionIdentifiers.includes(leaveSummaryIdentifier)) {
          return false;
        }
        leaveSummaryCollectionIdentifiers.push(leaveSummaryIdentifier);
        return true;
      });
      return [...leaveSummariesToAdd, ...leaveSummaryCollection];
    }
    return leaveSummaryCollection;
  }
}
