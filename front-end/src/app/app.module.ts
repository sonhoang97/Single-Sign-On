import * as fromApp from '../app';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';

const APP_COMPONENTS: any[] = [
  fromApp.AppComponent,
  fromApp.TopMenuComponent,
  fromApp.FooterComponent,
];

const APP_POPUP_COMPONENTS: any[] = [];

@NgModule({
  declarations: [APP_COMPONENTS, APP_POPUP_COMPONENTS],
  imports: [BrowserModule, AppRoutingModule],
  exports: [APP_POPUP_COMPONENTS],
  entryComponents: [APP_POPUP_COMPONENTS],
  providers: [],
  bootstrap: [fromApp.AppComponent],
})
export class AppModule {}
