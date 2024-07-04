import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccProcStepComponent } from './list/acc-proc-step.component';
import { AccProcStepDetailComponent } from './detail/acc-proc-step-detail.component';
import { AccProcStepUpdateComponent } from './update/acc-proc-step-update.component';
import AccProcStepResolve from './route/acc-proc-step-routing-resolve.service';

const accProcStepRoute: Routes = [
  {
    path: '',
    component: AccProcStepComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccProcStepDetailComponent,
    resolve: {
      accProcStep: AccProcStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccProcStepUpdateComponent,
    resolve: {
      accProcStep: AccProcStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccProcStepUpdateComponent,
    resolve: {
      accProcStep: AccProcStepResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accProcStepRoute;
