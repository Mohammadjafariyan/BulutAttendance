import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWork } from '../work.model';
import { WorkService } from '../service/work.service';

const workResolve = (route: ActivatedRouteSnapshot): Observable<null | IWork> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkService)
      .find(id)
      .pipe(
        mergeMap((work: HttpResponse<IWork>) => {
          if (work.body) {
            return of(work.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default workResolve;
