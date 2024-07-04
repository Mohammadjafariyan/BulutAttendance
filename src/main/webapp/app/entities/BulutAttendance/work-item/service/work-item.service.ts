import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkItem, NewWorkItem } from '../work-item.model';

export type PartialUpdateWorkItem = Partial<IWorkItem> & Pick<IWorkItem, 'id'>;

export type EntityResponseType = HttpResponse<IWorkItem>;
export type EntityArrayResponseType = HttpResponse<IWorkItem[]>;

@Injectable({ providedIn: 'root' })
export class WorkItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-items', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/work-items/_search', 'bulutattendance');

  create(workItem: NewWorkItem): Observable<EntityResponseType> {
    return this.http.post<IWorkItem>(this.resourceUrl, workItem, { observe: 'response' });
  }

  update(workItem: IWorkItem): Observable<EntityResponseType> {
    return this.http.put<IWorkItem>(`${this.resourceUrl}/${this.getWorkItemIdentifier(workItem)}`, workItem, { observe: 'response' });
  }

  partialUpdate(workItem: PartialUpdateWorkItem): Observable<EntityResponseType> {
    return this.http.patch<IWorkItem>(`${this.resourceUrl}/${this.getWorkItemIdentifier(workItem)}`, workItem, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IWorkItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IWorkItem[]>()], asapScheduler)));
  }

  getWorkItemIdentifier(workItem: Pick<IWorkItem, 'id'>): string {
    return workItem.id;
  }

  compareWorkItem(o1: Pick<IWorkItem, 'id'> | null, o2: Pick<IWorkItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkItemIdentifier(o1) === this.getWorkItemIdentifier(o2) : o1 === o2;
  }

  addWorkItemToCollectionIfMissing<Type extends Pick<IWorkItem, 'id'>>(
    workItemCollection: Type[],
    ...workItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workItems: Type[] = workItemsToCheck.filter(isPresent);
    if (workItems.length > 0) {
      const workItemCollectionIdentifiers = workItemCollection.map(workItemItem => this.getWorkItemIdentifier(workItemItem));
      const workItemsToAdd = workItems.filter(workItemItem => {
        const workItemIdentifier = this.getWorkItemIdentifier(workItemItem);
        if (workItemCollectionIdentifiers.includes(workItemIdentifier)) {
          return false;
        }
        workItemCollectionIdentifiers.push(workItemIdentifier);
        return true;
      });
      return [...workItemsToAdd, ...workItemCollection];
    }
    return workItemCollection;
  }
}
