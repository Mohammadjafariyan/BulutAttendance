import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHrLetterParameter } from '../hr-letter-parameter.model';
import { HrLetterParameterService } from '../service/hr-letter-parameter.service';

const hrLetterParameterResolve = (route: ActivatedRouteSnapshot): Observable<null | IHrLetterParameter> => {
  const id = route.params['id'];
  if (id) {
    return inject(HrLetterParameterService)
      .find(id)
      .pipe(
        mergeMap((hrLetterParameter: HttpResponse<IHrLetterParameter>) => {
          if (hrLetterParameter.body) {
            return of(hrLetterParameter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default hrLetterParameterResolve;
