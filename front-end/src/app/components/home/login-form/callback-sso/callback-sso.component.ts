import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-callback-sso',
  templateUrl: './callback-sso.component.html',
})
export class CallbackSsoComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      const code = params['code'];
      this.authService.loginSSO(code).subscribe(
        (res) => {
          this.checkAuthenticated();
        },
        (err) => {}
      );
    });
  }

  checkAuthenticated(): void {
    if (LsHelper.getTokenFromStorage()) {
      const authorities = LsHelper.getAuthoritiesFromToken();
      if (authorities.includes('ROLE_admin')) {
        this.router.navigate(['admin']);
      } else {
        this.router.navigate(['profile']);
      }
    }
  }
}
