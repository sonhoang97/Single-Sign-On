import { Component, OnInit } from '@angular/core';
import { User } from 'src/models/user/user.model';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
})
export class RegisterFormComponent implements OnInit {

  userRigister: User = new User();
  firstName: string;
  lastName: string;
  password: string;
  email: string;
  phoneNumber: number;
  constructor() { }

  ngOnInit(): void {
  }

  submitRegiser(): void{
    this.userRigister.firstname = this.firstName;
    this.userRigister.lastname = this.lastName;
    this.userRigister.email = this.email;
    this.userRigister.phonenumber = this.phoneNumber;
    console.log(this.userRigister);
  }
}
