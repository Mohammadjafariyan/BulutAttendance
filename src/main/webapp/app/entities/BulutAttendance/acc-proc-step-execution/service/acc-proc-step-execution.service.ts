import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccProcStepExecution, NewAccProcStepExecution } from '../acc-proc-step-execution.model';

export type PartialUpdateAccProcStepExecution = Partial<IAccProcStepExecution> & Pick<IAccProcStepExecution, 'id'>;

export type EntityResponseType = HttpResponse<IAccProcStepExecution>;
export type EntityArrayResponseType = HttpResponse<IAccProcStepExecution[]>;

@Injectable({ providedIn: 'root' })
export class AccProcStepExecutionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acc-proc-step-executions', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/acc-proc-step-executions/_search', 'bulutattendance');

  create(accProcStepExecution: NewAccProcStepExecution): Observable<EntityResponseType> {
    return this.http.post<IAccProcStepExecution>(this.resourceUrl, accProcStepExecution, { observe: 'response' });
  }

  update(accProcStepExecution: IAccProcStepExecution): Observable<EntityResponseType> {
    return this.http.put<IAccProcStepExecution>(
      `${this.resourceUrl}/${this.getAccProcStepExecutionIdentifier(accProcStepExecution)}`,
      accProcStepExecution,
      { observe: 'response' },
    );
  }

  partialUpdate(accProcStepExecution: PartialUpdateAccProcStepExecution): Observable<EntityResponseType> {
    return this.http.patch<IAccProcStepExecution>(
      `${this.resourceUrl}/${this.getAccProcStepExecutionIdentifier(accProcStepExecution)}`,
      accProcStepExecution,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccProcStepExecution>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccProcStepExecution[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccProcStepExecution[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccProcStepExecution[]>()], asapScheduler)));
  }

  getAccProcStepExecutionIdentifier(accProcStepExecution: Pick<IAccProcStepExecution, 'id'>): number {
    return accProcStepExecution.id;
  }

  compareAccProcStepExecution(o1: Pick<IAccProcStepExecution, 'id'> | null, o2: Pick<IAccProcStepExecution, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccProcStepExecutionIdentifier(o1) === this.getAccProcStepExecutionIdentifier(o2) : o1 === o2;
  }

  addAccProcStepExecutionToCollectionIfMissing<Type extends Pick<IAccProcStepExecution, 'id'>>(
    accProcStepExecutionCollection: Type[],
    ...accProcStepExecutionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accProcStepExecutions: Type[] = accProcStepExecutionsToCheck.filter(isPresent);
    if (accProcStepExecutions.length > 0) {
      const accProcStepExecutionCollectionIdentifiers = accProcStepExecutionCollection.map(accProcStepExecutionItem =>
        this.getAccProcStepExecutionIdentifier(accProcStepExecutionItem),
      );
      const accProcStepExecutionsToAdd = accProcStepExecutions.filter(accProcStepExecutionItem => {
        const accProcStepExecutionIdentifier = this.getAccProcStepExecutionIdentifier(accProcStepExecutionItem);
        if (accProcStepExecutionCollectionIdentifiers.includes(accProcStepExecutionIdentifier)) {
          return false;
        }
        accProcStepExecutionCollectionIdentifiers.push(accProcStepExecutionIdentifier);
        return true;
      });
      return [...accProcStepExecutionsToAdd, ...accProcStepExecutionCollection];
    }
    return accProcStepExecutionCollection;
  }
}
