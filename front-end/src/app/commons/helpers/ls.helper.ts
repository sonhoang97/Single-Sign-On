import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { TokenPassword } from 'src/models/token/tokenPassword';
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

  public static getUserNameFromStorage(): string {
    const currentToken = this.getTokenFromStorage();

    if (!currentToken || currentToken === '') {
      return undefined;
    }

    const payload: string = currentToken.split('.')[1];
    const username: string = JSON.parse(atob(payload)).user_name;
    return username;
  }
  //Delete Storage
  public static removeTokenStorage(): void {
    localStorage.removeItem(this.TOKEN_LOCAL);
  }

}
