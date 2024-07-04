import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITaxTemplate, NewTaxTemplate } from '../tax-template.model';

export type PartialUpdateTaxTemplate = Partial<ITaxTemplate> & Pick<ITaxTemplate, 'id'>;

export type EntityResponseType = HttpResponse<ITaxTemplate>;
export type EntityArrayResponseType = HttpResponse<ITaxTemplate[]>;

@Injectable({ providedIn: 'root' })
export class TaxTemplateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tax-templates', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tax-templates/_search', 'bulutattendance');

  create(taxTemplate: NewTaxTemplate): Observable<EntityResponseType> {
    return this.http.post<ITaxTemplate>(this.resourceUrl, taxTemplate, { observe: 'response' });
  }

  update(taxTemplate: ITaxTemplate): Observable<EntityResponseType> {
    return this.http.put<ITaxTemplate>(`${this.resourceUrl}/${this.getTaxTemplateIdentifier(taxTemplate)}`, taxTemplate, {
      observe: 'response',
    });
  }

  partialUpdate(taxTemplate: PartialUpdateTaxTemplate): Observable<EntityResponseType> {
    return this.http.patch<ITaxTemplate>(`${this.resourceUrl}/${this.getTaxTemplateIdentifier(taxTemplate)}`, taxTemplate, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ITaxTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxTemplate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxTemplate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITaxTemplate[]>()], asapScheduler)));
  }

  getTaxTemplateIdentifier(taxTemplate: Pick<ITaxTemplate, 'id'>): string {
    return taxTemplate.id;
  }

  compareTaxTemplate(o1: Pick<ITaxTemplate, 'id'> | null, o2: Pick<ITaxTemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getTaxTemplateIdentifier(o1) === this.getTaxTemplateIdentifier(o2) : o1 === o2;
  }

  addTaxTemplateToCollectionIfMissing<Type extends Pick<ITaxTemplate, 'id'>>(
    taxTemplateCollection: Type[],
    ...taxTemplatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxTemplates: Type[] = taxTemplatesToCheck.filter(isPresent);
    if (taxTemplates.length > 0) {
      const taxTemplateCollectionIdentifiers = taxTemplateCollection.map(taxTemplateItem => this.getTaxTemplateIdentifier(taxTemplateItem));
      const taxTemplatesToAdd = taxTemplates.filter(taxTemplateItem => {
        const taxTemplateIdentifier = this.getTaxTemplateIdentifier(taxTemplateItem);
        if (taxTemplateCollectionIdentifiers.includes(taxTemplateIdentifier)) {
          return false;
        }
        taxTemplateCollectionIdentifiers.push(taxTemplateIdentifier);
        return true;
      });
      return [...taxTemplatesToAdd, ...taxTemplateCollection];
    }
    return taxTemplateCollection;
  }
}
