import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccProcStepExecution } from '../acc-proc-step-execution.model';
import { AccProcStepExecutionService } from '../service/acc-proc-step-execution.service';

const accProcStepExecutionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccProcStepExecution> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccProcStepExecutionService)
      .find(id)
      .pipe(
        mergeMap((accProcStepExecution: HttpResponse<IAccProcStepExecution>) => {
          if (accProcStepExecution.body) {
            return of(accProcStepExecution.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accProcStepExecutionResolve;
