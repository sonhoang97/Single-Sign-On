import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginFormComponent } from '../components/home/login-form/login-form.component';
import { RegisterFormComponent } from '../components/home/register-form/register-form.component';
import { UserValidGuard } from './guards/user-valid.guard';
import { HomeMenuComponent } from './home/home-menu/home-menu.component';
import { RegistClientDetailComponent } from './home/client-detail/regist-client-detail/regist-client-detail.component';
import { ApiDocComponent } from './docs/api.doc.component';
const routes: Routes = [
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
  { path: '', component: HomeMenuComponent },
  { path: 'client/regist-client', component: RegistClientDetailComponent },
  { path: 'doc', component: ApiDocComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
