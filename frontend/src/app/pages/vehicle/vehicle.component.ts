import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vehicle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>Register Vehicle</h3>
  <form class="row g-3" (ngSubmit)="submit()">
    <div class="col-md-4">
      <label class="form-label">Vehicle No</label>
      <input class="form-control" maxlength="10" [(ngModel)]="model.vehicleNo" name="vehicleNo" required />
    </div>
    <div class="col-md-4">
      <label class="form-label">Vehicle Type</label>
      <select class="form-select" [(ngModel)]="model.vehicleType" name="vehicleType">
        <option value="2-wheeler">2-wheeler</option>
        <option value="4-wheeler">4-wheeler</option>
      </select>
    </div>
    <div class="col-md-4">
      <label class="form-label">Customer Name</label>
      <input class="form-control" maxlength="50" [(ngModel)]="model.customerName" name="customerName" />
    </div>
    <div class="col-md-4">
      <label class="form-label">Engine No</label>
      <input type="number" class="form-control" [(ngModel)]="model.engineNo" name="engineNo" />
    </div>
    <div class="col-md-4">
      <label class="form-label">Chasis No</label>
      <input type="number" class="form-control" [(ngModel)]="model.chasisNo" name="chasisNo" />
    </div>
    <div class="col-md-4">
      <label class="form-label">Phone No</label>
      <input class="form-control" maxlength="10" [(ngModel)]="model.phoneNo" name="phoneNo" />
    </div>
    <div class="col-md-4">
      <label class="form-label">Type</label>
      <select class="form-select" [(ngModel)]="model.type" name="type">
        <option value="Full">Full Insurance</option>
        <option value="ThirdParty">Third party</option>
      </select>
    </div>
    <div class="col-md-4">
      <label class="form-label">From Date</label>
      <input type="date" class="form-control" [(ngModel)]="model.fromDate" name="fromDate" />
    </div>
    <div class="col-md-4">
      <label class="form-label">To Date</label>
      <input type="date" class="form-control" [(ngModel)]="model.toDate" name="toDate" />
    </div>
    <div class="col-md-4">
      <label class="form-label">UnderWriter Id</label>
      <input type="number" class="form-control" [(ngModel)]="model.underwriterId" name="underwriterId" />
    </div>
    <div class="col-12">
      <button class="btn btn-primary" type="submit">Register</button>
      <span class="ms-3 text-success" *ngIf="message">{{message}}</span>
    </div>
  </form>
  `
})
export class VehicleComponent {
  model: any = {};
  message = '';

  async submit() {
    const res = await fetch('http://localhost:8080/api/vehicles', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(this.model)
    });
    this.message = res.ok ? 'Vehicle registered' : 'Error';
  }
}