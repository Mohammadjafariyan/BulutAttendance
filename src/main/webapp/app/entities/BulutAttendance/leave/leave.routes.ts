import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveComponent } from './list/leave.component';
import { LeaveDetailComponent } from './detail/leave-detail.component';
import { LeaveUpdateComponent } from './update/leave-update.component';
import LeaveResolve from './route/leave-routing-resolve.service';

const leaveRoute: Routes = [
  {
    path: '',
    component: LeaveComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveDetailComponent,
    resolve: {
      leave: LeaveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveUpdateComponent,
    resolve: {
      leave: LeaveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveUpdateComponent,
    resolve: {
      leave: LeaveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default leaveRoute;
