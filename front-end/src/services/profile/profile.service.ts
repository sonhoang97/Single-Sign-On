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
    const headers = new HttpHeaders({
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

  public getUser(username: string): Observable<UserProfile> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    return this.http
      .get(this.apiURL + '/user?username=' + username, {
        headers: headers,
      })
      .pipe(
        map((res: UserProfile) => {
          return res;
        })
      );
  }

  public updateProfile(
    firstname: string,
    lastname: string,
    email: string,
    phonenumber: string
  ): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    let bodyProfile = {
      firstname,
      lastname,
      email,
      phonenumber,
    };
    return this.http
      .put(this.apiURL + '/profile', JSON.stringify(bodyProfile), {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }

  public getUsersAdmin(
    searchString: string,
    status: number,
    roleId: number,
    sortType: number,
    pageIndex: number,
    pageSize: number
  ): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    const url =
      this.apiURL +
      '/getUsers/' +
      sortType +
      '/' +
      pageIndex +
      '/' +
      pageSize +
      '?searchString=' +
      searchString +
      '&status=' +
      status +
      '&roleId=' +
      roleId;

    return this.http
      .get(url, {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }

  public banUser(username: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    let body = {
      username,
    };
    return this.http
      .post(this.apiURL + '/banUser', JSON.stringify(body), {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }

  public activeUser(username: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    let body = {
      username,
    };
    return this.http
      .post(this.apiURL + '/activeUser', JSON.stringify(body), {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }

  public editRole(username: string, roleId: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    let bodyRole = {
      username,
      roleId,
    };
    return this.http
      .post(this.apiURL + '/editRole', JSON.stringify(bodyRole), {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }
}
