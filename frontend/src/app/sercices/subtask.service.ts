import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { ApiUrls } from '../enums/api-urls.enum'
import type { User } from '../models/user.model';


@Injectable({
    providedIn: 'root'
})
export class SubtaskService {
    constructor(private http: HttpClient) {}

    // getSubtasks(taskId)

    addNewTask(taskName:string):Observable<any>{

        const userData = {
            name: taskName
            
          };
          return this.http.post<any>(ApiUrls.Task,userData)
    }


    
    addNewSubtask(subtaskName:string, taskId:number):Observable<any>{

        const userData = {
            taskId:taskId,
            subtaskName: subtaskName
            
          };
          return this.http.post<any>(ApiUrls.Subtask,userData)
    }   


    getMe():Observable<User>{
        console.log("user service start");
        
        return this.http.get<any>(ApiUrls.Me)
    }

    changeComleteSubtask(idSubtask:number):Observable<any>{
              return this.http.patch<any> (ApiUrls.Subtask+'/'+idSubtask,{})
    }  
    
    updateSubtasks(idTask:number):Observable<any>{
        return this.http.get<any> (ApiUrls.Subtask+'/'+idTask,{})
}  


    
}