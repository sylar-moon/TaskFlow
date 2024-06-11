import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit,  ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { StateEnum } from '../../enums/state.enum';
import { Task } from '../../models/task.model';
import { TaskService } from '../../sercices/task.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { log } from 'console';
import type { Subtask } from '../../models/subtask.model';
import { MatIconModule } from '@angular/material/icon';
import { AddDialogComponent } from '../add-dialog/add-dialog.component';

// import { BrowserModule } from '@angular/platform-browser';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule, MatDialogModule, MatButtonModule,
    MatIconModule, MatPaginatorModule,
    MatTableModule, MatButtonModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})

export class HomePageComponent implements OnInit {
  name = "";
  idName!: number;
  newName = "";
  numberGetName!: number;
  disable = true;
  disableId = true;
  selectedStatus: { [key: number]: string } = {};

  newTasks!: Task[];
  inProgresTasks!: Task[];
  completedTasks!: Task[];
  closedTasks!: Task[];

  newTasksDataSource = new MatTableDataSource<Task>(this.newTasks);
  inProgresTasksDataSource = new MatTableDataSource<Task>(this.inProgresTasks);
  completedTasksDataSource = new MatTableDataSource<Task>(this.completedTasks);

  @ViewChild('newTasksPaginator') newTasksPaginator!: MatPaginator;
  @ViewChild('inProgresTasksPaginator') inProgresTasksPaginator!: MatPaginator;
  @ViewChild('completedTasksPaginator') completedTasksPaginator!: MatPaginator;


  constructor(private http: HttpClient, private dialog: MatDialog, private taskService: TaskService) {

  }


  ngOnInit(): void {
    this.newTasksDataSource.paginator = this.newTasksPaginator;
    this.inProgresTasksDataSource.paginator = this.inProgresTasksPaginator;
    this.completedTasksDataSource.paginator = this.completedTasksPaginator;
    this.getMyTasks();

  }

  changeIdGetName(event: Event): void {
    const target = event.target as HTMLInputElement;

    const value: number = parseInt(target.value);
    if (value != null) {
      this.disableId = false;
      this.numberGetName = value;
    } else {
      this.disableId = true;
    }
  }

  changeName(event: Event): void {
    const target = event.target as HTMLInputElement;

    const value = target.value;

    if (value.length > 0) {
      this.disable = false;
      this.newName = value;
    }
  }

  addNewName(input: HTMLInputElement): void {

    const userData = {
      name: this.newName

    };
    console.log(this.newName + "- this new task");


    this.http.post<any>("http://localhost:7000/api/tasks", userData).subscribe(
      (response: any) => {
        this.name = response.name;
        this.idName = response.id;
        this.newName = "";
        this.disable = true;
        input.value = "";
        this.getMyTasks();
      },
      (error: any) => {
        // Обработка ошибок, если необходимо
        console.error("Error:", error);
      }
    );
  }

  getName(input: HTMLInputElement): void {
    if (this.numberGetName != null) {
      this.http.get<any>("http://localhost:7000/api/tasks/" + this.numberGetName).subscribe(

        (response: any) => {
          this.name = response.name;
          this.idName = response.id;
          this.disableId = true;
          input.value = "";
        }
      )

        ;
    }

  }

  getMyTasks(): void {

    this.http.get<any>("http://localhost:7000/api/tasks/my").subscribe(

      (response) => {
        if (response) {

          this.newTasks = [];

          this.inProgresTasks = [];
          this.completedTasks = [];
          this.closedTasks = [];
          console.log(this.newTasks.length);

          response.forEach((element: { id: number, name: string, state: string, subtasks: Subtask[] }) => {
            const task: Task = new Task(element.id, element.name,
              StateEnum[element.state as keyof typeof StateEnum], element.subtasks);

            switch (task.state) {
              case StateEnum.NEW: this.newTasks.push(task);
                break;
              case StateEnum.IN_PROGRESS: this.inProgresTasks.push(task);
                break;
              case StateEnum.COMPLETED: this.completedTasks.push(task);
                break;
              case StateEnum.CLOSED: this.closedTasks.push(task);
            }
          });
        }
      }
    )

      ;
  }


  updateTaskStatus(id: number): void {
    const newStatus = this.selectedStatus[id];
    console.log("your id" + id);
    console.log("your new status" + newStatus);

    this.http.patch<any>("http://localhost:7000/api/tasks/" + id + "?taskState=" + newStatus, "").subscribe(

      (response: any) => {
        console.log("your new id" + response.id);
        console.log("your new status" + response.status);
        this.name = response.name;
        this.idName = response.id;
        this.getMyTasks();
      }
    )
      ;
  }


  openTaskDialog(task: Task): void {
    console.log(task.name);

    const dialogRef = this.dialog.open(TaskDialogComponent, {
      width: "560px",
      height: "500px"
    })

    dialogRef.componentInstance.initTask(task)
  }

  changeDisable(): void {
    this.disable = !this.disable;
  }

  addTask(): void {
    console.log("add task start");

    // this.taskService.addNewTask().subscribe

    const dialogRef = this.dialog.open(AddDialogComponent, {
      width: "400px",
      height: "200px"
    })

    // dialogRef.componentInstance.initTaskId(this.idTask)

    dialogRef.afterClosed().subscribe(result => {
      this.getMyTasks(); // Вызываем метод обновления подзадач после закрытия диалога
    });

  }
}
