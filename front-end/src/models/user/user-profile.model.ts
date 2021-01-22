import { ClientDetail } from '../clientDetail/client-detail';
import { Role } from '../role/role.model';

export class UserProfile {
  public username: string;
  public email: string;
  public phonenumber: string;
  public firstname: string;
  public lastname: string;
  public lsClientDetail: ClientDetail[];
  public enabled: boolean;
  public lsRoles: Role[];
}
