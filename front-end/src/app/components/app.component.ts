import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/models/user/user.model';
import { AuthService } from 'src/services/auth/auth.service';
import { LsHelper } from '../commons/helpers/ls.helper';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  title = 'front-end';
  userNameDisplay: string;
  userProfile: User = new User();
  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    const token = LsHelper.getTokenFromStorage();
    if (token) {
      this.checkToken();
    }
  }

  checkToken(): void {
    if (LsHelper.isExpiredRefreshToken()) {
      LsHelper.removeTokenStorage();
      this.router.navigate(['login']);
      return;
    }
    if (LsHelper.isExpiredToken()) {
      console.log('Expired token');
      this.getTokenByRefreshToken();
    }
    setTimeout(() => {
      console.log('timeout refreshtoken');
      this.checkToken();
    }, 140000);
  }

  getTokenByRefreshToken(): void {
    this.authService.getTokenByRefreshToken().subscribe(
      (res) => {
        LsHelper.removeTokenStorage();
        LsHelper.saveTokenToStorage(res);
        // window.location.reload();
      },
      (err) => {
        LsHelper.removeTokenStorage();
        this.router.navigate(['login']);
        console.log(err);
      }
    );
  }
}
