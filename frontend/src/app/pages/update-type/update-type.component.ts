import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-type',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>Update Policy Type</h3>
  <form class="row g-3" (ngSubmit)="update()">
    <div class="col-md-6">
      <label class="form-label">Policy ID</label>
      <input class="form-control" [(ngModel)]="policyId" name="policyId" required />
    </div>
    <div class="col-md-6">
      <label class="form-label">Policy Type</label>
      <select class="form-select" [(ngModel)]="type" name="type">
        <option value="Full">Full Insurance</option>
        <option value="ThirdParty">Third party</option>
      </select>
    </div>
    <div class="col-12">
      <button class="btn btn-secondary" type="submit">Update</button>
      <span class="ms-3" [class.text-success]="ok" [class.text-danger]="!ok && message">{{message}}</span>
    </div>
  </form>
  `
})
export class UpdateTypeComponent {
  policyId = '';
  type = 'Full';
  ok = false;
  message = '';

  async update() {
    const res = await fetch(`http://localhost:8080/api/policies/${this.policyId}/type`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ type: this.type })
    });
    this.ok = res.ok;
    this.message = res.ok ? 'Updated' : 'Update failed';
  }
}