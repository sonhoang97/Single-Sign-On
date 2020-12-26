import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';
import { SocialAuthService, SocialUser } from 'angularx-social-login';
import {
  FacebookLoginProvider,
  GoogleLoginProvider,
} from 'angularx-social-login';
import { User } from 'src/models/user/user.model';
@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent implements OnInit {
  username: string;
  password: string;
  url: string;
  isLoading = false;

  user: SocialUser;
  loggedInFb: boolean;
  userRigister: User = new User();
  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    private socialAuthService: SocialAuthService
  ) {}

  ngOnInit(): void {
    this.checkAuthenticated();
  }

  submitLogin() {
    if (this.loggedInFb) {
      this.username = this.user.id;
      this.password = this.user.authToken;
    }
    this.isLoading = true;
    this.authService.login(this.username, this.password).subscribe(
      (res) => {
        this.isLoading = false;
        this.toastr.success(Messages.SUCCESS.SUCCESS);
        this.refresh();
      },
      (err) => {
        this.isLoading = false;
        if (err.status == 400) this.toastr.warning(Messages.ERROR.LOGIN_WRONG);
        else this.toastr.warning('Something wrong!');
        console.log(err);
      }
    );
  }

  checkAuthenticated(): void {
    if (LsHelper.getTokenFromStorage()) {
      const authorities = LsHelper.getAuthoritiesFromToken();
      if (authorities.includes('ROLE_admin')) {
        this.router.navigate(['admin']);
      } else {
        this.router.navigate(['profile']);
      }
    }
    this.responseFacebook();
  }

  signInWithFB(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  signInWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  refresh(): void {
    window.location.reload();
  }

  responseFacebook(): void {
    this.socialAuthService.authState.subscribe((user) => {
      this.user = user;
      console.log(this.user);
      this.loggedInFb = user != null;
      this.userRigister.firstname = this.user.firstName;
      this.userRigister.lastname = this.user.lastName;
      this.userRigister.password = this.user.authToken;
      this.userRigister.username = this.user.id;
      this.userRigister.email = this.user.email;
      this.userRigister.phonenumber = '';
      this.userRigister.loggedInFb = true;
      console.log(this.userRigister);
      this.authService.register(this.userRigister).subscribe(
        (res) => {
          this.submitLogin();
        },
        (err) => {
          this.toastr.warning(err.error);
          console.log(err);
        }
      );
    });
  }
}
