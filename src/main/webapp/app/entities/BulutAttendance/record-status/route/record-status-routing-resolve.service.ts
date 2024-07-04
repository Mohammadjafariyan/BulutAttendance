import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecordStatus } from '../record-status.model';
import { RecordStatusService } from '../service/record-status.service';

const recordStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecordStatus> => {
  const id = route.params['id'];
  if (id) {
    return inject(RecordStatusService)
      .find(id)
      .pipe(
        mergeMap((recordStatus: HttpResponse<IRecordStatus>) => {
          if (recordStatus.body) {
            return of(recordStatus.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default recordStatusResolve;
