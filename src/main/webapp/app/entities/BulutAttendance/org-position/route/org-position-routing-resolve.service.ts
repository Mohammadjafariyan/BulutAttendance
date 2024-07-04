import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgPosition } from '../org-position.model';
import { OrgPositionService } from '../service/org-position.service';

const orgPositionResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrgPosition> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrgPositionService)
      .find(id)
      .pipe(
        mergeMap((orgPosition: HttpResponse<IOrgPosition>) => {
          if (orgPosition.body) {
            return of(orgPosition.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default orgPositionResolve;
