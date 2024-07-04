import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HrLetterParameterComponent } from './list/hr-letter-parameter.component';
import { HrLetterParameterDetailComponent } from './detail/hr-letter-parameter-detail.component';
import { HrLetterParameterUpdateComponent } from './update/hr-letter-parameter-update.component';
import HrLetterParameterResolve from './route/hr-letter-parameter-routing-resolve.service';

const hrLetterParameterRoute: Routes = [
  {
    path: '',
    component: HrLetterParameterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HrLetterParameterDetailComponent,
    resolve: {
      hrLetterParameter: HrLetterParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HrLetterParameterUpdateComponent,
    resolve: {
      hrLetterParameter: HrLetterParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HrLetterParameterUpdateComponent,
    resolve: {
      hrLetterParameter: HrLetterParameterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hrLetterParameterRoute;
