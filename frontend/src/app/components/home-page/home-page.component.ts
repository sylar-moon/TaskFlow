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
import { MatMenuModule } from '@angular/material/menu';
import { SortedEnum } from '../../enums/sorted.enum';


@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule, MatDialogModule, MatButtonModule,
    MatIconModule, MatPaginatorModule,
    MatTableModule, MatButtonModule, MatMenuModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})

export class HomePageComponent implements OnInit {
  name = "";
  idName!: number;
  sorted = SortedEnum.DATE_DOWN;

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
    console.log("your sort = " + this.sorted);

    this.refreshTasks(this.sorted);
  }

  refreshTasks(selectedSort: SortedEnum): void {
    console.log("your select sort = " + selectedSort);

    this.sorted = selectedSort;

    this.getMyTasks(this.sorted).subscribe((response) => {
      this.updatePaginatedNewTasks();
      this.updatePaginatedInProgressTasks();
      this.updatePaginatedCompletedTasks();
      this.totalNewTasks = this.newTasks.length;
      this.totalInProgressTasks = this.inProgresTasks.length;
      this.totalCompletedTasks = this.completedTasks.length;
    });
  }


  getMyTasks(sorted: SortedEnum): Observable<any> {
    console.log("get my task start");

    this.newTasks = [];
    this.inProgresTasks = [];
    this.completedTasks = [];

    return this.taskService.getMyTasks(sorted).pipe(
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




  updateTaskStatus(id: number): void {
    const newStatus = this.selectedStatus[id];
    console.log("your id" + id);
    console.log("your new status" + newStatus);
    this.taskService.changeTaskStatus(id, newStatus).subscribe(
      (response: any) => {
        console.log("your new id" + response.id);
        console.log("your new status" + response.status);
        this.name = response.name;
        this.idName = response.id;
        this.refreshTasks(this.sorted);
      }
    )
      ;
  }


  openTaskDialog(task: Task): void {

    const dialogRef = this.dialog.open(TaskDialogComponent, {
      width: "560px",
      height: "500px"
    })

    dialogRef.componentInstance.initTask(task);

    dialogRef.afterClosed().subscribe(result => {
      this.refreshTasks(this.sorted);
    });
  }

  changeDisable(): void {
    this.disable = !this.disable;
  }

  addTask(): void {
    const dialogRef = this.dialog.open(AddDialogComponent, {
      width: "400px",
      height: "200px"
    })

    dialogRef.afterClosed().subscribe(result => {
      this.refreshTasks(this.sorted);
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

  sortTasks(sort: string) {

    switch (sort) {
      case "DATE_UP":
        this.refreshTasks(SortedEnum.DATE_UP)
        break;
      case "DATE_DOWN":
        this.refreshTasks(SortedEnum.DATE_DOWN)
        break;
      case "STATE_UP":
        this.refreshTasks(SortedEnum.STATE_UP)
        break;
      case "STATE_DOWN":
        this.refreshTasks(SortedEnum.STATE_DOWN)
        break;
      case "NAME_UP":
        this.refreshTasks(SortedEnum.NAME_UP)
        break;
      case "NAME_DOWN":
        this.refreshTasks(SortedEnum.NAME_DOWN)
        break;
    }

  }
}
