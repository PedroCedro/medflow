import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { PatientService } from '../data-access/patient.service';
import { PatientRequest } from '../models/patient.model';
import { RelationshipType } from '../models/relationship-type.model';

@Component({
  selector: 'app-patient-form-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './patient-form-page.component.html',
  styleUrl: './patient-form-page.component.scss'
})
export class PatientFormPageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);
  private readonly patientService = inject(PatientService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly patientId = Number(this.route.snapshot.paramMap.get('id'));

  protected readonly isEditMode = computed(() => this.route.snapshot.paramMap.has('id'));
  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal('');

  protected readonly relationshipOptions: Array<{ value: RelationshipType; label: string }> = [
    { value: 'SELF', label: 'Eu' },
    { value: 'FATHER', label: 'Pai' },
    { value: 'MOTHER', label: 'Mãe' },
    { value: 'SON', label: 'Filho' },
    { value: 'DAUGHTER', label: 'Filha' },
    { value: 'SPOUSE', label: 'Cônjuge' },
    { value: 'GRANDFATHER', label: 'Avô' },
    { value: 'GRANDMOTHER', label: 'Avó' },
    { value: 'OTHER', label: 'Outro' }
  ];

  protected readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    birthDate: [''],
    relationship: ['' as RelationshipType | ''],
    notes: ['', [Validators.maxLength(500)]]
  });

  constructor() {
    if (this.isEditMode()) {
      this.loadPatient();
    }
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
      ? this.patientService.update(this.patientId, payload)
      : this.patientService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.saving.set(false);
          void this.router.navigate(['/patients']);
        },
        error: () => {
          this.errorMessage.set('Não foi possível salvar o paciente.');
          this.saving.set(false);
        }
      });
  }

  private loadPatient(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.patientService.findById(this.patientId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (patient) => {
          this.form.patchValue({
            name: patient.name,
            birthDate: patient.birthDate ?? '',
            relationship: patient.relationship ?? '',
            notes: patient.notes ?? ''
          });
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar o paciente.');
          this.loading.set(false);
        }
      });
  }

  private buildPayload(): PatientRequest {
    const rawValue = this.form.getRawValue();

    return {
      name: rawValue.name.trim(),
      birthDate: rawValue.birthDate || null,
      relationship: rawValue.relationship || null,
      notes: rawValue.notes.trim() || null
    };
  }
}
