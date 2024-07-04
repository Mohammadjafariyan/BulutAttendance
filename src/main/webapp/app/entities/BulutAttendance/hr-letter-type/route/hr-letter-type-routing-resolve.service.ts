import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHrLetterType } from '../hr-letter-type.model';
import { HrLetterTypeService } from '../service/hr-letter-type.service';

const hrLetterTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IHrLetterType> => {
  const id = route.params['id'];
  if (id) {
    return inject(HrLetterTypeService)
      .find(id)
      .pipe(
        mergeMap((hrLetterType: HttpResponse<IHrLetterType>) => {
          if (hrLetterType.body) {
            return of(hrLetterType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default hrLetterTypeResolve;
