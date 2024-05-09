
import { StateEnum } from "../enums/state.enum";

export class User {
    name!: string;
    email!: string;
    profilePictureUrl!: string | null;
    roles!: string[] | null;

    constructor(
        _name: string,
        _email: string,
        _profilePictureUrl: string | null,
        _roles: string[] | null,
    ) {
        this.email = _email;
        this.name = _name;
        this.profilePictureUrl = _profilePictureUrl;
        this.roles = _roles;
    }

    static empty(): User {
        return new User('', '',  null, null);
    }
    
    update(user: User): User {
        if (user) {
          this.name = user.name || '';
          this.email = user.email || '';
          this.profilePictureUrl = user.profilePictureUrl;
          this.roles = user.roles;  
          return this;
        }
        return User.empty();
    }

    static fromJson(json: any): User {
        if (json && json.name && json.email) {
            return new User(
                json.name,
                json.email,
                json.profilePictureUrl || null,
                json.roles || null
            );
        } else {
            return User.empty();
        }
    }
}
