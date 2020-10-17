import { TokenPassword } from 'src/models/token/tokenPassword';

export class LsHelper {
    // todo: create user model
    private static readonly TOKEN_LOCAL: string = 'token';
  
    public static saveTokenToStorage(token: TokenPassword): void {
      localStorage.setItem(this.TOKEN_LOCAL, JSON.stringify(token));
    }
  
    public static getTokenFromStorage(): any {
      const currentToken = localStorage.getItem(this.TOKEN_LOCAL);
  
      if (!currentToken || currentToken === '') {
        return undefined;
      }
  
      const token: TokenPassword = JSON.parse(currentToken);
      return token.access_token;
    }
  
    public static removeTokenStorage(): void {
      localStorage.removeItem(this.TOKEN_LOCAL);
    }
  
    
  }
  

