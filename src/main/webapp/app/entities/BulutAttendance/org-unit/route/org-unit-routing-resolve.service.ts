import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';

const orgUnitResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrgUnit> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrgUnitService)
      .find(id)
      .pipe(
        mergeMap((orgUnit: HttpResponse<IOrgUnit>) => {
          if (orgUnit.body) {
            return of(orgUnit.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default orgUnitResolve;
