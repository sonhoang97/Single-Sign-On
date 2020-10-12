import * as fromApp from '../app';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

const APP_COMPONENTS: any[] = [
  fromApp.AppComponent,
  fromApp.TopMenuComponent,
  fromApp.FooterComponent,
  fromApp.LoginFormComponent,
  fromApp.RegisterFormComponent
];

const APP_POPUP_COMPONENTS: any[] = [];

@NgModule({
  declarations: [APP_COMPONENTS, APP_POPUP_COMPONENTS],
  imports: [BrowserModule, AppRoutingModule, MDBBootstrapModule.forRoot()],
  exports: [APP_POPUP_COMPONENTS],
  entryComponents: [APP_POPUP_COMPONENTS],
  providers: [],
  bootstrap: [fromApp.AppComponent],
})
export class AppModule {}
