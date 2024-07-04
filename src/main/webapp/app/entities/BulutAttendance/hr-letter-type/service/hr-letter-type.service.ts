import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IHrLetterType, NewHrLetterType } from '../hr-letter-type.model';

export type PartialUpdateHrLetterType = Partial<IHrLetterType> & Pick<IHrLetterType, 'id'>;

export type EntityResponseType = HttpResponse<IHrLetterType>;
export type EntityArrayResponseType = HttpResponse<IHrLetterType[]>;

@Injectable({ providedIn: 'root' })
export class HrLetterTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hr-letter-types', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/hr-letter-types/_search', 'bulutattendance');

  create(hrLetterType: NewHrLetterType): Observable<EntityResponseType> {
    return this.http.post<IHrLetterType>(this.resourceUrl, hrLetterType, { observe: 'response' });
  }

  update(hrLetterType: IHrLetterType): Observable<EntityResponseType> {
    return this.http.put<IHrLetterType>(`${this.resourceUrl}/${this.getHrLetterTypeIdentifier(hrLetterType)}`, hrLetterType, {
      observe: 'response',
    });
  }

  partialUpdate(hrLetterType: PartialUpdateHrLetterType): Observable<EntityResponseType> {
    return this.http.patch<IHrLetterType>(`${this.resourceUrl}/${this.getHrLetterTypeIdentifier(hrLetterType)}`, hrLetterType, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IHrLetterType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHrLetterType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHrLetterType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IHrLetterType[]>()], asapScheduler)));
  }

  getHrLetterTypeIdentifier(hrLetterType: Pick<IHrLetterType, 'id'>): string {
    return hrLetterType.id;
  }

  compareHrLetterType(o1: Pick<IHrLetterType, 'id'> | null, o2: Pick<IHrLetterType, 'id'> | null): boolean {
    return o1 && o2 ? this.getHrLetterTypeIdentifier(o1) === this.getHrLetterTypeIdentifier(o2) : o1 === o2;
  }

  addHrLetterTypeToCollectionIfMissing<Type extends Pick<IHrLetterType, 'id'>>(
    hrLetterTypeCollection: Type[],
    ...hrLetterTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hrLetterTypes: Type[] = hrLetterTypesToCheck.filter(isPresent);
    if (hrLetterTypes.length > 0) {
      const hrLetterTypeCollectionIdentifiers = hrLetterTypeCollection.map(hrLetterTypeItem =>
        this.getHrLetterTypeIdentifier(hrLetterTypeItem),
      );
      const hrLetterTypesToAdd = hrLetterTypes.filter(hrLetterTypeItem => {
        const hrLetterTypeIdentifier = this.getHrLetterTypeIdentifier(hrLetterTypeItem);
        if (hrLetterTypeCollectionIdentifiers.includes(hrLetterTypeIdentifier)) {
          return false;
        }
        hrLetterTypeCollectionIdentifiers.push(hrLetterTypeIdentifier);
        return true;
      });
      return [...hrLetterTypesToAdd, ...hrLetterTypeCollection];
    }
    return hrLetterTypeCollection;
  }
}
