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

  }

  checkAuthenticated(): void {
    
  }




}
