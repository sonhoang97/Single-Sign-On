import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { User } from 'src/models/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
private apiURL = Config.getPath(PathController.Users);

  constructor(private http: HttpClient) { }

  public getProfile(): Observable<User>{
    const token = LsHelper.getTokenFromStorage();
    let headers = new HttpHeaders({
      Authorization: 'Bearer ' + token
    });

    return this.http
      .get(this.apiURL + '/profile', { headers: headers })
      .pipe(
        map((res: User) => {
          
          return res;
        })
      );
  }
}
