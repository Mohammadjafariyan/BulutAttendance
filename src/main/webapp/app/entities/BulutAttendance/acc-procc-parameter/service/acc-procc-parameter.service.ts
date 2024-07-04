import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccProccParameter, NewAccProccParameter } from '../acc-procc-parameter.model';

export type PartialUpdateAccProccParameter = Partial<IAccProccParameter> & Pick<IAccProccParameter, 'id'>;

export type EntityResponseType = HttpResponse<IAccProccParameter>;
export type EntityArrayResponseType = HttpResponse<IAccProccParameter[]>;

@Injectable({ providedIn: 'root' })
export class AccProccParameterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acc-procc-parameters', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/acc-procc-parameters/_search', 'bulutattendance');

  create(accProccParameter: NewAccProccParameter): Observable<EntityResponseType> {
    return this.http.post<IAccProccParameter>(this.resourceUrl, accProccParameter, { observe: 'response' });
  }

  update(accProccParameter: IAccProccParameter): Observable<EntityResponseType> {
    return this.http.put<IAccProccParameter>(
      `${this.resourceUrl}/${this.getAccProccParameterIdentifier(accProccParameter)}`,
      accProccParameter,
      { observe: 'response' },
    );
  }

  partialUpdate(accProccParameter: PartialUpdateAccProccParameter): Observable<EntityResponseType> {
    return this.http.patch<IAccProccParameter>(
      `${this.resourceUrl}/${this.getAccProccParameterIdentifier(accProccParameter)}`,
      accProccParameter,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccProccParameter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccProccParameter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccProccParameter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccProccParameter[]>()], asapScheduler)));
  }

  getAccProccParameterIdentifier(accProccParameter: Pick<IAccProccParameter, 'id'>): number {
    return accProccParameter.id;
  }

  compareAccProccParameter(o1: Pick<IAccProccParameter, 'id'> | null, o2: Pick<IAccProccParameter, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccProccParameterIdentifier(o1) === this.getAccProccParameterIdentifier(o2) : o1 === o2;
  }

  addAccProccParameterToCollectionIfMissing<Type extends Pick<IAccProccParameter, 'id'>>(
    accProccParameterCollection: Type[],
    ...accProccParametersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accProccParameters: Type[] = accProccParametersToCheck.filter(isPresent);
    if (accProccParameters.length > 0) {
      const accProccParameterCollectionIdentifiers = accProccParameterCollection.map(accProccParameterItem =>
        this.getAccProccParameterIdentifier(accProccParameterItem),
      );
      const accProccParametersToAdd = accProccParameters.filter(accProccParameterItem => {
        const accProccParameterIdentifier = this.getAccProccParameterIdentifier(accProccParameterItem);
        if (accProccParameterCollectionIdentifiers.includes(accProccParameterIdentifier)) {
          return false;
        }
        accProccParameterCollectionIdentifiers.push(accProccParameterIdentifier);
        return true;
      });
      return [...accProccParametersToAdd, ...accProccParameterCollection];
    }
    return accProccParameterCollection;
  }
}
