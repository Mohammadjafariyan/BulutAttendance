import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountingProcedureExecution } from '../accounting-procedure-execution.model';
import { AccountingProcedureExecutionService } from '../service/accounting-procedure-execution.service';

const accountingProcedureExecutionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountingProcedureExecution> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountingProcedureExecutionService)
      .find(id)
      .pipe(
        mergeMap((accountingProcedureExecution: HttpResponse<IAccountingProcedureExecution>) => {
          if (accountingProcedureExecution.body) {
            return of(accountingProcedureExecution.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountingProcedureExecutionResolve;
