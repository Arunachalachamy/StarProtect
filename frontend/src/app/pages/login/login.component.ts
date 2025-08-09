import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>Login</h3>
  <form class="row g-3" (ngSubmit)="login()">
    <div class="col-md-6">
      <label class="form-label">User Name</label>
      <input class="form-control" [(ngModel)]="model.userName" name="userName" required />
    </div>
    <div class="col-md-6">
      <label class="form-label">Password</label>
      <input type="password" class="form-control" [(ngModel)]="model.password" name="password" required />
    </div>
    <div class="col-12">
      <button class="btn btn-success" type="submit">Login</button>
      <span class="ms-3" [class.text-success]="ok" [class.text-danger]="!ok && message">{{message}}</span>
    </div>
  </form>
  `
})
export class LoginComponent {
  model: any = {};
  message = '';
  ok = false;

  async login() {
    const res = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(this.model)
    });
    this.ok = res.ok;
    this.message = res.ok ? 'Logged in' : 'Invalid credentials';
  }
}