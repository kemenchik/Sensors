import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DefaultComponent} from './default.component';
import {DashboardComponent} from '../../modules/dashboard/dashboard.component';
import {SharedModule} from '../../shared/shared.module';
import {RouterModule} from '@angular/router';
import {ClrDatagridModule, ClrDatepickerModule, ClrSelectModule, ClrTimelineModule} from '@clr/angular';
import {ReactiveFormsModule} from '@angular/forms';
import {AgmCoreModule} from '@agm/core';


@NgModule({
  declarations: [
    DefaultComponent,
    DashboardComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ClrSelectModule,
    ClrDatagridModule,
    ClrDatepickerModule,
    ReactiveFormsModule,
    ClrTimelineModule,
    AgmCoreModule.forRoot({apiKey: 'apikey'})
  ]
})
export class DefaultModule {
}
