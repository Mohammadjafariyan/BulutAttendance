import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaxTemplateComponent } from './list/tax-template.component';
import { TaxTemplateDetailComponent } from './detail/tax-template-detail.component';
import { TaxTemplateUpdateComponent } from './update/tax-template-update.component';
import TaxTemplateResolve from './route/tax-template-routing-resolve.service';

const taxTemplateRoute: Routes = [
  {
    path: '',
    component: TaxTemplateComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxTemplateDetailComponent,
    resolve: {
      taxTemplate: TaxTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxTemplateUpdateComponent,
    resolve: {
      taxTemplate: TaxTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxTemplateUpdateComponent,
    resolve: {
      taxTemplate: TaxTemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default taxTemplateRoute;
