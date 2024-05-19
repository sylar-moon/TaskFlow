import { StateEnum } from "../enums/state.enum";
import type { Subtask } from "./subtask.model";

export class Task {
    id:number;
    name:string;
    state:StateEnum;
    subtasks:Subtask[];

    constructor(id:number,name:string,state:StateEnum,subtasks:Subtask[]){
        this.id=id;
        this.name=name;
        this.state=state;
        this.subtasks=subtasks;
    }

}
