export class User {
    public username: string;
    public password: string;
    public email: string;
    public phonenumber: string;
    public firstname: string;
    public lastname: string;
    public accountNonExpired: boolean = true;
    public enabled: boolean = true;
    public credentialsNonExpired: boolean = true;
    public accountNonLocked: boolean = true;
    public loggedInFb: boolean = false;
}
