import { Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
        {path:"", component: HomePageComponent},
        {path:"login", component: LoginComponent}
];
