import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HrLetterComponent } from './list/hr-letter.component';
import { HrLetterDetailComponent } from './detail/hr-letter-detail.component';
import { HrLetterUpdateComponent } from './update/hr-letter-update.component';
import HrLetterResolve from './route/hr-letter-routing-resolve.service';

const hrLetterRoute: Routes = [
  {
    path: '',
    component: HrLetterComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HrLetterDetailComponent,
    resolve: {
      hrLetter: HrLetterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HrLetterUpdateComponent,
    resolve: {
      hrLetter: HrLetterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HrLetterUpdateComponent,
    resolve: {
      hrLetter: HrLetterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hrLetterRoute;
