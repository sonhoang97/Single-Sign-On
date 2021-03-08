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

  private apiEndPointOAuth2: string;
  public get ApiEndPointOAuth2(): string {
    return this.apiEndPointOAuth2;
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
      this.apiEndPoint = 'sso-sys.ap-southeast-1.elasticbeanstalk.com/api/';
      this.apiEndPointOAuth2 = 'sso-sys.ap-southeast-1.elasticbeanstalk.com/';
    } else {
      this.domain = 'none';
      this.protocol = 'http://';
      this.apiEndPoint = 'localhost:8083/api/';
      this.apiEndPointOAuth2 = 'localhost:8083/';
    }
  }
}
