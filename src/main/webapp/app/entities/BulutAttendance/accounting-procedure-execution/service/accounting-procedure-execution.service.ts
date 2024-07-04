import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountingProcedureExecution, NewAccountingProcedureExecution } from '../accounting-procedure-execution.model';

export type PartialUpdateAccountingProcedureExecution = Partial<IAccountingProcedureExecution> & Pick<IAccountingProcedureExecution, 'id'>;

type RestOf<T extends IAccountingProcedureExecution | NewAccountingProcedureExecution> = Omit<T, 'dateTime'> & {
  dateTime?: string | null;
};

export type RestAccountingProcedureExecution = RestOf<IAccountingProcedureExecution>;

export type NewRestAccountingProcedureExecution = RestOf<NewAccountingProcedureExecution>;

export type PartialUpdateRestAccountingProcedureExecution = RestOf<PartialUpdateAccountingProcedureExecution>;

export type EntityResponseType = HttpResponse<IAccountingProcedureExecution>;
export type EntityArrayResponseType = HttpResponse<IAccountingProcedureExecution[]>;

@Injectable({ providedIn: 'root' })
export class AccountingProcedureExecutionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounting-procedure-executions', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/accounting-procedure-executions/_search',
    'bulutattendance',
  );

  create(accountingProcedureExecution: NewAccountingProcedureExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountingProcedureExecution);
    return this.http
      .post<RestAccountingProcedureExecution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(accountingProcedureExecution: IAccountingProcedureExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountingProcedureExecution);
    return this.http
      .put<RestAccountingProcedureExecution>(
        `${this.resourceUrl}/${this.getAccountingProcedureExecutionIdentifier(accountingProcedureExecution)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(accountingProcedureExecution: PartialUpdateAccountingProcedureExecution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountingProcedureExecution);
    return this.http
      .patch<RestAccountingProcedureExecution>(
        `${this.resourceUrl}/${this.getAccountingProcedureExecutionIdentifier(accountingProcedureExecution)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAccountingProcedureExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAccountingProcedureExecution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAccountingProcedureExecution[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IAccountingProcedureExecution[]>()], asapScheduler)),
    );
  }

  getAccountingProcedureExecutionIdentifier(accountingProcedureExecution: Pick<IAccountingProcedureExecution, 'id'>): number {
    return accountingProcedureExecution.id;
  }

  compareAccountingProcedureExecution(
    o1: Pick<IAccountingProcedureExecution, 'id'> | null,
    o2: Pick<IAccountingProcedureExecution, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getAccountingProcedureExecutionIdentifier(o1) === this.getAccountingProcedureExecutionIdentifier(o2) : o1 === o2;
  }

  addAccountingProcedureExecutionToCollectionIfMissing<Type extends Pick<IAccountingProcedureExecution, 'id'>>(
    accountingProcedureExecutionCollection: Type[],
    ...accountingProcedureExecutionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountingProcedureExecutions: Type[] = accountingProcedureExecutionsToCheck.filter(isPresent);
    if (accountingProcedureExecutions.length > 0) {
      const accountingProcedureExecutionCollectionIdentifiers = accountingProcedureExecutionCollection.map(
        accountingProcedureExecutionItem => this.getAccountingProcedureExecutionIdentifier(accountingProcedureExecutionItem),
      );
      const accountingProcedureExecutionsToAdd = accountingProcedureExecutions.filter(accountingProcedureExecutionItem => {
        const accountingProcedureExecutionIdentifier = this.getAccountingProcedureExecutionIdentifier(accountingProcedureExecutionItem);
        if (accountingProcedureExecutionCollectionIdentifiers.includes(accountingProcedureExecutionIdentifier)) {
          return false;
        }
        accountingProcedureExecutionCollectionIdentifiers.push(accountingProcedureExecutionIdentifier);
        return true;
      });
      return [...accountingProcedureExecutionsToAdd, ...accountingProcedureExecutionCollection];
    }
    return accountingProcedureExecutionCollection;
  }

  protected convertDateFromClient<
    T extends IAccountingProcedureExecution | NewAccountingProcedureExecution | PartialUpdateAccountingProcedureExecution,
  >(accountingProcedureExecution: T): RestOf<T> {
    return {
      ...accountingProcedureExecution,
      dateTime: accountingProcedureExecution.dateTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAccountingProcedureExecution: RestAccountingProcedureExecution): IAccountingProcedureExecution {
    return {
      ...restAccountingProcedureExecution,
      dateTime: restAccountingProcedureExecution.dateTime ? dayjs(restAccountingProcedureExecution.dateTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAccountingProcedureExecution>): HttpResponse<IAccountingProcedureExecution> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestAccountingProcedureExecution[]>,
  ): HttpResponse<IAccountingProcedureExecution[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
