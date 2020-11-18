import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { Observable } from 'rxjs';
import { User } from 'src/models/user/user.model';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { map } from 'rxjs/operators';
import { UserProfile } from 'src/models/user/user-profile.model';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private apiURL = Config.getPath(PathController.Account);
  constructor(private http: HttpClient) {}

  public getProfile(): Observable<UserProfile> {
    let headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    return this.http
      .get(this.apiURL + '/profile', {
        headers: headers,
      })
      .pipe(
        map((res: UserProfile) => {
          return res;
        })
      );
  }
}
