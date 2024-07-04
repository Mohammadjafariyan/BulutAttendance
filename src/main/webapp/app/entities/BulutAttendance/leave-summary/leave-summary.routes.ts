import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveSummaryComponent } from './list/leave-summary.component';
import { LeaveSummaryDetailComponent } from './detail/leave-summary-detail.component';
import { LeaveSummaryUpdateComponent } from './update/leave-summary-update.component';
import LeaveSummaryResolve from './route/leave-summary-routing-resolve.service';

const leaveSummaryRoute: Routes = [
  {
    path: '',
    component: LeaveSummaryComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveSummaryDetailComponent,
    resolve: {
      leaveSummary: LeaveSummaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveSummaryUpdateComponent,
    resolve: {
      leaveSummary: LeaveSummaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveSummaryUpdateComponent,
    resolve: {
      leaveSummary: LeaveSummaryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default leaveSummaryRoute;
