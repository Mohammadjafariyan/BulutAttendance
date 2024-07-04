import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBank, NewBank } from '../bank.model';

export type PartialUpdateBank = Partial<IBank> & Pick<IBank, 'id'>;

export type EntityResponseType = HttpResponse<IBank>;
export type EntityArrayResponseType = HttpResponse<IBank[]>;

@Injectable({ providedIn: 'root' })
export class BankService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/banks', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/banks/_search', 'bulutattendance');

  create(bank: NewBank): Observable<EntityResponseType> {
    return this.http.post<IBank>(this.resourceUrl, bank, { observe: 'response' });
  }

  update(bank: IBank): Observable<EntityResponseType> {
    return this.http.put<IBank>(`${this.resourceUrl}/${this.getBankIdentifier(bank)}`, bank, { observe: 'response' });
  }

  partialUpdate(bank: PartialUpdateBank): Observable<EntityResponseType> {
    return this.http.patch<IBank>(`${this.resourceUrl}/${this.getBankIdentifier(bank)}`, bank, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBank>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBank[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBank[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IBank[]>()], asapScheduler)));
  }

  getBankIdentifier(bank: Pick<IBank, 'id'>): string {
    return bank.id;
  }

  compareBank(o1: Pick<IBank, 'id'> | null, o2: Pick<IBank, 'id'> | null): boolean {
    return o1 && o2 ? this.getBankIdentifier(o1) === this.getBankIdentifier(o2) : o1 === o2;
  }

  addBankToCollectionIfMissing<Type extends Pick<IBank, 'id'>>(
    bankCollection: Type[],
    ...banksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const banks: Type[] = banksToCheck.filter(isPresent);
    if (banks.length > 0) {
      const bankCollectionIdentifiers = bankCollection.map(bankItem => this.getBankIdentifier(bankItem));
      const banksToAdd = banks.filter(bankItem => {
        const bankIdentifier = this.getBankIdentifier(bankItem);
        if (bankCollectionIdentifiers.includes(bankIdentifier)) {
          return false;
        }
        bankCollectionIdentifiers.push(bankIdentifier);
        return true;
      });
      return [...banksToAdd, ...bankCollection];
    }
    return bankCollection;
  }
}
