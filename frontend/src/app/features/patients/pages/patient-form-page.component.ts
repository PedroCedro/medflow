import { Component, computed, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-patient-form-page',
  imports: [RouterLink],
  templateUrl: './patient-form-page.component.html',
  styleUrl: './patient-form-page.component.scss'
})
export class PatientFormPageComponent {
  private readonly route = inject(ActivatedRoute);

  protected readonly isEditMode = computed(() => this.route.snapshot.paramMap.has('id'));
}
