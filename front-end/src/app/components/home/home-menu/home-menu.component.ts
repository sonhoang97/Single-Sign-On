import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { User } from 'src/models/user/user.model';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user/user.service';
@Component({
  selector: 'app-home-menu',
  templateUrl: './home-menu.component.html',
})
export class HomeMenuComponent implements OnInit {
  isView = false;
  username: string;
  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.queryParams['username'];
  }

  viewProfile(): void {
    this.isView = !this.isView;
  }

  loginCode() {
    this.authService.loginCode().subscribe(
      (res) => {
        console.log(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
