import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-renew',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3>Renew Insurance</h3>
  <form class="row g-3" (ngSubmit)="renew()">
    <div class="col-md-6">
      <label class="form-label">Policy ID</label>
      <input class="form-control" [(ngModel)]="policyId" name="policyId" required />
    </div>
    <div class="col-md-6">
      <label class="form-label">Claim Status</label>
      <select class="form-select" [(ngModel)]="claimStatus" name="claimStatus">
        <option [ngValue]="false">No</option>
        <option [ngValue]="true">Yes</option>
      </select>
    </div>
    <div class="col-12">
      <button class="btn btn-warning" type="submit">Renew</button>
      <span class="ms-3" [class.text-success]="message && ok" [class.text-danger]="message && !ok">{{message}}</span>
    </div>
  </form>
  `
})
export class RenewComponent {
  policyId = '';
  claimStatus = false;
  message = '';
  ok = false;

  async renew() {
    const res = await fetch(`http://localhost:8080/api/vehicles/${this.policyId}/renew`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ claimStatus: this.claimStatus })
    });
    this.ok = res.ok;
    this.message = res.ok ? 'Renewed successfully' : 'Renewal failed';
  }
}