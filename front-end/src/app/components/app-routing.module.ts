import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginFormComponent } from '../components/home/login-form/login-form.component';
import { RegisterFormComponent } from '../components/home/register-form/register-form.component';
import { UserValidGuard } from './guards/user-valid.guard';
import { HomeMenuComponent } from './home/home-menu/home-menu.component';
import { ApiDocComponent } from './docs/api.doc.component';
import {ProfileComponent} from './home/profile/profile.component'
const routes: Routes = [
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
  { path: '', component: HomeMenuComponent },
  { path: 'doc', component: ApiDocComponent },
  { path: 'profile', component: ProfileComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
