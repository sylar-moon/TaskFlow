import { StateEnum } from "../enums/state.enum";

export class Task {
    id:number;
    name:string;
    state:StateEnum;

    constructor(id:number,name:string,state:StateEnum){
        this.id=id;
        this.name=name;
        this.state=state;
    }

}
