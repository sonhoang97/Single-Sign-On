import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { Observable } from 'rxjs';
import { User } from 'src/models/user/user.model';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { map } from 'rxjs/operators';
import { TokenPassword } from 'src/models/token/tokenPassword';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiURL = Config.getPath(PathController.Account);

  constructor(private http: HttpClient) {}

  public register(userRegister: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      dataType: 'json',
    });
    return this.http
      .post(this.apiURL + '/register', JSON.stringify(userRegister), {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          //to do: register User
          return res;
        })
      );
  }

  public login(username: string, password: string): Observable<TokenPassword> {
    let body = new URLSearchParams();
    body.append('username', username);
    body.append('password', password);
    body.append('grant_type', 'password');

    let headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Basic ' + btoa('mobile:pin'),
    });

    return this.http
      .post('http://localhost:8083/oauth/token', body.toString(), {
        headers: headers,
      })
      .pipe(
        map((res: TokenPassword) => {
          LsHelper.saveTokenToStorage(res);
          return res;
        })
      );
  }

  public logout(): Observable<any> {
    let headers = new HttpHeaders({
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });
    const refresh_token = LsHelper.getRefreshTokenFromStorage();
    return this.http
      .delete(this.apiURL + '/log_out?refresh_token=' + refresh_token, {
        headers: headers,
      })
      .pipe(
        map((res: any) => {
          LsHelper.removeTokenStorage();
          LsHelper.removeUserStorage();
          return res;
        })
      );
  }

  public getTokenByRefreshToken(): Observable<TokenPassword> {
    let body = new URLSearchParams();
    body.append('grant_type', 'refresh_token');
    body.append('refresh_token', LsHelper.getRefreshTokenFromStorage());

    let headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Basic ' + btoa('admin:admin'),
    });

    return this.http
      .post('http://localhost:8083/oauth/token', body.toString(), {
        headers: headers,
      })
      .pipe(
        map((res: TokenPassword) => {
          return res;
        })
      );
  }

  public isAuthenticated(): Observable<any> {
    const token = LsHelper.getTokenFromStorage();
    let headers = new HttpHeaders({
      Authorization: 'Bearer ' + token,
    });
    return this.http
      .get(this.apiURL + '/isAuthenticated', { headers: headers })
      .pipe(
        map((res: any) => {
          return res;
        })
      );
  }
}
