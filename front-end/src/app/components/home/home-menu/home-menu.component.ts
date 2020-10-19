import { Component, OnInit } from '@angular/core';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';
@Component({
  selector: 'app-home-menu',
  templateUrl: './home-menu.component.html',
})
export class HomeMenuComponent implements OnInit {
  token: string;
  validate = false;
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.home().subscribe(
      (res) => {
        console.log(res);
        if (LsHelper.getTokenFromStorage()) {
          this.validate = true;
          this.token = LsHelper.getTokenFromStorage();
        }
      },
      (err) => {
        console.log(err);
      }
    );
    
  }
}
