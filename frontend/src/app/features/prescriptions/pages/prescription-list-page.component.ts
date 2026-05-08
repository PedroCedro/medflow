import { DatePipe } from '@angular/common';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { PrescriptionService } from '../data-access/prescription.service';
import { Prescription } from '../models/prescription.model';

@Component({
  selector: 'app-prescription-list-page',
  imports: [DatePipe, RouterLink],
  templateUrl: './prescription-list-page.component.html',
  styleUrl: './prescription-list-page.component.scss'
})
export class PrescriptionListPageComponent {
  private readonly prescriptionService = inject(PrescriptionService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly prescriptions = signal<Prescription[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');

  constructor() {
    this.loadPrescriptions();
  }

  protected loadPrescriptions(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.prescriptionService.findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (prescriptions) => {
          this.prescriptions.set(prescriptions);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar as prescrições.');
          this.loading.set(false);
        }
      });
  }
}
