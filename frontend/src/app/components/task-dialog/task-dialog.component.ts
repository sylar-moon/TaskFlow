import { Component, NgModule, type OnInit } from '@angular/core';
import type { Task } from '../../models/task.model';
import type { Subtask } from '../../models/subtask.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // импорт FormsModule
import { SubtaskService } from '../../sercices/subtask.service';
import { response } from 'express';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AddSubtaskDialogComponent} from '../add-subtask-dialog/add-subtask-dialog.component';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';

@Component({
  selector: 'app-task-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule,
    MatIconModule],
  templateUrl: './task-dialog.component.html',
  styleUrl: './task-dialog.component.css'
})
export class TaskDialogComponent implements OnInit {
 idTask =0;
 nameTask ="null";
 stateTask ="null";
 subtasks: Subtask[]=[];

  constructor(private subtaskServise:SubtaskService, private dialog: MatDialog){}

  ngOnInit(): void {
    this.idTask
  }

initTask(task:Task):void{
  this.nameTask=task.name
  this.idTask=task.id
  this.stateTask=task.state
  
   // this.subtasks=task.subtasks
 this.updateSubtasks(this.idTask);

  this.subtasks.forEach
}

changeComplete(subtask:Subtask):boolean{
  console.log("is complete start");
  console.log(subtask.name);
  console.log("current complete: "+subtask.complete);
  console.log(subtask.id);
   
  this.subtaskServise.changeComleteSubtask(subtask.id).subscribe(

    // (response: any) => {
    //   console.log("your new id" + response.id);
    //   console.log("your new status" + response.status);
    //   subtask.complete=response.complete((subtask: Subtask) => {
    //     console.log(subtask.complete + " this is your subtask status complete");
    //   });     
    // }
  )

  return  subtask.complete;
}

updateSubtasks(taskId: number) {

  this.subtaskServise.updateSubtasks(taskId).subscribe(
    (response: Subtask[]) => {
              this.subtasks=response;
    },
    (error) => {
      console.error('Error updating subtasks:', error);
    }
  );
}

addTask(){
  console.log("add new subtask start");
 
  console.log(this.idTask+" : your id task for add subtask ");
    
    const dialogRef = this.dialog.open(AddSubtaskDialogComponent,{
      width:"400px",
      height:"200px"
    })

    dialogRef.componentInstance.initTaskId(this.idTask)

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog add subtask was closed');
      this.updateSubtasks(this.idTask); // Вызываем метод обновления подзадач после закрытия диалога
    });
}

}
