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
      Authorization: 'Basic ' + btoa('javainuse-client:javainuse-secret'),
    });

    return this.http
      .post('http://localhost:8083/oauth/token', body.toString(), { headers: headers })
      .pipe(
        map((res: TokenPassword) => {
          LsHelper.saveTokenToStorage(res);
          return res;
        })
      );
  }

  public logout(): Observable<boolean> {
    return this.http.get(this.apiURL + '/logout').pipe(
      map((res: boolean) => {
        LsHelper.removeTokenStorage();
        return true;
      })
    );
  }
public home(): Observable<any> {
  let headers = new HttpHeaders({
    Authorization: 'bearer ' +LsHelper.getTokenFromStorage(),
  });
  const a = "abc";
    return this.http.post(this.apiURL + '/home',null, {headers: headers}).pipe(
      map((res: any) => {
        return res;
      })
    );
  }
  public authenticate(): Observable<TokenPassword> {
    return this.http.get(this.apiURL + '/authenticate').pipe(
      map((res: TokenPassword) => {
        LsHelper.saveTokenToStorage(res);
        return res;
      })
    );
  }
}
