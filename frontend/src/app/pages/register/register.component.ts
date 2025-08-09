import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>UnderWriter Registration</h3>
  <form class="row g-3" (ngSubmit)="register()">
    <div class="col-md-6">
      <label class="form-label">User Name</label>
      <input class="form-control" [(ngModel)]="model.userName" name="userName" required />
    </div>
    <div class="col-md-6">
      <label class="form-label">Name</label>
      <input class="form-control" [(ngModel)]="model.name" name="name" required />
    </div>
    <div class="col-md-6">
      <label class="form-label">DOB</label>
      <input type="date" class="form-control" [(ngModel)]="model.dob" name="dob" />
    </div>
    <div class="col-md-6">
      <label class="form-label">Joining Date</label>
      <input type="date" class="form-control" [(ngModel)]="model.joiningDate" name="joiningDate" />
    </div>
    <div class="col-md-6">
      <label class="form-label">Password</label>
      <input type="password" class="form-control" [(ngModel)]="model.password" name="password" required />
    </div>
    <div class="col-12">
      <button class="btn btn-primary" type="submit">Register</button>
      <span class="ms-3 text-success" *ngIf="message">{{message}}</span>
    </div>
  </form>
  `
})
export class RegisterComponent {
  model: any = {};
  message = '';

  async register() {
    const res = await fetch('http://localhost:8080/api/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(this.model)
    });
    this.message = res.ok ? 'Registered successfully' : 'Error registering';
  }
}