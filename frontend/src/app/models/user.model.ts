import { StateEnum } from "../enums/state.enum";


export class User {
    id!: number;
    email!: string;
    name!: string;
    profilePictureUrl!: string;
    roles!: string[];

    constructor(
        _id: number,
        _email: string,
        _name: string,
        _profilePictureUrl: string,
        _roles: string[],
    ) {
        this.id = _id;
        this.email = _email;
        this.name = _name;
        this.profilePictureUrl = _profilePictureUrl;
        this.roles = _roles;
    }

    static empty(): User {
        return new User(0, '', '',  '', []);
      }
    
      update(user: User): User {
        if (user) {
          this.id = user.id;
          this.email = user.email;
          this.name =user.name;
          this.profilePictureUrl =user.profilePictureUrl;
          this.roles = user.roles;  
          return this;
        }
        return User.empty();
      }
    

    static fromJson(json: any): User {
        return new User(
            json.id,
            json.email,
            json.name,
            json.profilePictureUrl,
            json.roles
        );
    }
}