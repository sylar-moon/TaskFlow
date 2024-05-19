import { Component } from '@angular/core';
import { SubtaskService } from '../../sercices/subtask.service';
@Component({
  selector: 'app-add-subtask-dialog',
  standalone: true,
  imports: [],
  templateUrl: './add-subtask-dialog.component.html',
  styleUrl: './add-subtask-dialog.component.css'
})
export class AddSubtaskDialogComponent {
  taskId= 0;
  isEmpty=true;
  newName=""

  constructor(private subtaskServise:SubtaskService){}

  initTaskId(id:number):void{
    this.taskId=id
  }

  changeName(event: Event): void {
    const target = event.target as HTMLInputElement;

    const value = target.value;

    if (value.length > 0) {
      this.isEmpty = false;
      this.newName = value;
    }
  }

  addNewSubtask(input: HTMLInputElement):void{
    console.log("add new sub start"+ this.newName);

    this.subtaskServise.addNewSubtask(this.newName,this.taskId).subscribe(
      (response: any) => {
               console.log("subtask add");
               console.log(response.name+ "task name");
               
               
      },
      (error) => {
        console.error('Error updating subtasks:', error);
      }
    );   

    input.value=""

    this.isEmpty=true

  }
}
