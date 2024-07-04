import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccountingProcedureComponent } from './list/accounting-procedure.component';
import { AccountingProcedureDetailComponent } from './detail/accounting-procedure-detail.component';
import { AccountingProcedureUpdateComponent } from './update/accounting-procedure-update.component';
import AccountingProcedureResolve from './route/accounting-procedure-routing-resolve.service';

const accountingProcedureRoute: Routes = [
  {
    path: '',
    component: AccountingProcedureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountingProcedureDetailComponent,
    resolve: {
      accountingProcedure: AccountingProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountingProcedureUpdateComponent,
    resolve: {
      accountingProcedure: AccountingProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountingProcedureUpdateComponent,
    resolve: {
      accountingProcedure: AccountingProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountingProcedureRoute;
