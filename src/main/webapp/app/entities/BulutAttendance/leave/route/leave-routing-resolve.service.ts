import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeave } from '../leave.model';
import { LeaveService } from '../service/leave.service';

const leaveResolve = (route: ActivatedRouteSnapshot): Observable<null | ILeave> => {
  const id = route.params['id'];
  if (id) {
    return inject(LeaveService)
      .find(id)
      .pipe(
        mergeMap((leave: HttpResponse<ILeave>) => {
          if (leave.body) {
            return of(leave.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default leaveResolve;
