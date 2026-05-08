import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { PrescriptionService } from '../../prescriptions/data-access/prescription.service';
import { Prescription } from '../../prescriptions/models/prescription.model';
import { IntakeService } from '../data-access/intake.service';
import { IntakeRequest } from '../models/intake.model';
import { INTAKE_STATUS_OPTIONS, IntakeStatus } from '../models/intake-status.model';

@Component({
  selector: 'app-intake-form-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './intake-form-page.component.html',
  styleUrl: './intake-form-page.component.scss'
})
export class IntakeFormPageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);
  private readonly intakeService = inject(IntakeService);
  private readonly prescriptionService = inject(PrescriptionService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly intakeId = Number(this.route.snapshot.paramMap.get('id'));

  protected readonly isEditMode = computed(() => this.route.snapshot.paramMap.has('id'));
  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly prescriptions = signal<Prescription[]>([]);
  protected readonly statusOptions = INTAKE_STATUS_OPTIONS;

  protected readonly form = this.formBuilder.nonNullable.group({
    prescriptionId: ['', [Validators.required]],
    scheduledAt: ['', [Validators.required]],
    takenAt: [''],
    status: ['PENDING' as IntakeStatus, [Validators.required]],
    notes: ['', [Validators.maxLength(500)]]
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
      ? this.intakeService.update(this.intakeId, payload)
      : this.intakeService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.saving.set(false);
          void this.router.navigate(['/intakes']);
        },
        error: () => {
          this.errorMessage.set('Não foi possível salvar a tomada.');
          this.saving.set(false);
        }
      });
  }

  protected prescriptionLabel(prescription: Prescription): string {
    return `${prescription.patientName} - ${prescription.medicationName}`;
  }

  private loadInitialData(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    const dataRequests = {
      prescriptions: this.prescriptionService.findAll()
    };

    const requests = this.isEditMode()
      ? { ...dataRequests, intake: this.intakeService.findById(this.intakeId) }
      : dataRequests;

    forkJoin(requests)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (result) => {
          this.prescriptions.set(result.prescriptions);

          if ('intake' in result) {
            this.form.patchValue({
              prescriptionId: String(result.intake.prescriptionId),
              scheduledAt: this.toDateTimeInputValue(result.intake.scheduledAt),
              takenAt: result.intake.takenAt ? this.toDateTimeInputValue(result.intake.takenAt) : '',
              status: result.intake.status,
              notes: result.intake.notes ?? ''
            });
          }

          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar os dados da tomada.');
          this.loading.set(false);
        }
      });
  }

  private buildPayload(): IntakeRequest {
    const rawValue = this.form.getRawValue();

    return {
      prescriptionId: Number(rawValue.prescriptionId),
      scheduledAt: rawValue.scheduledAt,
      takenAt: rawValue.takenAt || null,
      status: rawValue.status,
      notes: rawValue.notes.trim() || null
    };
  }

  private toDateTimeInputValue(value: string): string {
    return value.slice(0, 16);
  }
}
