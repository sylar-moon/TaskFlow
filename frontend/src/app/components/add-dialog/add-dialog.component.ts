import { Component } from '@angular/core';
import { SubtaskService } from '../../sercices/subtask.service';
import { TaskService } from '../../sercices/task.service';
import { CommonModule } from "@angular/common";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-add-subtask-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule],
  templateUrl: './add-dialog.component.html',
  styleUrl: './add-dialog.component.css'
})
export class AddDialogComponent {
  taskId = 0;
  isEmpty = true;
  newName = ""
  isSubtask = false

  constructor(private subtaskServise: SubtaskService, private taskService: TaskService,
    private dialogRef: MatDialogRef<AddDialogComponent>
    ) { }

  initTaskId(id: number): void {
    this.taskId = id
    this.isSubtask = true;
  }

  changeName(event: Event): void {
    const target = event.target as HTMLInputElement;

    const value = target.value;

    if (value.length > 0) {
      this.isEmpty = false;
      this.newName = value;
    }
  }

  addNewSubtask(input: HTMLInputElement): void {
    console.log("add new sub start" + this.newName);

    this.subtaskServise.addNewSubtask(this.newName, this.taskId).subscribe(
      (response: any) => {
        console.log("subtask add");
        console.log(response.name + "task name");


      },
      (error) => {
        console.error('Error updating subtasks:', error);
      }
    );

    input.value = ""

    this.isEmpty = true

  }

  addNewTask(input: HTMLInputElement): void {
    console.log("add new sub start" + this.newName);

    this.taskService.addNewTask(this.newName).subscribe(
      (response: any) => {
        console.log("task add");
        console.log(response.name + "task name");


      },
      (error) => {
        console.error('Error updating tasks:', error);
      }
    );

    input.value = ""

    this.isEmpty = true

  }

  close():void{
    console.log("close");
    this.dialogRef.close();

  }
}
