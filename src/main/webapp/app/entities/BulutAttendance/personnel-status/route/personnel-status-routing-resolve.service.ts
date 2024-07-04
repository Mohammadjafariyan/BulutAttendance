import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonnelStatus } from '../personnel-status.model';
import { PersonnelStatusService } from '../service/personnel-status.service';

const personnelStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IPersonnelStatus> => {
  const id = route.params['id'];
  if (id) {
    return inject(PersonnelStatusService)
      .find(id)
      .pipe(
        mergeMap((personnelStatus: HttpResponse<IPersonnelStatus>) => {
          if (personnelStatus.body) {
            return of(personnelStatus.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default personnelStatusResolve;
