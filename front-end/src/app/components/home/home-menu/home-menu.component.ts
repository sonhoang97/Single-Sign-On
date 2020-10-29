import { Component, Input, OnInit } from '@angular/core';
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
  userProfile: User;
  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {}

  viewProfile(): void {
    this.isView = !this.isView;
    this.userProfile = LsHelper.getUserFromStorage();
  }
}
