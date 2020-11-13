import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'top-menu',
  templateUrl: './top-menu.component.html',
})
export class TopMenuComponent implements OnInit {
  userNameDisplay: string;
  isAuthenticated = false;
  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const user = LsHelper.getUserFromStorage();
    if(user){
      this.userNameDisplay = user.username;
      this.isAuthenticated = true;
    }else {
      this.userNameDisplay = 'Account';
      this.isAuthenticated = false;
    }
  }

  logOut(): void {
    LsHelper.removeTokenStorage();
    this.refresh();
  }

  refresh(): void {
    window.location.reload();
  }
}
