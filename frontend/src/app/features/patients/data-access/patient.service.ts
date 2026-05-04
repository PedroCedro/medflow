import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Patient, PatientRequest } from '../models/patient.model';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/patients`;

  findAll(): Observable<Patient[]> {
    return this.http.get<Patient[]>(this.apiUrl);
  }

  findById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  create(payload: PatientRequest): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, payload);
  }

  update(id: number, payload: PatientRequest): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, payload);
  }
}
