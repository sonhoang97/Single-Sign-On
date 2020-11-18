import * as fromApp from '../components/index';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { CommonModule } from '@angular/common';
import { UserValidGuard } from './guards/user-valid.guard';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const APP_COMPONENTS: any[] = [
  fromApp.AppComponent,
  fromApp.TopMenuComponent,
  fromApp.FooterComponent,
  fromApp.LoginFormComponent,
  fromApp.RegisterFormComponent,
  fromApp.HomeMenuComponent,
  fromApp.ApiDocComponent,
  fromApp.ProfileComponent,
  fromApp.UserProfileComponent,
  fromApp.ClientComponent,
  fromApp.ClientScopeComponent,
  fromApp.PasswordComponent,
  fromApp.SettingsTokenComponent,
  fromApp.RegistClientComponent,
];

const APP_POPUP_COMPONENTS: any[] = [];

const APP_SHARED_COMPONENTS: any[] = [fromApp.LoadingComponent];
@NgModule({
  declarations: [APP_COMPONENTS, APP_POPUP_COMPONENTS, APP_SHARED_COMPONENTS ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    CommonModule,
    AppRoutingModule,
    MDBBootstrapModule.forRoot(),
    HttpClientModule,
    ToastrModule.forRoot({
      timeOut: 3000,
    }),
    
  ],
  exports: [APP_POPUP_COMPONENTS],
  entryComponents: [APP_POPUP_COMPONENTS],
  providers: [UserValidGuard],
  bootstrap: [fromApp.AppComponent],
})
export class AppModule {}
