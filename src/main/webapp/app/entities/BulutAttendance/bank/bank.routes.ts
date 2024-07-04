import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankComponent } from './list/bank.component';
import { BankDetailComponent } from './detail/bank-detail.component';
import { BankUpdateComponent } from './update/bank-update.component';
import BankResolve from './route/bank-routing-resolve.service';

const bankRoute: Routes = [
  {
    path: '',
    component: BankComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankDetailComponent,
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankUpdateComponent,
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankUpdateComponent,
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankRoute;
