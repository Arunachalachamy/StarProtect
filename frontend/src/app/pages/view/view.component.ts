import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>View Policy</h3>
  <div class="row g-3">
    <div class="col-md-6">
      <label class="form-label">Policy ID</label>
      <input class="form-control" [(ngModel)]="policyId" />
    </div>
    <div class="col-md-6 d-flex align-items-end">
      <button class="btn btn-info" (click)="fetch()">Search</button>
    </div>
  </div>
  <pre class="mt-3 bg-light p-3" *ngIf="data">{{ data | json }}</pre>
  `
})
export class ViewComponent {
  policyId = '';
  data: any;

  async fetch() {
    const res = await fetch(`http://localhost:8080/api/policies/${this.policyId}`);
    this.data = res.ok ? await res.json() : null;
  }
}