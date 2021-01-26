import { Permission } from "./permission.model";

export class Role {
  
    public name: string;
    public id: number;
    public permissions: Permission[];
    public usernames: string[];
}
