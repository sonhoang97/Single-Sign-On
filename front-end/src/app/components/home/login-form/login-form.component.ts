import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent implements OnInit {
  username: string;
  password: string;

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  submitLogin() {
    this.authService.login(this.username, this.password).subscribe(
      (res) => {
        this.toastr.success(Messages.SUCCESS.SUCCESS);
        this.router.navigate[''];
      },
      (err) => {
        if (err.status == 400) this.toastr.warning(Messages.ERROR.LOGIN_WRONG);
        else this.toastr.warning('Something wrong!');
        console.log(err);
      }
    );
  }
}
