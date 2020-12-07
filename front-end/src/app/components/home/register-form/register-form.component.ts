import { Component, OnInit } from '@angular/core';
import { User } from 'src/models/user/user.model';
import { AuthService } from 'src/services/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Messages } from 'src/app/commons/consts/message.const';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
})
export class RegisterFormComponent implements OnInit {
  userRigister: User = new User();
  firstName: string;
  lastName: string;
  username: string;
  passwordConfirm: string;
  password: string;

  email: string;
  phoneNumber: string;

  invalid: boolean = false;
  wrongConfirm: boolean = false;
  constructor(private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
    ) {}

  ngOnInit(): void {}

  isInvalid(): boolean {
    if (
      !this.firstName ||
      !this.lastName ||
      !this.email ||
      !this.phoneNumber ||
      !this.password ||
      !this.passwordConfirm ||
      !this.username
    ) {
      this.invalid = true;
    } else {
      this.invalid = false;
    }
    if (this.password != this.passwordConfirm) {
      this.wrongConfirm = true;
    } else {
      this.wrongConfirm = false;
    }
    if (!this.wrongConfirm && !this.invalid) {
      return false;
    }
    return true;
  }

  submitRegiser(): void {
    if (this.isInvalid()) {
      return;
    }
    this.userRigister.firstname = this.firstName;
    this.userRigister.lastname = this.lastName;
    this.userRigister.password = this.password;
    this.userRigister.username = this.username;
    this.userRigister.email = this.email;
    this.userRigister.phonenumber = this.phoneNumber;
    console.log(this.userRigister);
    this.authService.register(this.userRigister).subscribe(
      (res) => {
        this.toastr.success(Messages.SUCCESS.SUCCESS);
        this.router.navigate(['/login']);
      },
      (err) => {
        this.toastr.warning(err.error);
        console.log(err);
      }
    );
  }
}
