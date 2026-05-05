import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { MedicationService } from '../data-access/medication.service';
import { MedicationRequest } from '../models/medication.model';

@Component({
  selector: 'app-medication-form-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './medication-form-page.component.html',
  styleUrl: './medication-form-page.component.scss'
})
export class MedicationFormPageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);
  private readonly medicationService = inject(MedicationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly medicationId = Number(this.route.snapshot.paramMap.get('id'));

  protected readonly isEditMode = computed(() => this.route.snapshot.paramMap.has('id'));
  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal('');

  protected readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    activeIngredient: ['', [Validators.maxLength(120)]],
    presentation: ['', [Validators.maxLength(80)]],
    notes: ['', [Validators.maxLength(500)]]
  });

  constructor() {
    if (this.isEditMode()) {
      this.loadMedication();
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
      ? this.medicationService.update(this.medicationId, payload)
      : this.medicationService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.saving.set(false);
          void this.router.navigate(['/medications']);
        },
        error: () => {
          this.errorMessage.set('Não foi possível salvar o medicamento.');
          this.saving.set(false);
        }
      });
  }

  private loadMedication(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.medicationService.findById(this.medicationId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (medication) => {
          this.form.patchValue({
            name: medication.name,
            activeIngredient: medication.activeIngredient ?? '',
            presentation: medication.presentation ?? '',
            notes: medication.notes ?? ''
          });
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar o medicamento.');
          this.loading.set(false);
        }
      });
  }

  private buildPayload(): MedicationRequest {
    const rawValue = this.form.getRawValue();

    return {
      name: rawValue.name.trim(),
      activeIngredient: rawValue.activeIngredient.trim() || null,
      presentation: rawValue.presentation.trim() || null,
      notes: rawValue.notes.trim() || null
    };
  }
}
