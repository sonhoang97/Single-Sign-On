import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent implements OnInit {
  username: string;
  password: string;
  url: string;
  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.checkAuthenticated();
    
  }

  submitLogin() {
    this.authService.login(this.username, this.password).subscribe(
      (res) => {
        this.toastr.success(Messages.SUCCESS.SUCCESS);
        this.router.navigate([''],{queryParams:{username:LsHelper.getUserNameFromStorage()}});
      },
      (err) => {
        if (err.status == 400) this.toastr.warning(Messages.ERROR.LOGIN_WRONG);
        else this.toastr.warning('Something wrong!');
        console.log(err);
      }
    );
  }

  checkAuthenticated():void {
    if(LsHelper.getTokenFromStorage()){
      this.router.navigate(['']);
    }
  }

  refresh(): void {
    window.location.reload();
}
}
