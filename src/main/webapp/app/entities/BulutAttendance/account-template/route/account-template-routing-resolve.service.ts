import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountTemplate } from '../account-template.model';
import { AccountTemplateService } from '../service/account-template.service';

const accountTemplateResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountTemplate> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountTemplateService)
      .find(id)
      .pipe(
        mergeMap((accountTemplate: HttpResponse<IAccountTemplate>) => {
          if (accountTemplate.body) {
            return of(accountTemplate.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountTemplateResolve;
