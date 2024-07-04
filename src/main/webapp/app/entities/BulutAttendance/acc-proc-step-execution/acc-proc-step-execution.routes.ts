import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccProcStepExecutionComponent } from './list/acc-proc-step-execution.component';
import { AccProcStepExecutionDetailComponent } from './detail/acc-proc-step-execution-detail.component';
import { AccProcStepExecutionUpdateComponent } from './update/acc-proc-step-execution-update.component';
import AccProcStepExecutionResolve from './route/acc-proc-step-execution-routing-resolve.service';

const accProcStepExecutionRoute: Routes = [
  {
    path: '',
    component: AccProcStepExecutionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccProcStepExecutionDetailComponent,
    resolve: {
      accProcStepExecution: AccProcStepExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccProcStepExecutionUpdateComponent,
    resolve: {
      accProcStepExecution: AccProcStepExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccProcStepExecutionUpdateComponent,
    resolve: {
      accProcStepExecution: AccProcStepExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accProcStepExecutionRoute;
