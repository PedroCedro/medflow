import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Medication, MedicationRequest } from '../models/medication.model';

@Injectable({
  providedIn: 'root'
})
export class MedicationService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/medications`;

  findAll(): Observable<Medication[]> {
    return this.http.get<Medication[]>(this.apiUrl);
  }

  findById(id: number): Observable<Medication> {
    return this.http.get<Medication>(`${this.apiUrl}/${id}`);
  }

  create(payload: MedicationRequest): Observable<Medication> {
    return this.http.post<Medication>(this.apiUrl, payload);
  }

  update(id: number, payload: MedicationRequest): Observable<Medication> {
    return this.http.put<Medication>(`${this.apiUrl}/${id}`, payload);
  }
}
