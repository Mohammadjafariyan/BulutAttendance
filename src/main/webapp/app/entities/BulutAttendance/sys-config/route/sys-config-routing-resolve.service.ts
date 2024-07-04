import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISysConfig } from '../sys-config.model';
import { SysConfigService } from '../service/sys-config.service';

const sysConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | ISysConfig> => {
  const id = route.params['id'];
  if (id) {
    return inject(SysConfigService)
      .find(id)
      .pipe(
        mergeMap((sysConfig: HttpResponse<ISysConfig>) => {
          if (sysConfig.body) {
            return of(sysConfig.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sysConfigResolve;
