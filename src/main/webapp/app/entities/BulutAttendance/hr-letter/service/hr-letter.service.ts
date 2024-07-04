import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IHrLetter, NewHrLetter } from '../hr-letter.model';

export type PartialUpdateHrLetter = Partial<IHrLetter> & Pick<IHrLetter, 'id'>;

type RestOf<T extends IHrLetter | NewHrLetter> = Omit<T, 'issueDate' | 'executionDate'> & {
  issueDate?: string | null;
  executionDate?: string | null;
};

export type RestHrLetter = RestOf<IHrLetter>;

export type NewRestHrLetter = RestOf<NewHrLetter>;

export type PartialUpdateRestHrLetter = RestOf<PartialUpdateHrLetter>;

export type EntityResponseType = HttpResponse<IHrLetter>;
export type EntityArrayResponseType = HttpResponse<IHrLetter[]>;

@Injectable({ providedIn: 'root' })
export class HrLetterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hr-letters', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/hr-letters/_search', 'bulutattendance');

  create(hrLetter: NewHrLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hrLetter);
    return this.http
      .post<RestHrLetter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hrLetter: IHrLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hrLetter);
    return this.http
      .put<RestHrLetter>(`${this.resourceUrl}/${this.getHrLetterIdentifier(hrLetter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hrLetter: PartialUpdateHrLetter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hrLetter);
    return this.http
      .patch<RestHrLetter>(`${this.resourceUrl}/${this.getHrLetterIdentifier(hrLetter)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestHrLetter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHrLetter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestHrLetter[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IHrLetter[]>()], asapScheduler)),
    );
  }

  getHrLetterIdentifier(hrLetter: Pick<IHrLetter, 'id'>): string {
    return hrLetter.id;
  }

  compareHrLetter(o1: Pick<IHrLetter, 'id'> | null, o2: Pick<IHrLetter, 'id'> | null): boolean {
    return o1 && o2 ? this.getHrLetterIdentifier(o1) === this.getHrLetterIdentifier(o2) : o1 === o2;
  }

  addHrLetterToCollectionIfMissing<Type extends Pick<IHrLetter, 'id'>>(
    hrLetterCollection: Type[],
    ...hrLettersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hrLetters: Type[] = hrLettersToCheck.filter(isPresent);
    if (hrLetters.length > 0) {
      const hrLetterCollectionIdentifiers = hrLetterCollection.map(hrLetterItem => this.getHrLetterIdentifier(hrLetterItem));
      const hrLettersToAdd = hrLetters.filter(hrLetterItem => {
        const hrLetterIdentifier = this.getHrLetterIdentifier(hrLetterItem);
        if (hrLetterCollectionIdentifiers.includes(hrLetterIdentifier)) {
          return false;
        }
        hrLetterCollectionIdentifiers.push(hrLetterIdentifier);
        return true;
      });
      return [...hrLettersToAdd, ...hrLetterCollection];
    }
    return hrLetterCollection;
  }

  protected convertDateFromClient<T extends IHrLetter | NewHrLetter | PartialUpdateHrLetter>(hrLetter: T): RestOf<T> {
    return {
      ...hrLetter,
      issueDate: hrLetter.issueDate?.toJSON() ?? null,
      executionDate: hrLetter.executionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHrLetter: RestHrLetter): IHrLetter {
    return {
      ...restHrLetter,
      issueDate: restHrLetter.issueDate ? dayjs(restHrLetter.issueDate) : undefined,
      executionDate: restHrLetter.executionDate ? dayjs(restHrLetter.executionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHrLetter>): HttpResponse<IHrLetter> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHrLetter[]>): HttpResponse<IHrLetter[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
