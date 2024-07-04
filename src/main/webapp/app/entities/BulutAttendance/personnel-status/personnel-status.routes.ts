import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonnelStatusComponent } from './list/personnel-status.component';
import { PersonnelStatusDetailComponent } from './detail/personnel-status-detail.component';
import { PersonnelStatusUpdateComponent } from './update/personnel-status-update.component';
import PersonnelStatusResolve from './route/personnel-status-routing-resolve.service';

const personnelStatusRoute: Routes = [
  {
    path: '',
    component: PersonnelStatusComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonnelStatusDetailComponent,
    resolve: {
      personnelStatus: PersonnelStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonnelStatusUpdateComponent,
    resolve: {
      personnelStatus: PersonnelStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonnelStatusUpdateComponent,
    resolve: {
      personnelStatus: PersonnelStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default personnelStatusRoute;
