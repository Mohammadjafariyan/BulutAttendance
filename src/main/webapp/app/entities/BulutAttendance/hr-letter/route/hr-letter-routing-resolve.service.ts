import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHrLetter } from '../hr-letter.model';
import { HrLetterService } from '../service/hr-letter.service';

const hrLetterResolve = (route: ActivatedRouteSnapshot): Observable<null | IHrLetter> => {
  const id = route.params['id'];
  if (id) {
    return inject(HrLetterService)
      .find(id)
      .pipe(
        mergeMap((hrLetter: HttpResponse<IHrLetter>) => {
          if (hrLetter.body) {
            return of(hrLetter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default hrLetterResolve;
