import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiUrls } from '../enums/api-urls.enum'
import type { User } from '../models/user.model';
import { Task } from '../models/task.model';
import { StateEnum } from '../enums/state.enum';
import { SortedEnum } from '../enums/sorted.enum';


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

    getMyTasks(sorted:SortedEnum): Observable<any> {
        console.log("your service sort = "+sorted);

        return this.http.get<Task>(ApiUrls.MyTasks+"?sortedTask="+sorted)
    }

    getMyClosedTasks(sorted:SortedEnum): Observable<any> {
        return this.http.get<Task>(ApiUrls.ClosedTasks+"?sortedTask="+sorted)
    }

    changeTaskStatus(id: number, newStatus: string): Observable<Task> {
        return this.http.patch<any>(ApiUrls.Task +"/"+ id+"/state?taskState=" + newStatus, "");
    }

    changeTaskName(id: number, newName: string): Observable<Task> {
        console.log("id task = "+id );
        
        const userData = {
            name: newName

        };
        return this.http.patch<any>(ApiUrls.Task +"/"+ id+"/name", userData);
    }
}