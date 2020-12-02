import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'password',
  templateUrl: './password.component.html',
})
export class PasswordComponent implements OnInit {
  @Input() username: string;
  isValid = true;
  currentPassword = '';
  newPassword = '';
  confirmPassword = '';
  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log(this.username);
  }

  changePassword(): void {
    if (
      !this.currentPassword ||
      !this.newPassword ||
      !this.confirmPassword ||
      this.newPassword != this.confirmPassword
    ) {
      this.isValid = false;
      return;
    }

    this.authService
      .changePassword(
        this.currentPassword,
        this.newPassword,
        this.confirmPassword
      )
      .subscribe(
        (res) => {
          LsHelper.removeTokenStorage();
          this.toastr.success(Messages.SUCCESS.SUCCESS);
          this.router.navigate(['/login']);
        },
        (err) => {
          if(err.status ===409 || err.status ===404){
          this.toastr.warning(err.error);
        }
          console.log(err);
        }
      );
  }
}
