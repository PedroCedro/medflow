import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { MedicationService } from '../data-access/medication.service';
import { Medication } from '../models/medication.model';

@Component({
  selector: 'app-medication-list-page',
  imports: [RouterLink],
  templateUrl: './medication-list-page.component.html',
  styleUrl: './medication-list-page.component.scss'
})
export class MedicationListPageComponent {
  private readonly medicationService = inject(MedicationService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly medications = signal<Medication[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');

  constructor() {
    this.loadMedications();
  }

  protected loadMedications(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.medicationService.findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (medications) => {
          this.medications.set(medications);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar os medicamentos.');
          this.loading.set(false);
        }
      });
  }
}
