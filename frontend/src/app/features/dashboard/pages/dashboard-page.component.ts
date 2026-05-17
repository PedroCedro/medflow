import { DatePipe } from '@angular/common';
import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { IntakeService } from '../../intakes/data-access/intake.service';
import { Intake } from '../../intakes/models/intake.model';
import { MedicationService } from '../../medications/data-access/medication.service';
import { Patient } from '../../patients/models/patient.model';
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

  protected readonly patients = signal<Patient[]>([]);
  protected readonly patientCount = signal(0);
  protected readonly medicationCount = signal(0);
  protected readonly prescriptionCount = signal(0);
  protected readonly intakes = signal<Intake[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');
  protected readonly actionErrorMessage = signal('');
  protected readonly selectedPatientId = signal<number | null>(null);
  protected readonly selectedIntakeIds = signal<Set<number>>(new Set());
  protected readonly confirming = signal(false);
  protected readonly submitting = signal(false);
  protected readonly confirmationTime = signal<Date | null>(null);

  protected readonly pendingIntakes = computed(() => {
    const patientId = this.selectedPatientId();
    return this.intakes()
      .filter((i) => i.status === 'PENDING' && i.patientId === patientId)
      .sort((a, b) => a.scheduledAt.localeCompare(b.scheduledAt));
  });

  protected readonly selectedPatientName = computed(() => {
    const id = this.selectedPatientId();
    return this.patients().find((p) => p.id === id)?.name ?? '';
  });

  protected readonly confirmationMeds = computed(() => {
    const ids = this.selectedIntakeIds();
    return this.intakes()
      .filter((i) => ids.has(i.id))
      .map((i) => i.medicationName);
  });

  protected readonly selectedCount = computed(() => this.selectedIntakeIds().size);

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
          this.patients.set(result.patients);
          this.patientCount.set(result.patients.length);
          this.medicationCount.set(result.medications.length);
          this.prescriptionCount.set(result.prescriptions.length);
          this.intakes.set(result.intakes);
          if (result.patients.length > 0 && this.selectedPatientId() === null) {
            this.selectedPatientId.set(result.patients[0].id);
          }
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar o resumo.');
          this.loading.set(false);
        }
      });
  }

  protected selectPatient(id: number): void {
    this.selectedPatientId.set(id);
    this.selectedIntakeIds.set(new Set());
  }

  protected toggleIntake(id: number): void {
    const current = new Set(this.selectedIntakeIds());
    if (current.has(id)) {
      current.delete(id);
    } else {
      current.add(id);
    }
    this.selectedIntakeIds.set(current);
  }

  protected openConfirmModal(): void {
    this.confirmationTime.set(new Date());
    this.confirming.set(true);
  }

  protected closeConfirmModal(): void {
    if (!this.submitting()) {
      this.confirming.set(false);
      this.confirmationTime.set(null);
    }
  }

  protected confirmTaken(): void {
    const takenAt = this.toLocalDateTimeValue(this.confirmationTime() ?? new Date());
    const items = Array.from(this.selectedIntakeIds()).map((id) => ({
      id,
      status: 'TAKEN' as const,
      takenAt
    }));

    this.submitting.set(true);
    this.actionErrorMessage.set('');

    this.intakeService
      .bulkUpdate(items)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (updated) => {
          this.intakes.update((current) =>
            current.map((i) => updated.find((u) => u.id === i.id) ?? i)
          );
          this.selectedIntakeIds.set(new Set());
          this.confirming.set(false);
          this.submitting.set(false);
        },
        error: () => {
          this.actionErrorMessage.set('Não foi possível registrar as tomadas.');
          this.submitting.set(false);
        }
      });
  }

  private toLocalDateTimeValue(date: Date): string {
    const offsetDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
    return offsetDate.toISOString().slice(0, 19);
  }
}
