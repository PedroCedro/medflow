import { Routes } from '@angular/router';
import { AppShellComponent } from './layout/app-shell/app-shell.component';
import { PatientFormPageComponent } from './features/patients/pages/patient-form-page.component';
import { PatientListPageComponent } from './features/patients/pages/patient-list-page.component';

export const routes: Routes = [
  {
    path: '',
    component: AppShellComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'patients' },
      { path: 'patients', component: PatientListPageComponent },
      { path: 'patients/new', component: PatientFormPageComponent },
      { path: 'patients/:id/edit', component: PatientFormPageComponent }
    ]
  }
];
