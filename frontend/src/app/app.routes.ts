import { Routes } from '@angular/router';
import { DashboardPageComponent } from './features/dashboard/pages/dashboard-page.component';
import { IntakeFormPageComponent } from './features/intakes/pages/intake-form-page.component';
import { IntakeListPageComponent } from './features/intakes/pages/intake-list-page.component';
import { AppShellComponent } from './layout/app-shell/app-shell.component';
import { MedicationFormPageComponent } from './features/medications/pages/medication-form-page.component';
import { MedicationListPageComponent } from './features/medications/pages/medication-list-page.component';
import { PatientFormPageComponent } from './features/patients/pages/patient-form-page.component';
import { PatientListPageComponent } from './features/patients/pages/patient-list-page.component';
import { PrescriptionFormPageComponent } from './features/prescriptions/pages/prescription-form-page.component';
import { PrescriptionListPageComponent } from './features/prescriptions/pages/prescription-list-page.component';

export const routes: Routes = [
  {
    path: '',
    component: AppShellComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: DashboardPageComponent },
      { path: 'patients', component: PatientListPageComponent },
      { path: 'patients/new', component: PatientFormPageComponent },
      { path: 'patients/:id/edit', component: PatientFormPageComponent },
      { path: 'medications', component: MedicationListPageComponent },
      { path: 'medications/new', component: MedicationFormPageComponent },
      { path: 'medications/:id/edit', component: MedicationFormPageComponent },
      { path: 'prescriptions', component: PrescriptionListPageComponent },
      { path: 'prescriptions/new', component: PrescriptionFormPageComponent },
      { path: 'prescriptions/:id/edit', component: PrescriptionFormPageComponent },
      { path: 'intakes', component: IntakeListPageComponent },
      { path: 'intakes/new', component: IntakeFormPageComponent },
      { path: 'intakes/:id/edit', component: IntakeFormPageComponent }
    ]
  }
];
