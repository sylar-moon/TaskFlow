import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';

@Injectable({
    providedIn: 'root'
  })
export class TokenInterceptor implements HttpInterceptor {

  constructor(private tokenStorageService: TokenStorageService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log("TokenInterceptor start");
    
    // Получаем токен из сервиса TokenStorageService
    const token = this.tokenStorageService.getToken();
    
    console.log("token: "+token);
    
    // Если токен есть, добавляем заголовок Authorization к запросу
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    
    // Пропускаем запрос дальше
    return next.handle(request);
  }
}
