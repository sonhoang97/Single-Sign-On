import { ClientDetail } from '../clientDetail/client-detail';

export class UserProfile {
    public username: string;
    public email: string;
    public phoneNumber: number;
    public firstname: string;
    public lastname: string;
    public clients: ClientDetail[];
}
