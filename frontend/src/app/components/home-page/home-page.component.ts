import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
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

import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

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

  paginatedNewTasks: Task[] = [];
  paginatedInProgressTasks: Task[] = [];
  paginatedCompletedTasks: Task[] = [];

  totalNewTasks = 0;
  totalInProgressTasks = 0;
  totalCompletedTasks = 0;

  pageSizeNewTask = 10;
  pageSizeInProgressTask = 10;
  pageSizeCompletedTask = 10;

  pageNewTask = 0;
  pageInProgressTask = 0;
  pageCompletedTask = 0;


  newTasksDataSource = new MatTableDataSource<Task>(this.newTasks);
  inProgresTasksDataSource = new MatTableDataSource<Task>(this.inProgresTasks);
  completedTasksDataSource = new MatTableDataSource<Task>(this.completedTasks);

  @ViewChild('newTasksPaginator') newTasksPaginator!: MatPaginator;
  @ViewChild('inProgresTasksPaginator') inProgresTasksPaginator!: MatPaginator;
  @ViewChild('completedTasksPaginator') completedTasksPaginator!: MatPaginator;


  constructor(private http: HttpClient, private dialog: MatDialog, private taskService: TaskService) {
    this.refreshTasks();
  }

  refreshTasks(): void {
    this.getMyTasks().subscribe((response) => {
      this.updatePaginatedNewTasks();
      this.updatePaginatedInProgressTasks();
      this.updatePaginatedCompletedTasks();
      this.totalNewTasks = this.newTasks.length;
      this.totalInProgressTasks = this.inProgresTasks.length;
      this.totalCompletedTasks = this.completedTasks.length;
    });
  }


  getMyTasks(): Observable<any> {
    console.log("get my task start");

    this.newTasks = [];
    this.inProgresTasks = [];
    this.completedTasks = [];

    return this.taskService.getMyTasks().pipe(
      tap((response: any) => {
        if (response) {
          response.forEach((element: { id: number; name: string; state: string; subtasks: Subtask[] }) => {
            const task: Task = new Task(element.id, element.name,
              StateEnum[element.state as keyof typeof StateEnum], element.subtasks);

            switch (task.state) {
              case StateEnum.NEW:
                this.newTasks.push(task);
                break;
              case StateEnum.IN_PROGRESS:
                this.inProgresTasks.push(task);
                break;
              case StateEnum.COMPLETED:
                this.completedTasks.push(task);
                break;
            }
          });
        }

      })

    );
  }


  ngOnInit(): void {
    this.newTasksDataSource.paginator = this.newTasksPaginator;
    this.inProgresTasksDataSource.paginator = this.inProgresTasksPaginator;
    this.completedTasksDataSource.paginator = this.completedTasksPaginator;

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
        this.refreshTasks;
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
        this.refreshTasks();
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

    const dialogRef = this.dialog.open(AddDialogComponent, {
      width: "400px",
      height: "200px"
    })

    dialogRef.afterClosed().subscribe(result => {
      this.refreshTasks(); // Вызываем метод обновления подзадач после закрытия диалога
    });

  }

  updatePaginatedNewTasks() {
    const start = this.pageNewTask * this.pageSizeNewTask;
    const end = start + this.pageSizeNewTask;
    this.paginatedNewTasks = this.newTasks.slice(start, end);
  }

  updatePaginatedInProgressTasks() {
    const start = this.pageInProgressTask * this.pageSizeInProgressTask;
    const end = start + this.pageSizeInProgressTask;
    this.paginatedInProgressTasks = this.inProgresTasks.slice(start, end);
  }

  updatePaginatedCompletedTasks() {
    const start = this.pageCompletedTask * this.pageSizeCompletedTask;
    const end = start + this.pageSizeCompletedTask;
    this.paginatedCompletedTasks = this.completedTasks.slice(start, end);
  }

  onPageChangeNewTask(event: PageEvent) {
    this.pageSizeNewTask = event.pageSize;
    this.pageNewTask = event.pageIndex;
    this.updatePaginatedNewTasks();
  }

  onPageChangeInProgressTask(event: PageEvent) {
    this.pageSizeInProgressTask = event.pageSize;
    this.pageInProgressTask = event.pageIndex;
    this.updatePaginatedInProgressTasks();
  }

  onPageChangeCompletedTask(event: PageEvent) {
    this.pageSizeCompletedTask = event.pageSize;
    this.pageCompletedTask = event.pageIndex;
    this.updatePaginatedCompletedTasks();
  }
}
