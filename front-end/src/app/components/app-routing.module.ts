import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginFormComponent } from '../components/home/login-form/login-form.component';
import { RegisterFormComponent } from '../components/home/register-form/register-form.component';
import { UserValidGuard } from './guards/user-valid.guard';
import { HomeMenuComponent } from './home/home-menu/home-menu.component';
import { ApiDocComponent } from './docs/api.doc.component';
import { ProfileComponent } from './home/profile/profile.component';
import { HomeAdminComponent } from './admin/home-admin/home-admin.component';
import { AdminValidGuard } from './guards/admin-valid.guard';
import { CallbackFacebookComponent } from './home/login-form/callback-facebook/callback-facebook.component';
const routes: Routes = [
  { path: '', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
  { path: 'doc', component: ApiDocComponent },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [UserValidGuard],
  },
  {
    path: 'admin',
    component: HomeAdminComponent,
    canActivate: [AdminValidGuard],
  },
  {
    path: 'auth/facebook/callback',
    component: CallbackFacebookComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
