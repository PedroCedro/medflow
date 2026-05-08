import { DatePipe } from '@angular/common';
import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { IntakeService } from '../../intakes/data-access/intake.service';
import { Intake } from '../../intakes/models/intake.model';
import { INTAKE_STATUS_LABELS } from '../../intakes/models/intake-status.model';
import { MedicationService } from '../../medications/data-access/medication.service';
import { PatientService } from '../../patients/data-access/patient.service';
import { PrescriptionService } from '../../prescriptions/data-access/prescription.service';

@Component({
  selector: 'app-dashboard-page',
  imports: [DatePipe, RouterLink],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {
  private readonly patientService = inject(PatientService);
  private readonly medicationService = inject(MedicationService);
  private readonly prescriptionService = inject(PrescriptionService);
  private readonly intakeService = inject(IntakeService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly patientCount = signal(0);
  protected readonly medicationCount = signal(0);
  protected readonly prescriptionCount = signal(0);
  protected readonly intakes = signal<Intake[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');
  protected readonly statusLabels = INTAKE_STATUS_LABELS;

  protected readonly pendingIntakes = computed(() =>
    this.intakes()
      .filter((intake) => intake.status === 'PENDING')
      .sort((current, next) => current.scheduledAt.localeCompare(next.scheduledAt))
      .slice(0, 3)
  );

  protected readonly nextIntakes = computed(() =>
    this.intakes()
      .slice()
      .sort((current, next) => current.scheduledAt.localeCompare(next.scheduledAt))
      .slice(0, 3)
  );

  constructor() {
    this.loadDashboard();
  }

  protected loadDashboard(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    forkJoin({
      patients: this.patientService.findAll(),
      medications: this.medicationService.findAll(),
      prescriptions: this.prescriptionService.findAll(),
      intakes: this.intakeService.findAll()
    })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (result) => {
          this.patientCount.set(result.patients.length);
          this.medicationCount.set(result.medications.length);
          this.prescriptionCount.set(result.prescriptions.length);
          this.intakes.set(result.intakes);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar o resumo.');
          this.loading.set(false);
        }
      });
  }
}
