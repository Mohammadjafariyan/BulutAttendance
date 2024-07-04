import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaxTemplate } from '../tax-template.model';
import { TaxTemplateService } from '../service/tax-template.service';

const taxTemplateResolve = (route: ActivatedRouteSnapshot): Observable<null | ITaxTemplate> => {
  const id = route.params['id'];
  if (id) {
    return inject(TaxTemplateService)
      .find(id)
      .pipe(
        mergeMap((taxTemplate: HttpResponse<ITaxTemplate>) => {
          if (taxTemplate.body) {
            return of(taxTemplate.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default taxTemplateResolve;
