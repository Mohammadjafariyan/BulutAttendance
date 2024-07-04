import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountTemplateComponent } from './list/account-template.component';
import { AccountTemplateDetailComponent } from './detail/account-template-detail.component';
import { AccountTemplateUpdateComponent } from './update/account-template-update.component';
import AccountTemplateResolve from './route/account-template-routing-resolve.service';

const accountTemplateRoute: Routes = [
  {
    path: '',
    component: AccountTemplateComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountTemplateDetailComponent,
    resolve: {
      accountTemplate: AccountTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountTemplateUpdateComponent,
    resolve: {
      accountTemplate: AccountTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountTemplateUpdateComponent,
    resolve: {
      accountTemplate: AccountTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountTemplateRoute;
