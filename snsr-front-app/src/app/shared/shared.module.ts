import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './components/header/header.component';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import {LineComponent} from './charts/line/line.component';
import {ClrDatagridModule, ClrSelectModule, ClrVerticalNavModule} from '@clr/angular';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {ChartsModule} from 'ng2-charts';


@NgModule({
  declarations: [
    HeaderComponent,
    SidebarComponent,
    LineComponent
  ],
    imports: [
        CommonModule,
        ClrSelectModule,
        FormsModule,
        RouterModule,
        ClrVerticalNavModule,
        ChartsModule,
        ClrDatagridModule
    ],
  exports: [
    HeaderComponent,
    SidebarComponent,
    LineComponent
  ]
})
export class SharedModule {
}
