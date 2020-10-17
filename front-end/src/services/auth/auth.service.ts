import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
  public register(userRegister: User): Observable<any> {
    return this.http.post(this.apiURL + '/register', { userRegister }).pipe(
      map((res: any) => {
        //to do: register User
        return res;
      })
    );
  }

  public login(username: string, password: string): Observable<TokenPassword> {
        //to do: setup grant type,...
    return this.http.post(this.apiURL + '/login', { username, password }).pipe(
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

  public authenticate(): Observable<TokenPassword> {
    return this.http.get(this.apiURL + '/authenticate').pipe(
      map((res: TokenPassword) => {
        LsHelper.saveTokenToStorage(res);
        return res;
      })
    );
  }
}
