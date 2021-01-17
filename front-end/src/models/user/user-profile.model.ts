import { ClientDetail } from '../clientDetail/client-detail';
import { Role } from '../role/role.mdel';

export class UserProfile {
    public username: string;
    public email: string;
    public phonenumber: string;
    public firstname: string;
    public lastname: string;
    public lsClientDetail: ClientDetail[];
    public enabled: boolean;
    public lsRole: Role[];
}
