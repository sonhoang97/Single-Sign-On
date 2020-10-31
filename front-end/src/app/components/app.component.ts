import { Component, OnInit } from '@angular/core';
import { User } from 'src/models/user/user.model';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user/user.service';
import { LsHelper } from '../commons/helpers/ls.helper';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  title = 'front-end';
  userNameDisplay: string;
  userProfile: User = new User();
  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}
  ngOnInit(): void {
    if (LsHelper.getTokenFromStorage()) {
      this.checkAuthenticated();
    } else {
    }
  }

  checkAuthenticated(): void {
    this.authService.isAuthenticated().subscribe(
      (res) => {
        this.getProfile();
      },
      (err) => {
        if (err.status === 401) {
          this.getTokenByRefreshToken();
        }
      }
    );
  }

  getTokenByRefreshToken(): void {
    this.authService.getTokenByRefreshToken().subscribe(
      (res) => {
        LsHelper.saveTokenToStorage(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  getProfile(): void {
    if (LsHelper.getUserNameFromStorage()) {
      this.userNameDisplay = LsHelper.getUserNameFromStorage();
      return;
    }
    this.userService.getProfile().subscribe(
      (res) => {
        LsHelper.saveUserToStorage(res);
        this.userNameDisplay = LsHelper.getUserNameFromStorage();
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
