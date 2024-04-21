import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './sercices/token-interceptor.service';
import { TokenStorageService } from './sercices/token-storage.service';

@NgModule({
  imports: [
    HttpClientModule,
    RouterModule.forRoot([/* ваши маршруты */])
  ],
  providers: [
    TokenStorageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ]
})
export class AppModule { }