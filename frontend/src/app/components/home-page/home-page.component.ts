import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { response } from 'express';
import { FormsModule } from '@angular/forms';
import { Task } from '../../models/task.model';
import { StateEnum } from '../../enums/state.enum';
@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
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

  constructor(private http: HttpClient) {

  }


  ngOnInit(): void {
    this.getAllTasks();

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

    this.http.post<any>("https://localhost:5000/api/tasks", this.newName).subscribe(
      (response: any) => {
        this.name = response.name;
        this.idName = response.id;
        this.newName = "";
        this.disable = true;
        input.value = "";
        this.getAllTasks();
      },
      (error: any) => {
        // Обработка ошибок, если необходимо
        console.error("Error:", error);
      }
    );
  }

  getName(input: HTMLInputElement): void {
    if (this.numberGetName != null) {
      this.http.get<any>("https://localhost:5000/api/tasks/" + this.numberGetName).subscribe(

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

  getAllTasks(): void {

    this.http.get<any>("https://localhost:5000/api/tasks").subscribe(

      (response) => {
        if (response) {

          this.newTasks = [];

          this.inProgresTasks = [];
          this.completedTasks = [];
          this.closedTasks = [];
          console.log(this.newTasks.length);

          response.forEach((element: { id: number, name: string, state: string }) => {
            const task: Task = new Task(element.id, element.name,
              StateEnum[element.state as keyof typeof StateEnum]);

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


  updateTaskStatus(id:number): void {
    const newStatus = this.selectedStatus[id];
      console.log("your id"+id);
      console.log("your new status"+newStatus);
      
    this.http.patch<any>("https://localhost:5000/api/tasks/" + id + "?taskState=" + newStatus,"").subscribe(

        (response: any) => {
          console.log("your new id"+response.id);
      console.log("your new status"+response.status);
          this.name = response.name;
          this.idName = response.id;
          this.getAllTasks();
        }
      )
        ;
  }

  changeDisable(): void {
    this.disable = !this.disable;
  }
}
