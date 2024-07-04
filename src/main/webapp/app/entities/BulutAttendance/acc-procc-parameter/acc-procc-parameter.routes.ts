import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccProccParameterComponent } from './list/acc-procc-parameter.component';
import { AccProccParameterDetailComponent } from './detail/acc-procc-parameter-detail.component';
import { AccProccParameterUpdateComponent } from './update/acc-procc-parameter-update.component';
import AccProccParameterResolve from './route/acc-procc-parameter-routing-resolve.service';

const accProccParameterRoute: Routes = [
  {
    path: '',
    component: AccProccParameterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccProccParameterDetailComponent,
    resolve: {
      accProccParameter: AccProccParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccProccParameterUpdateComponent,
    resolve: {
      accProccParameter: AccProccParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccProccParameterUpdateComponent,
    resolve: {
      accProccParameter: AccProccParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accProccParameterRoute;
