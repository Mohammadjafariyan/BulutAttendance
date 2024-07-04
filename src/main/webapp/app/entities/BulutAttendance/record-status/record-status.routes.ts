import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecordStatusComponent } from './list/record-status.component';
import { RecordStatusDetailComponent } from './detail/record-status-detail.component';
import { RecordStatusUpdateComponent } from './update/record-status-update.component';
import RecordStatusResolve from './route/record-status-routing-resolve.service';

const recordStatusRoute: Routes = [
  {
    path: '',
    component: RecordStatusComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecordStatusDetailComponent,
    resolve: {
      recordStatus: RecordStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecordStatusUpdateComponent,
    resolve: {
      recordStatus: RecordStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecordStatusUpdateComponent,
    resolve: {
      recordStatus: RecordStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default recordStatusRoute;
