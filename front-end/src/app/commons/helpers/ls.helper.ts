export class LsHelper {
    // todo: create user model
    private static readonly USER_LOCAL: string = 'user';
  
    public static saveUserToStorage(user: any): void {
      localStorage.setItem(this.USER_LOCAL, JSON.stringify(user));
    }
  
    public static getUserFromStorage(): any {
      const currentUser = localStorage.getItem(this.USER_LOCAL);
  
      if (!currentUser || currentUser === '') {
        return undefined;
      }
  
      const user: any = JSON.parse(currentUser);
      const u1: any = JSON.parse(atob(user.token.split('.')[1]));
  
      return u1;
    }
  
    public static removeUserStorage(): void {
      localStorage.removeItem(this.USER_LOCAL);
    }
  
    public static getToken(): any {
      const currentUser = localStorage.getItem(this.USER_LOCAL);
  
      if (!currentUser || currentUser === '') {
        return undefined;
      }
  
      const user: any = JSON.parse(currentUser);
      return user.token;
    }
  }
  

