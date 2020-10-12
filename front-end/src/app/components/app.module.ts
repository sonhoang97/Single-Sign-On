import * as fromApp from '../components/index';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

const APP_COMPONENTS: any[] = [
  fromApp.AppComponent,
  fromApp.TopMenuComponent,
  fromApp.FooterComponent,
  fromApp.LoginFormComponent,
  fromApp.RegisterFormComponent,
];

const APP_POPUP_COMPONENTS: any[] = [];

const APP_SHARED_COMPONENTS: any[] = [fromApp.LoadingComponent];
@NgModule({
  declarations: [APP_COMPONENTS, APP_POPUP_COMPONENTS, APP_SHARED_COMPONENTS],
  imports: [BrowserModule, AppRoutingModule, MDBBootstrapModule.forRoot()],
  exports: [APP_POPUP_COMPONENTS],
  entryComponents: [APP_POPUP_COMPONENTS],
  providers: [],
  bootstrap: [fromApp.AppComponent],
})
export class AppModule {}
