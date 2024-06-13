import { Routes } from '@angular/router';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { ClosedTaskPageComponent } from './components/closed-task-page/closed-task-page.component';
import { AllTaskPageComponent } from './components/all-task-page/all-task-page.component';

export const routes: Routes = [
        {path:"", component: HomePageComponent},
        {path:"login", component: LoginComponent},
        {path:"closed-tasks", component: ClosedTaskPageComponent},
        {path:"all-tasks", component: AllTaskPageComponent},
];
