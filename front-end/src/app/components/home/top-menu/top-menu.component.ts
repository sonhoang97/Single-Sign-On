import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'top-menu',
  templateUrl: './top-menu.component.html',
})
export class TopMenuComponent implements OnInit {
  @Input() userNameDisplay: string;
  isAuthenticated = false;
  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.setupUserNameDisplay();
  }

  setupUserNameDisplay(): void {
    if (!LsHelper.getUserFromStorage()) {
      this.userNameDisplay = 'Account';
      this.isAuthenticated = false;
    } else {
      this.isAuthenticated = true;
    }
  }

  logOut(): void {
    this.authService.logout().subscribe(
      (res) => {
        this.setupUserNameDisplay();
        this.refresh();
      },
      (err) => {
        console.log(err);
      }
    );
  }

  refresh(): void {
    window.location.reload();
}
}
