import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PathController } from 'src/app/commons/consts/path-controller.const';
import { Config } from 'src/app/config';
import { Observable } from 'rxjs';
import { User } from 'src/models/user/user.model';
import { LsHelper } from 'src/app/commons/helpers/ls.helper';
import { map } from 'rxjs/operators';
import { TokenPassword } from 'src/models/token/tokenPassword';
import { ClientDetail } from 'src/models/clientDetail/client-detail';

@Injectable({
  providedIn: 'root'
})
export class ClientDetailService {
  private apiURL = Config.getPath(PathController.Client);

  constructor(private http: HttpClient) { }

  public register(clientId: string,redirectUri: string): Observable<ClientDetail> {
    let headers = new HttpHeaders({
      'Content-type': 'application/json',
      Authorization: 'bearer ' + LsHelper.getTokenFromStorage(),
    });

    let clientDetail = {
      clientId,redirectUri
    }

    return this.http
      .post(this.apiURL + '/createClientDetail',JSON.stringify(clientDetail), {
        headers: headers,
      })
      .pipe(
        map((res: ClientDetail) => {
          return res;
        })
      );
  }
}