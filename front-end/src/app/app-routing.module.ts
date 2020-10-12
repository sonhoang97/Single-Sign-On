import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginFormComponent } from './home/login-form/login-form.component';
import { RegisterFormComponent } from './home/register-form/register-form.component';

const routes: Routes = [
  { path: '', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
