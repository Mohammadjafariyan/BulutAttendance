import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HrLetterTypeComponent } from './list/hr-letter-type.component';
import { HrLetterTypeDetailComponent } from './detail/hr-letter-type-detail.component';
import { HrLetterTypeUpdateComponent } from './update/hr-letter-type-update.component';
import HrLetterTypeResolve from './route/hr-letter-type-routing-resolve.service';

const hrLetterTypeRoute: Routes = [
  {
    path: '',
    component: HrLetterTypeComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HrLetterTypeDetailComponent,
    resolve: {
      hrLetterType: HrLetterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HrLetterTypeUpdateComponent,
    resolve: {
      hrLetterType: HrLetterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HrLetterTypeUpdateComponent,
    resolve: {
      hrLetterType: HrLetterTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hrLetterTypeRoute;
