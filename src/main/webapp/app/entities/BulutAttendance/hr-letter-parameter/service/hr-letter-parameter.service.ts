import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IHrLetterParameter, NewHrLetterParameter } from '../hr-letter-parameter.model';

export type PartialUpdateHrLetterParameter = Partial<IHrLetterParameter> & Pick<IHrLetterParameter, 'id'>;

export type EntityResponseType = HttpResponse<IHrLetterParameter>;
export type EntityArrayResponseType = HttpResponse<IHrLetterParameter[]>;

@Injectable({ providedIn: 'root' })
export class HrLetterParameterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hr-letter-parameters', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/hr-letter-parameters/_search', 'bulutattendance');

  create(hrLetterParameter: NewHrLetterParameter): Observable<EntityResponseType> {
    return this.http.post<IHrLetterParameter>(this.resourceUrl, hrLetterParameter, { observe: 'response' });
  }

  update(hrLetterParameter: IHrLetterParameter): Observable<EntityResponseType> {
    return this.http.put<IHrLetterParameter>(
      `${this.resourceUrl}/${this.getHrLetterParameterIdentifier(hrLetterParameter)}`,
      hrLetterParameter,
      { observe: 'response' },
    );
  }

  partialUpdate(hrLetterParameter: PartialUpdateHrLetterParameter): Observable<EntityResponseType> {
    return this.http.patch<IHrLetterParameter>(
      `${this.resourceUrl}/${this.getHrLetterParameterIdentifier(hrLetterParameter)}`,
      hrLetterParameter,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHrLetterParameter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHrLetterParameter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHrLetterParameter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IHrLetterParameter[]>()], asapScheduler)));
  }

  getHrLetterParameterIdentifier(hrLetterParameter: Pick<IHrLetterParameter, 'id'>): number {
    return hrLetterParameter.id;
  }

  compareHrLetterParameter(o1: Pick<IHrLetterParameter, 'id'> | null, o2: Pick<IHrLetterParameter, 'id'> | null): boolean {
    return o1 && o2 ? this.getHrLetterParameterIdentifier(o1) === this.getHrLetterParameterIdentifier(o2) : o1 === o2;
  }

  addHrLetterParameterToCollectionIfMissing<Type extends Pick<IHrLetterParameter, 'id'>>(
    hrLetterParameterCollection: Type[],
    ...hrLetterParametersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hrLetterParameters: Type[] = hrLetterParametersToCheck.filter(isPresent);
    if (hrLetterParameters.length > 0) {
      const hrLetterParameterCollectionIdentifiers = hrLetterParameterCollection.map(hrLetterParameterItem =>
        this.getHrLetterParameterIdentifier(hrLetterParameterItem),
      );
      const hrLetterParametersToAdd = hrLetterParameters.filter(hrLetterParameterItem => {
        const hrLetterParameterIdentifier = this.getHrLetterParameterIdentifier(hrLetterParameterItem);
        if (hrLetterParameterCollectionIdentifiers.includes(hrLetterParameterIdentifier)) {
          return false;
        }
        hrLetterParameterCollectionIdentifiers.push(hrLetterParameterIdentifier);
        return true;
      });
      return [...hrLetterParametersToAdd, ...hrLetterParameterCollection];
    }
    return hrLetterParameterCollection;
  }
}
