import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SysConfigComponent } from './list/sys-config.component';
import { SysConfigDetailComponent } from './detail/sys-config-detail.component';
import { SysConfigUpdateComponent } from './update/sys-config-update.component';
import SysConfigResolve from './route/sys-config-routing-resolve.service';

const sysConfigRoute: Routes = [
  {
    path: '',
    component: SysConfigComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SysConfigDetailComponent,
    resolve: {
      sysConfig: SysConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SysConfigUpdateComponent,
    resolve: {
      sysConfig: SysConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SysConfigUpdateComponent,
    resolve: {
      sysConfig: SysConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sysConfigRoute;
