import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonnel } from '../personnel.model';
import { PersonnelService } from '../service/personnel.service';

const personnelResolve = (route: ActivatedRouteSnapshot): Observable<null | IPersonnel> => {
  const id = route.params['id'];
  if (id) {
    return inject(PersonnelService)
      .find(id)
      .pipe(
        mergeMap((personnel: HttpResponse<IPersonnel>) => {
          if (personnel.body) {
            return of(personnel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default personnelResolve;
