import { Component, DestroyRef, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { PatientService } from '../data-access/patient.service';
import { Patient } from '../models/patient.model';

@Component({
  selector: 'app-patient-list-page',
  imports: [RouterLink],
  templateUrl: './patient-list-page.component.html',
  styleUrl: './patient-list-page.component.scss'
})
export class PatientListPageComponent {
  private readonly patientService = inject(PatientService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly patients = signal<Patient[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');

  constructor() {
    this.loadPatients();
  }

  protected loadPatients(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.patientService.findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (patients) => {
          this.patients.set(patients);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar os pacientes.');
          this.loading.set(false);
        }
      });
  }
}
