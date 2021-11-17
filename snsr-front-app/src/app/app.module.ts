import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {ClarityModule} from '@clr/angular';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {RoutingModule} from './routing/routing.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {DefaultModule} from './layouts/default/default.module';
import {PlotsComponent} from './modules/plots/plots.component';
import {SharedModule} from './shared/shared.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {JwtInterceptor} from './shared/payload/jwt.interceptor';
import {LoginComponent} from './modules/login/login.component';
import {ManagementComponent} from './modules/management/management.component';
import {SensorCreationComponent} from './modules/management/sensor-creation/sensor-creation.component';
import {BaseStationCreationComponent} from './modules/management/base-station-creation/base-station-creation.component';
import {ErrorInterceptor} from './shared/payload/error.interceptor';
import {ChartsModule} from 'ng2-charts';

@NgModule({
  declarations: [
    AppComponent,
    PlotsComponent,
    LoginComponent,
    ManagementComponent,
    SensorCreationComponent,
    BaseStationCreationComponent,
  ],
  imports: [
    BrowserModule,
    ClarityModule,
    BrowserAnimationsModule,
    RouterModule,
    RoutingModule,
    HttpClientModule,
    DefaultModule,
    SharedModule,
    ReactiveFormsModule,
    FormsModule,
    ChartsModule,

  ],
  providers: [DatePipe,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
