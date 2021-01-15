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
import { AdminValidGuard } from './guards/admin-valid.guard';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import {NgxPaginationModule} from 'ngx-pagination'
// MDB Angular Free
import {
  CheckboxModule,
  WavesModule,
  ButtonsModule,
} from 'angular-bootstrap-md';
import {
  SocialLoginModule,
  SocialAuthServiceConfig,
} from 'angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider,
} from 'angularx-social-login';

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
  fromApp.HomeAdminComponent,
  fromApp.CallbackFacebookComponent,
  fromApp.RoleAdminComponent,
  fromApp.UserAdminComponent
];

const APP_POPUP_COMPONENTS: any[] = [];

const APP_SHARED_COMPONENTS: any[] = [
  fromApp.LoadingComponent,
  fromApp.PasswordStrengthBarComponent,
];
@NgModule({
  declarations: [APP_COMPONENTS, APP_POPUP_COMPONENTS, APP_SHARED_COMPONENTS],
  imports: [
    BrowserModule,
    ModalModule.forRoot(),
    BrowserAnimationsModule,
    AccordionModule.forRoot(),
    FormsModule,
    CommonModule,
    AppRoutingModule,
    MDBBootstrapModule.forRoot(),
    HttpClientModule,
    ToastrModule.forRoot({
      timeOut: 3000,
    }),
    SocialLoginModule,
    TooltipModule.forRoot(),
    CheckboxModule,
    WavesModule,
    ButtonsModule,
    NgxPaginationModule
  ],
  exports: [APP_POPUP_COMPONENTS],
  entryComponents: [APP_POPUP_COMPONENTS],
  providers: [
    UserValidGuard,
    AdminValidGuard,
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('848039156015832'),
          },
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '761570297587-529p6ssv5pjfcmna5jo8nts5hj4945sg.apps.googleusercontent.com'
            ),
          },
        ],
      } as SocialAuthServiceConfig,
    },
  ],
  bootstrap: [fromApp.AppComponent],
})
export class AppModule {}
