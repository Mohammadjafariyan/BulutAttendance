import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccProcStep } from '../acc-proc-step.model';
import { AccProcStepService } from '../service/acc-proc-step.service';

const accProcStepResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccProcStep> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccProcStepService)
      .find(id)
      .pipe(
        mergeMap((accProcStep: HttpResponse<IAccProcStep>) => {
          if (accProcStep.body) {
            return of(accProcStep.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accProcStepResolve;
