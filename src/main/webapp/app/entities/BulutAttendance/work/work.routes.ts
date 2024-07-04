import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkComponent } from './list/work.component';
import { WorkDetailComponent } from './detail/work-detail.component';
import { WorkUpdateComponent } from './update/work-update.component';
import WorkResolve from './route/work-routing-resolve.service';

const workRoute: Routes = [
  {
    path: '',
    component: WorkComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkDetailComponent,
    resolve: {
      work: WorkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkUpdateComponent,
    resolve: {
      work: WorkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkUpdateComponent,
    resolve: {
      work: WorkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workRoute;
