import { ClientDetail } from 'src/models/clientDetail/client-detail';
import { TokenPassword } from 'src/models/token/tokenPassword';
import { UserToken } from 'src/models/user/user-token.model';
import { User } from 'src/models/user/user.model';
import { Base64Converter } from './ls.base4-convert';
import { Base64 } from 'js-base64';

export class LsHelper {
  private static readonly TOKEN_LOCAL: string = 'token';

  //Save to storage
  public static saveTokenToStorage(token: TokenPassword): void {
    localStorage.setItem(this.TOKEN_LOCAL, JSON.stringify(token));
  }

  //Get from Storage
  public static getTokenFromStorage(): string {
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
    const user: UserToken = JSON.parse(Base64.decode(payload)).user;
    return user;
  }

  public static getAuthoritiesFromToken(): string[] {
    const currentToken = this.getTokenFromStorage();

    if (!currentToken || currentToken === '') {
      return undefined;
    }

    const payload: string = currentToken.split('.')[1];
    const authorities: string[] = JSON.parse(Base64.decode(payload)).authorities;
    if (!authorities) return [];
    return authorities;
  }

  //Delete Storage
  public static removeTokenStorage(): void {
    localStorage.removeItem(this.TOKEN_LOCAL);
  }

  //Check
  public static isExpiredToken(): boolean {
    const currentToken = this.getTokenFromStorage();
    if (!currentToken) {
      return null;
    }

    const payload: string = currentToken.split('.')[1];
    const expired: number = JSON.parse(Base64.decode(payload)).exp;
    return Math.floor(new Date().getTime() / 1000) >= expired;
  }

  public static isExpiredRefreshToken(): boolean {
    const currentRefreshToken = this.getRefreshTokenFromStorage();
    if (!currentRefreshToken) {
      return null;
    }

    const payload: string = currentRefreshToken.split('.')[1];
    const expired: number = JSON.parse(Base64.decode(payload)).exp;
    return Math.floor(new Date().getTime() / 1000) >= expired;
  }
}
