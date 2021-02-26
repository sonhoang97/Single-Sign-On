import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LsHelper } from '../../commons/helpers/ls.helper';

@Injectable()
export class AdminValidGuard implements CanActivate {
  constructor(private router: Router) {}

  public canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | boolean {
    const authorities = LsHelper.getAuthoritiesFromToken();
    if (authorities.includes("ROLE_admin") || authorities.includes("read_adminpage")) {
      return true;
    } else {
      this.router.navigate(['/profile']);
      return false;
    }
  }
}
