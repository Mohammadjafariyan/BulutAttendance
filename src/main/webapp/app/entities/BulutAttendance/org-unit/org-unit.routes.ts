import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgUnitComponent } from './list/org-unit.component';
import { OrgUnitDetailComponent } from './detail/org-unit-detail.component';
import { OrgUnitUpdateComponent } from './update/org-unit-update.component';
import OrgUnitResolve from './route/org-unit-routing-resolve.service';

const orgUnitRoute: Routes = [
  {
    path: '',
    component: OrgUnitComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgUnitDetailComponent,
    resolve: {
      orgUnit: OrgUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgUnitUpdateComponent,
    resolve: {
      orgUnit: OrgUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgUnitUpdateComponent,
    resolve: {
      orgUnit: OrgUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orgUnitRoute;
