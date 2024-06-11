import { StateEnum } from "../enums/state.enum";

export class Subtask {
    id:number;
    name:string;
    complete:boolean;

    constructor(id:number,name:string,complete:boolean){
        
        this.id=id;
        this.name=name;
        this.complete=complete;
    }

}
