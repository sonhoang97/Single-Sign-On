export class ClientDetail{
    clientId: string;
    clientSecret: string;
    redirectUri: string;
    tokenExpiration: number;
    refreshExpiration: number;
    scope: string[];
    authorizeGrantType: string[];
    createdAt: Date;
}