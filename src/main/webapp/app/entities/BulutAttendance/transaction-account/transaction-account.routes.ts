import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TransactionAccountComponent } from './list/transaction-account.component';
import { TransactionAccountDetailComponent } from './detail/transaction-account-detail.component';
import { TransactionAccountUpdateComponent } from './update/transaction-account-update.component';
import TransactionAccountResolve from './route/transaction-account-routing-resolve.service';

const transactionAccountRoute: Routes = [
  {
    path: '',
    component: TransactionAccountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountDetailComponent,
    resolve: {
      transactionAccount: TransactionAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionAccountRoute;
