import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountingProcedure, NewAccountingProcedure } from '../accounting-procedure.model';

export type PartialUpdateAccountingProcedure = Partial<IAccountingProcedure> & Pick<IAccountingProcedure, 'id'>;

export type EntityResponseType = HttpResponse<IAccountingProcedure>;
export type EntityArrayResponseType = HttpResponse<IAccountingProcedure[]>;

@Injectable({ providedIn: 'root' })
export class AccountingProcedureService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounting-procedures', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounting-procedures/_search', 'bulutattendance');

  create(accountingProcedure: NewAccountingProcedure): Observable<EntityResponseType> {
    return this.http.post<IAccountingProcedure>(this.resourceUrl, accountingProcedure, { observe: 'response' });
  }

  update(accountingProcedure: IAccountingProcedure): Observable<EntityResponseType> {
    return this.http.put<IAccountingProcedure>(
      `${this.resourceUrl}/${this.getAccountingProcedureIdentifier(accountingProcedure)}`,
      accountingProcedure,
      { observe: 'response' },
    );
  }

  partialUpdate(accountingProcedure: PartialUpdateAccountingProcedure): Observable<EntityResponseType> {
    return this.http.patch<IAccountingProcedure>(
      `${this.resourceUrl}/${this.getAccountingProcedureIdentifier(accountingProcedure)}`,
      accountingProcedure,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountingProcedure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountingProcedure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountingProcedure[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccountingProcedure[]>()], asapScheduler)));
  }

  getAccountingProcedureIdentifier(accountingProcedure: Pick<IAccountingProcedure, 'id'>): number {
    return accountingProcedure.id;
  }

  compareAccountingProcedure(o1: Pick<IAccountingProcedure, 'id'> | null, o2: Pick<IAccountingProcedure, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountingProcedureIdentifier(o1) === this.getAccountingProcedureIdentifier(o2) : o1 === o2;
  }

  addAccountingProcedureToCollectionIfMissing<Type extends Pick<IAccountingProcedure, 'id'>>(
    accountingProcedureCollection: Type[],
    ...accountingProceduresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountingProcedures: Type[] = accountingProceduresToCheck.filter(isPresent);
    if (accountingProcedures.length > 0) {
      const accountingProcedureCollectionIdentifiers = accountingProcedureCollection.map(accountingProcedureItem =>
        this.getAccountingProcedureIdentifier(accountingProcedureItem),
      );
      const accountingProceduresToAdd = accountingProcedures.filter(accountingProcedureItem => {
        const accountingProcedureIdentifier = this.getAccountingProcedureIdentifier(accountingProcedureItem);
        if (accountingProcedureCollectionIdentifiers.includes(accountingProcedureIdentifier)) {
          return false;
        }
        accountingProcedureCollectionIdentifiers.push(accountingProcedureIdentifier);
        return true;
      });
      return [...accountingProceduresToAdd, ...accountingProcedureCollection];
    }
    return accountingProcedureCollection;
  }
}
