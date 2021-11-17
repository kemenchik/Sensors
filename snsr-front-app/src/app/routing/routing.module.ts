import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DefaultComponent} from '../layouts/default/default.component';
import {DashboardComponent} from '../modules/dashboard/dashboard.component';
import {PlotsComponent} from '../modules/plots/plots.component';
import {LoginComponent} from '../modules/login/login.component';
import {AuthGuardModule} from '../shared/auth-guard/auth-guard.module';
import {ManagementComponent} from '../modules/management/management.component';

const routes: Routes = [
  {
    path: '',
    component: DefaultComponent,
    canActivate: [AuthGuardModule],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuardModule]
      },
      {
        path: 'plots',
        component: PlotsComponent
      },
      {
        path: 'management',
        component: ManagementComponent
      },
      {path: '', redirectTo: '/login', pathMatch: 'full'}
    ]
  },
  {
    path: 'login',
    component: LoginComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class RoutingModule {
}
