import { ApiWareHouse } from './api-ware-house';
import { PathController } from './commons/consts/path-controller.const';

export class Config {
  private static apiWareHouse: ApiWareHouse = new ApiWareHouse();

  public static getPath(value: string): string {
    const apiCluster: string =
      this.apiWareHouse.Protocol + this.apiWareHouse.ApiEndPoint;
    if (
      Object.keys(PathController).find((key) => PathController[key] === value)
    ) {
      return apiCluster + value;
    }
    return apiCluster;
  }

  public static getPathToken(): string {
    const apiCluster: string =
      this.apiWareHouse.Protocol + this.apiWareHouse.ApiEndPointOAuth2;
    return apiCluster + 'oauth/token';
  }

  public static getPathAuthorizeAuthorize(): string {
    const apiCluster: string =
      this.apiWareHouse.Protocol + this.apiWareHouse.ApiEndPointOAuth2;
    return apiCluster + 'oauth/authorize';
  }
    
  public static getDomain(): string {
    return this.apiWareHouse.Domain;
  }
}
