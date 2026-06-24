import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Login - Task Manager'
  },
  {
    path: 'register',
    component: RegisterComponent,
    title: 'Cadastro - Task Manager'
   },
  {
    path: 'tasks',
    component: DashboardComponent,
    title: 'Painel de Tarefas',
    canActivate: [authGuard],
  },
  { path: '**', redirectTo: 'login' }
];