import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccProcStep, NewAccProcStep } from '../acc-proc-step.model';

export type PartialUpdateAccProcStep = Partial<IAccProcStep> & Pick<IAccProcStep, 'id'>;

export type EntityResponseType = HttpResponse<IAccProcStep>;
export type EntityArrayResponseType = HttpResponse<IAccProcStep[]>;

@Injectable({ providedIn: 'root' })
export class AccProcStepService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acc-proc-steps', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/acc-proc-steps/_search', 'bulutattendance');

  create(accProcStep: NewAccProcStep): Observable<EntityResponseType> {
    return this.http.post<IAccProcStep>(this.resourceUrl, accProcStep, { observe: 'response' });
  }

  update(accProcStep: IAccProcStep): Observable<EntityResponseType> {
    return this.http.put<IAccProcStep>(`${this.resourceUrl}/${this.getAccProcStepIdentifier(accProcStep)}`, accProcStep, {
      observe: 'response',
    });
  }

  partialUpdate(accProcStep: PartialUpdateAccProcStep): Observable<EntityResponseType> {
    return this.http.patch<IAccProcStep>(`${this.resourceUrl}/${this.getAccProcStepIdentifier(accProcStep)}`, accProcStep, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccProcStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccProcStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccProcStep[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccProcStep[]>()], asapScheduler)));
  }

  getAccProcStepIdentifier(accProcStep: Pick<IAccProcStep, 'id'>): number {
    return accProcStep.id;
  }

  compareAccProcStep(o1: Pick<IAccProcStep, 'id'> | null, o2: Pick<IAccProcStep, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccProcStepIdentifier(o1) === this.getAccProcStepIdentifier(o2) : o1 === o2;
  }

  addAccProcStepToCollectionIfMissing<Type extends Pick<IAccProcStep, 'id'>>(
    accProcStepCollection: Type[],
    ...accProcStepsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accProcSteps: Type[] = accProcStepsToCheck.filter(isPresent);
    if (accProcSteps.length > 0) {
      const accProcStepCollectionIdentifiers = accProcStepCollection.map(accProcStepItem => this.getAccProcStepIdentifier(accProcStepItem));
      const accProcStepsToAdd = accProcSteps.filter(accProcStepItem => {
        const accProcStepIdentifier = this.getAccProcStepIdentifier(accProcStepItem);
        if (accProcStepCollectionIdentifiers.includes(accProcStepIdentifier)) {
          return false;
        }
        accProcStepCollectionIdentifiers.push(accProcStepIdentifier);
        return true;
      });
      return [...accProcStepsToAdd, ...accProcStepCollection];
    }
    return accProcStepCollection;
  }
}
