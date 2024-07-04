import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonnelComponent } from './list/personnel.component';
import { PersonnelDetailComponent } from './detail/personnel-detail.component';
import { PersonnelUpdateComponent } from './update/personnel-update.component';
import PersonnelResolve from './route/personnel-routing-resolve.service';

const personnelRoute: Routes = [
  {
    path: '',
    component: PersonnelComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonnelDetailComponent,
    resolve: {
      personnel: PersonnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonnelUpdateComponent,
    resolve: {
      personnel: PersonnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonnelUpdateComponent,
    resolve: {
      personnel: PersonnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default personnelRoute;
