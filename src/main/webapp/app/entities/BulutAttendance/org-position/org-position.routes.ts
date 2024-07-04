import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgPositionComponent } from './list/org-position.component';
import { OrgPositionDetailComponent } from './detail/org-position-detail.component';
import { OrgPositionUpdateComponent } from './update/org-position-update.component';
import OrgPositionResolve from './route/org-position-routing-resolve.service';

const orgPositionRoute: Routes = [
  {
    path: '',
    component: OrgPositionComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgPositionDetailComponent,
    resolve: {
      orgPosition: OrgPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgPositionUpdateComponent,
    resolve: {
      orgPosition: OrgPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgPositionUpdateComponent,
    resolve: {
      orgPosition: OrgPositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orgPositionRoute;
