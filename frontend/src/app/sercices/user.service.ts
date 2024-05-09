import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { ApiUrls } from '../enums/api-urls.enum'
import type { User } from '../models/user.model';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) {}

    addNewTask(taskName:string):Observable<any>{

        const userData = {
            name: taskName
            
          };
          return this.http.post<any>(ApiUrls.Task,userData)
    }

    getMe():Observable<User>{
        console.log("user service start");
        
        return this.http.get<any>(ApiUrls.Me)
    }
}