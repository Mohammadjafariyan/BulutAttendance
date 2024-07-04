import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountingProcedure } from '../accounting-procedure.model';
import { AccountingProcedureService } from '../service/accounting-procedure.service';

const accountingProcedureResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountingProcedure> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountingProcedureService)
      .find(id)
      .pipe(
        mergeMap((accountingProcedure: HttpResponse<IAccountingProcedure>) => {
          if (accountingProcedure.body) {
            return of(accountingProcedure.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountingProcedureResolve;
