import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveSummary } from '../leave-summary.model';
import { LeaveSummaryService } from '../service/leave-summary.service';

const leaveSummaryResolve = (route: ActivatedRouteSnapshot): Observable<null | ILeaveSummary> => {
  const id = route.params['id'];
  if (id) {
    return inject(LeaveSummaryService)
      .find(id)
      .pipe(
        mergeMap((leaveSummary: HttpResponse<ILeaveSummary>) => {
          if (leaveSummary.body) {
            return of(leaveSummary.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default leaveSummaryResolve;
