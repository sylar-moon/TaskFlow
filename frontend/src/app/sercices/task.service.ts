import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiUrls } from '../enums/api-urls.enum'
import type { User } from '../models/user.model';
import { Task } from '../models/task.model';


@Injectable({
    providedIn: 'root'
})
export class TaskService {
    constructor(private http: HttpClient) { }


    addNewTask(taskName: string): Observable<Task> {

        const userData = {
            name: taskName

        };
        return this.http.post<Task>(ApiUrls.Task, userData)
    }

    getMyTasks():Observable<any>{
        return this.http.get<Task>(ApiUrls.MyTasks)
    }
}