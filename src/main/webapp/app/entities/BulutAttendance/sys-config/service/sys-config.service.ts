import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISysConfig, NewSysConfig } from '../sys-config.model';

export type PartialUpdateSysConfig = Partial<ISysConfig> & Pick<ISysConfig, 'id'>;

export type EntityResponseType = HttpResponse<ISysConfig>;
export type EntityArrayResponseType = HttpResponse<ISysConfig[]>;

@Injectable({ providedIn: 'root' })
export class SysConfigService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sys-configs', 'bulutattendance');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/sys-configs/_search', 'bulutattendance');

  create(sysConfig: NewSysConfig): Observable<EntityResponseType> {
    return this.http.post<ISysConfig>(this.resourceUrl, sysConfig, { observe: 'response' });
  }

  update(sysConfig: ISysConfig): Observable<EntityResponseType> {
    return this.http.put<ISysConfig>(`${this.resourceUrl}/${this.getSysConfigIdentifier(sysConfig)}`, sysConfig, { observe: 'response' });
  }

  partialUpdate(sysConfig: PartialUpdateSysConfig): Observable<EntityResponseType> {
    return this.http.patch<ISysConfig>(`${this.resourceUrl}/${this.getSysConfigIdentifier(sysConfig)}`, sysConfig, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISysConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISysConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISysConfig[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ISysConfig[]>()], asapScheduler)));
  }

  getSysConfigIdentifier(sysConfig: Pick<ISysConfig, 'id'>): string {
    return sysConfig.id;
  }

  compareSysConfig(o1: Pick<ISysConfig, 'id'> | null, o2: Pick<ISysConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getSysConfigIdentifier(o1) === this.getSysConfigIdentifier(o2) : o1 === o2;
  }

  addSysConfigToCollectionIfMissing<Type extends Pick<ISysConfig, 'id'>>(
    sysConfigCollection: Type[],
    ...sysConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sysConfigs: Type[] = sysConfigsToCheck.filter(isPresent);
    if (sysConfigs.length > 0) {
      const sysConfigCollectionIdentifiers = sysConfigCollection.map(sysConfigItem => this.getSysConfigIdentifier(sysConfigItem));
      const sysConfigsToAdd = sysConfigs.filter(sysConfigItem => {
        const sysConfigIdentifier = this.getSysConfigIdentifier(sysConfigItem);
        if (sysConfigCollectionIdentifiers.includes(sysConfigIdentifier)) {
          return false;
        }
        sysConfigCollectionIdentifiers.push(sysConfigIdentifier);
        return true;
      });
      return [...sysConfigsToAdd, ...sysConfigCollection];
    }
    return sysConfigCollection;
  }
}
