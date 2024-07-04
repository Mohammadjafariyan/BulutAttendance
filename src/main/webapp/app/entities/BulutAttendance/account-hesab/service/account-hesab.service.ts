import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountHesab, NewAccountHesab } from '../account-hesab.model';

export type PartialUpdateAccountHesab = Partial<IAccountHesab> & Pick<IAccountHesab, 'id'>;

export type EntityResponseType = HttpResponse<IAccountHesab>;
export type EntityArrayResponseType = HttpResponse<IAccountHesab[]>;

@Injectable({ providedIn: 'root' })
export class AccountHesabService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-hesabs', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/account-hesabs/_search', 'bulutattendance');

  create(accountHesab: NewAccountHesab): Observable<EntityResponseType> {
    return this.http.post<IAccountHesab>(this.resourceUrl, accountHesab, { observe: 'response' });
  }

  update(accountHesab: IAccountHesab): Observable<EntityResponseType> {
    return this.http.put<IAccountHesab>(`${this.resourceUrl}/${this.getAccountHesabIdentifier(accountHesab)}`, accountHesab, {
      observe: 'response',
    });
  }

  partialUpdate(accountHesab: PartialUpdateAccountHesab): Observable<EntityResponseType> {
    return this.http.patch<IAccountHesab>(`${this.resourceUrl}/${this.getAccountHesabIdentifier(accountHesab)}`, accountHesab, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAccountHesab>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountHesab[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountHesab[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccountHesab[]>()], asapScheduler)));
  }

  getAccountHesabIdentifier(accountHesab: Pick<IAccountHesab, 'id'>): string {
    return accountHesab.id;
  }

  compareAccountHesab(o1: Pick<IAccountHesab, 'id'> | null, o2: Pick<IAccountHesab, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountHesabIdentifier(o1) === this.getAccountHesabIdentifier(o2) : o1 === o2;
  }

  addAccountHesabToCollectionIfMissing<Type extends Pick<IAccountHesab, 'id'>>(
    accountHesabCollection: Type[],
    ...accountHesabsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountHesabs: Type[] = accountHesabsToCheck.filter(isPresent);
    if (accountHesabs.length > 0) {
      const accountHesabCollectionIdentifiers = accountHesabCollection.map(accountHesabItem =>
        this.getAccountHesabIdentifier(accountHesabItem),
      );
      const accountHesabsToAdd = accountHesabs.filter(accountHesabItem => {
        const accountHesabIdentifier = this.getAccountHesabIdentifier(accountHesabItem);
        if (accountHesabCollectionIdentifiers.includes(accountHesabIdentifier)) {
          return false;
        }
        accountHesabCollectionIdentifiers.push(accountHesabIdentifier);
        return true;
      });
      return [...accountHesabsToAdd, ...accountHesabCollection];
    }
    return accountHesabCollection;
  }
}
