import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { TokenPassword } from 'src/models/token/tokenPassword';
import { UserToken } from 'src/models/user/user-token.model';
import { User } from 'src/models/user/user.model';

export class LsHelper {
  private static readonly TOKEN_LOCAL: string = 'token';
  
  //Save to storage
  public static saveTokenToStorage(token: TokenPassword): void {
    localStorage.setItem(this.TOKEN_LOCAL, JSON.stringify(token));
  }

  //Get from Storage
  public static getTokenFromStorage(): any {
    const currentToken = localStorage.getItem(this.TOKEN_LOCAL);

    if (!currentToken || currentToken === '') {
      return undefined;
    }

    const token: TokenPassword = JSON.parse(currentToken);
    return token.access_token;
  }

  public static getRefreshTokenFromStorage(): any {
    const currentToken = localStorage.getItem(this.TOKEN_LOCAL);

    if (!currentToken || currentToken === '') {
      return undefined;
    }

    const token: TokenPassword = JSON.parse(currentToken);
    return token.refresh_token;
  }

  public static getUserFromStorage(): UserToken {
    const currentToken = this.getTokenFromStorage();

    if (!currentToken || currentToken === '') {
      return undefined;
    }

    const payload: string = currentToken.split('.')[1];
    const user: UserToken = JSON.parse(atob(payload)).user;
    return user;
  }

  //Delete Storage
  public static removeTokenStorage(): void {
    localStorage.removeItem(this.TOKEN_LOCAL);
  }

}
