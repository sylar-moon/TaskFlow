import { Component } from '@angular/core';
import type { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-dialog',
  standalone: true,
  imports: [],
  templateUrl: './task-dialog.component.html',
  styleUrl: './task-dialog.component.css'
})
export class TaskDialogComponent {
 idTask =0;
 nameTask ="null";
 stateTask ="null";

initTask(task:Task):void{
  this.nameTask=task.name
  this.idTask=task.id
  this.stateTask=task.state
}
}
