import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { Observable } from 'rxjs';
import { User } from 'src/models/user/user.model';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { map } from 'rxjs/operators';
import { UserProfile } from 'src/models/user/user-profile.model';
import { Role } from 'src/models/role/role.model';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private apiURL = Config.getPath(PathController.Role);
  private headers = new HttpHeaders({
    'Content-type': 'application/json',
    Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
  });
  constructor(private http: HttpClient) {}

  public getAllRoles(): Observable<Role[]> {
    return this.http
      .get(this.apiURL + '/all', {
        headers: this.headers,
      })
      .pipe(
        map((res: Role[]) => {
          return res;
        })
      );
  }

  
}
