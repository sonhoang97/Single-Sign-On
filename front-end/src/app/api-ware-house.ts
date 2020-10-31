import { environment } from '../environments/environment';

export class ApiWareHouse {
  private domain: string;
  public get Domain(): string {
    return this.domain;
  }

  private protocol: string;
  public get Protocol(): string {
    return this.protocol;
  }

  private apiEndPoint: string;
  public get ApiEndPoint(): string {
    return this.apiEndPoint;
  }


  private appVersion: string;
  public get Version(): string {
    return this.appVersion;
  }

  //to do: Add domain when hosted
  constructor() {
    this.appVersion = '1.0.0.0';
    if (environment.production) {
      this.domain = 'none';
      this.protocol = 'http://';
      this.apiEndPoint = 'none/api/';
    } else {
      this.domain = 'none';
      this.protocol = 'http://';
      this.apiEndPoint = 'localhost:8083/api/';
    }
  }
}
