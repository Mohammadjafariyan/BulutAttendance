import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountHesabComponent } from './list/account-hesab.component';
import { AccountHesabDetailComponent } from './detail/account-hesab-detail.component';
import { AccountHesabUpdateComponent } from './update/account-hesab-update.component';
import AccountHesabResolve from './route/account-hesab-routing-resolve.service';

const accountHesabRoute: Routes = [
  {
    path: '',
    component: AccountHesabComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountHesabDetailComponent,
    resolve: {
      accountHesab: AccountHesabResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountHesabUpdateComponent,
    resolve: {
      accountHesab: AccountHesabResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountHesabUpdateComponent,
    resolve: {
      accountHesab: AccountHesabResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountHesabRoute;
