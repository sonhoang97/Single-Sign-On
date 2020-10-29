import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { TokenPassword } from 'src/models/token/tokenPassword';
import { User } from 'src/models/user/user.model';

export class LsHelper {
  private static readonly TOKEN_LOCAL: string = 'token';
  private static readonly USER_LOCAL: string = 'user';
  
  //Save to storage
  public static saveTokenToStorage(token: TokenPassword): void {
    localStorage.setItem(this.TOKEN_LOCAL, JSON.stringify(token));
  }
  
  public static saveUserToStorage(user: User): void {
    localStorage.setItem(this.USER_LOCAL, JSON.stringify(user));
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

  public static getUserFromStorage(): User {
    const currentUser = localStorage.getItem(this.USER_LOCAL);

    if (!currentUser || currentUser === '') {
      return undefined;
    }

    const user: User = JSON.parse(currentUser);
    return user;
  }

  public static getUserNameFromStorage(): string {
    const currentUser = localStorage.getItem(this.USER_LOCAL);

    if (!currentUser || currentUser === '') {
      return undefined;
    }

    const user: User = JSON.parse(currentUser);
    return user.username;
  }
  //Delete Storage
  public static removeTokenStorage(): void {
    localStorage.removeItem(this.TOKEN_LOCAL);
  }

  public static removeUserStorage(): void {
    localStorage.removeItem(this.USER_LOCAL);
  }

}
