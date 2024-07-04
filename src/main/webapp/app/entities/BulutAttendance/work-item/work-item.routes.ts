import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkItemComponent } from './list/work-item.component';
import { WorkItemDetailComponent } from './detail/work-item-detail.component';
import { WorkItemUpdateComponent } from './update/work-item-update.component';
import WorkItemResolve from './route/work-item-routing-resolve.service';

const workItemRoute: Routes = [
  {
    path: '',
    component: WorkItemComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkItemDetailComponent,
    resolve: {
      workItem: WorkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkItemUpdateComponent,
    resolve: {
      workItem: WorkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkItemUpdateComponent,
    resolve: {
      workItem: WorkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workItemRoute;
