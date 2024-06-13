import { Component } from '@angular/core';
import { SubtaskService } from '../../sercices/subtask.service';
import { TaskService } from '../../sercices/task.service';
import { CommonModule } from "@angular/common";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogRef } from '@angular/material/dialog';
import { response } from 'express';

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
  taskNameForAdd = ""
  taskNameForEdit = "";
  isSubtask = false
  isEditTask = false

  constructor(private subtaskServise: SubtaskService, private taskService: TaskService,
    private dialogRef: MatDialogRef<AddDialogComponent>
  ) { }

  initForAddSubtask(id: number): void {
    this.taskId = id
    this.isSubtask = true;
  }

  initForChangeName(name: string,id:number): void {
    this.taskId = id
    this.isEditTask = true;
    console.log("task name - " + name);
    this.taskNameForEdit = name;

  }

  changeName(event: Event): void {
    const target = event.target as HTMLInputElement;

    const value = target.value;

    if (value.length > 0) {
      this.isEmpty = false;
      this.taskNameForAdd = value;
    }
  }

  addNewSubtask(input: HTMLInputElement): void {
    console.log("add new sub start" + this.taskNameForAdd);

    this.subtaskServise.addNewSubtask(this.taskNameForAdd, this.taskId).subscribe(
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
    console.log("add new sub start" + this.taskNameForAdd);

    this.taskService.addNewTask(this.taskNameForAdd).subscribe(
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

  close(): void {
    console.log("close");
    this.dialogRef.close();

  }

  editTaskName(input: HTMLInputElement): void {
    this.taskService.changeTaskName(this.taskId, input.value).subscribe((response) => {

      this.dialogRef.close(input.value);
    })



  }
}
