import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { Observable } from 'rxjs';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { map } from 'rxjs/operators';
import { Permission } from 'src/models/role/permission.model';

@Injectable({
  providedIn: 'root'
})
export class PermissionServiceService {
  private apiURL = Config.getPath(PathController.Permission);
  private headers = new HttpHeaders({
    'Content-type': 'application/json',
    Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
  });
  constructor(private http: HttpClient) { }

  public getAllPermissions(): Observable<Permission[]> {
    return this.http
      .get(this.apiURL, {
        headers: this.headers,
      })
      .pipe(
        map((res: Permission[]) => {
          return res;
        })
      );
  }
}
