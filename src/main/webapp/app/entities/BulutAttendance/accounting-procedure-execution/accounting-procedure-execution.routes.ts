import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccountingProcedureExecutionComponent } from './list/accounting-procedure-execution.component';
import { AccountingProcedureExecutionDetailComponent } from './detail/accounting-procedure-execution-detail.component';
import { AccountingProcedureExecutionUpdateComponent } from './update/accounting-procedure-execution-update.component';
import AccountingProcedureExecutionResolve from './route/accounting-procedure-execution-routing-resolve.service';

const accountingProcedureExecutionRoute: Routes = [
  {
    path: '',
    component: AccountingProcedureExecutionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountingProcedureExecutionDetailComponent,
    resolve: {
      accountingProcedureExecution: AccountingProcedureExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountingProcedureExecutionUpdateComponent,
    resolve: {
      accountingProcedureExecution: AccountingProcedureExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountingProcedureExecutionUpdateComponent,
    resolve: {
      accountingProcedureExecution: AccountingProcedureExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountingProcedureExecutionRoute;
