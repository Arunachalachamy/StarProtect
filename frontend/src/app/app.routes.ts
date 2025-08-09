import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent) },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'vehicle', loadComponent: () => import('./pages/vehicle/vehicle.component').then(m => m.VehicleComponent) },
  { path: 'renew', loadComponent: () => import('./pages/renew/renew.component').then(m => m.RenewComponent) },
  { path: 'view', loadComponent: () => import('./pages/view/view.component').then(m => m.ViewComponent) },
  { path: 'update-type', loadComponent: () => import('./pages/update-type/update-type.component').then(m => m.UpdateTypeComponent) },
  { path: '', pathMatch: 'full', redirectTo: 'register' },
  { path: '**', redirectTo: 'register' }
];
