import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountTemplate, NewAccountTemplate } from '../account-template.model';

export type PartialUpdateAccountTemplate = Partial<IAccountTemplate> & Pick<IAccountTemplate, 'id'>;

export type EntityResponseType = HttpResponse<IAccountTemplate>;
export type EntityArrayResponseType = HttpResponse<IAccountTemplate[]>;

@Injectable({ providedIn: 'root' })
export class AccountTemplateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-templates', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/account-templates/_search', 'bulutattendance');

  create(accountTemplate: NewAccountTemplate): Observable<EntityResponseType> {
    return this.http.post<IAccountTemplate>(this.resourceUrl, accountTemplate, { observe: 'response' });
  }

  update(accountTemplate: IAccountTemplate): Observable<EntityResponseType> {
    return this.http.put<IAccountTemplate>(`${this.resourceUrl}/${this.getAccountTemplateIdentifier(accountTemplate)}`, accountTemplate, {
      observe: 'response',
    });
  }

  partialUpdate(accountTemplate: PartialUpdateAccountTemplate): Observable<EntityResponseType> {
    return this.http.patch<IAccountTemplate>(`${this.resourceUrl}/${this.getAccountTemplateIdentifier(accountTemplate)}`, accountTemplate, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAccountTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountTemplate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountTemplate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAccountTemplate[]>()], asapScheduler)));
  }

  getAccountTemplateIdentifier(accountTemplate: Pick<IAccountTemplate, 'id'>): string {
    return accountTemplate.id;
  }

  compareAccountTemplate(o1: Pick<IAccountTemplate, 'id'> | null, o2: Pick<IAccountTemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountTemplateIdentifier(o1) === this.getAccountTemplateIdentifier(o2) : o1 === o2;
  }

  addAccountTemplateToCollectionIfMissing<Type extends Pick<IAccountTemplate, 'id'>>(
    accountTemplateCollection: Type[],
    ...accountTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountTemplates: Type[] = accountTemplatesToCheck.filter(isPresent);
    if (accountTemplates.length > 0) {
      const accountTemplateCollectionIdentifiers = accountTemplateCollection.map(accountTemplateItem =>
        this.getAccountTemplateIdentifier(accountTemplateItem),
      );
      const accountTemplatesToAdd = accountTemplates.filter(accountTemplateItem => {
        const accountTemplateIdentifier = this.getAccountTemplateIdentifier(accountTemplateItem);
        if (accountTemplateCollectionIdentifiers.includes(accountTemplateIdentifier)) {
          return false;
        }
        accountTemplateCollectionIdentifiers.push(accountTemplateIdentifier);
        return true;
      });
      return [...accountTemplatesToAdd, ...accountTemplateCollection];
    }
    return accountTemplateCollection;
  }
}
