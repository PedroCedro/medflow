import { DatePipe } from '@angular/common';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { IntakeService } from '../data-access/intake.service';
import { Intake } from '../models/intake.model';
import { INTAKE_STATUS_LABELS } from '../models/intake-status.model';

@Component({
  selector: 'app-intake-list-page',
  imports: [DatePipe, RouterLink],
  templateUrl: './intake-list-page.component.html',
  styleUrl: './intake-list-page.component.scss'
})
export class IntakeListPageComponent {
  private readonly intakeService = inject(IntakeService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly intakes = signal<Intake[]>([]);
  protected readonly loading = signal(true);
  protected readonly errorMessage = signal('');
  protected readonly statusLabels = INTAKE_STATUS_LABELS;

  constructor() {
    this.loadIntakes();
  }

  protected loadIntakes(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.intakeService.findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (intakes) => {
          this.intakes.set(intakes);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar as tomadas.');
          this.loading.set(false);
        }
      });
  }
}
