import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ParameterComponent } from './list/parameter.component';
import { ParameterDetailComponent } from './detail/parameter-detail.component';
import { ParameterUpdateComponent } from './update/parameter-update.component';
import ParameterResolve from './route/parameter-routing-resolve.service';

const parameterRoute: Routes = [
  {
    path: '',
    component: ParameterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParameterDetailComponent,
    resolve: {
      parameter: ParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParameterUpdateComponent,
    resolve: {
      parameter: ParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParameterUpdateComponent,
    resolve: {
      parameter: ParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parameterRoute;
