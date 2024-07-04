import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountHesab } from '../account-hesab.model';
import { AccountHesabService } from '../service/account-hesab.service';

const accountHesabResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountHesab> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountHesabService)
      .find(id)
      .pipe(
        mergeMap((accountHesab: HttpResponse<IAccountHesab>) => {
          if (accountHesab.body) {
            return of(accountHesab.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountHesabResolve;
