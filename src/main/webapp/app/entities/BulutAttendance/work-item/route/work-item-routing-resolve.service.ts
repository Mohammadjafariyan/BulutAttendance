import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkItem } from '../work-item.model';
import { WorkItemService } from '../service/work-item.service';

const workItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkItemService)
      .find(id)
      .pipe(
        mergeMap((workItem: HttpResponse<IWorkItem>) => {
          if (workItem.body) {
            return of(workItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default workItemResolve;
