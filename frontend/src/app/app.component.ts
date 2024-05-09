import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { TokenStorageService } from './sercices/token-storage.service';
import { TokenInterceptor } from './sercices/token-interceptor.service';
import { HeaderComponent } from './components/header/header.component';
import { log } from 'console';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HttpClientModule, HeaderComponent,CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers:[TokenStorageService,{
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }]
})
export class AppComponent {
  constructor(private router: Router){}
 
  title = 'frontend';

  isLoginPage():boolean{
    return this.router.url==="/login"
  }
}
