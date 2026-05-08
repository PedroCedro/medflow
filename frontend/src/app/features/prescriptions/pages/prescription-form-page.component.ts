import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { MedicationService } from '../../medications/data-access/medication.service';
import { Medication } from '../../medications/models/medication.model';
import { PatientService } from '../../patients/data-access/patient.service';
import { Patient } from '../../patients/models/patient.model';
import { PrescriptionService } from '../data-access/prescription.service';
import { DOSAGE_UNIT_OPTIONS, DosageUnit } from '../models/dosage-unit.model';
import { PrescriptionRequest } from '../models/prescription.model';

@Component({
  selector: 'app-prescription-form-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './prescription-form-page.component.html',
  styleUrl: './prescription-form-page.component.scss'
})
export class PrescriptionFormPageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);
  private readonly prescriptionService = inject(PrescriptionService);
  private readonly patientService = inject(PatientService);
  private readonly medicationService = inject(MedicationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly prescriptionId = Number(this.route.snapshot.paramMap.get('id'));

  protected readonly isEditMode = computed(() => this.route.snapshot.paramMap.has('id'));
  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly patients = signal<Patient[]>([]);
  protected readonly medications = signal<Medication[]>([]);
  protected readonly dosageUnitOptions = DOSAGE_UNIT_OPTIONS;

  protected readonly form = this.formBuilder.nonNullable.group({
    patientId: ['', [Validators.required]],
    medicationId: ['', [Validators.required]],
    dosageAmount: ['', [Validators.required, Validators.min(0.01)]],
    dosageUnit: ['' as DosageUnit | '', [Validators.required]],
    frequency: ['', [Validators.required, Validators.maxLength(80)]],
    scheduledTime: [''],
    startDate: ['', [Validators.required]],
    continuousUse: [false],
    stockQuantity: ['', [Validators.min(0)]],
    minimumStockAlert: ['', [Validators.min(0)]],
    instructions: ['', [Validators.maxLength(500)]]
  });

  constructor() {
    this.loadInitialData();
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.errorMessage.set('');

    const payload = this.buildPayload();
    const request$ = this.isEditMode()
      ? this.prescriptionService.update(this.prescriptionId, payload)
      : this.prescriptionService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.saving.set(false);
          void this.router.navigate(['/prescriptions']);
        },
        error: () => {
          this.errorMessage.set('Não foi possível salvar a prescrição.');
          this.saving.set(false);
        }
      });
  }

  private loadInitialData(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    const dataRequests = {
      patients: this.patientService.findAll(),
      medications: this.medicationService.findAll()
    };

    const requests = this.isEditMode()
      ? { ...dataRequests, prescription: this.prescriptionService.findById(this.prescriptionId) }
      : dataRequests;

    forkJoin(requests)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (result) => {
          this.patients.set(result.patients);
          this.medications.set(result.medications);

          if ('prescription' in result) {
            this.form.patchValue({
              patientId: String(result.prescription.patientId),
              medicationId: String(result.prescription.medicationId),
              dosageAmount: String(result.prescription.dosageAmount),
              dosageUnit: result.prescription.dosageUnit,
              frequency: result.prescription.frequency,
              scheduledTime: result.prescription.scheduledTime ?? '',
              startDate: result.prescription.startDate,
              continuousUse: result.prescription.continuousUse,
              stockQuantity: result.prescription.stockQuantity === null ? '' : String(result.prescription.stockQuantity),
              minimumStockAlert: result.prescription.minimumStockAlert === null ? '' : String(result.prescription.minimumStockAlert),
              instructions: result.prescription.instructions ?? ''
            });
          }

          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar os dados da prescrição.');
          this.loading.set(false);
        }
      });
  }

  private buildPayload(): PrescriptionRequest {
    const rawValue = this.form.getRawValue();

    return {
      patientId: Number(rawValue.patientId),
      medicationId: Number(rawValue.medicationId),
      dosageAmount: Number(rawValue.dosageAmount),
      dosageUnit: rawValue.dosageUnit as DosageUnit,
      frequency: rawValue.frequency.trim(),
      scheduledTime: rawValue.scheduledTime || null,
      startDate: rawValue.startDate,
      continuousUse: rawValue.continuousUse,
      stockQuantity: rawValue.stockQuantity === '' ? null : Number(rawValue.stockQuantity),
      minimumStockAlert: rawValue.minimumStockAlert === '' ? null : Number(rawValue.minimumStockAlert),
      instructions: rawValue.instructions.trim() || null
    };
  }
}
