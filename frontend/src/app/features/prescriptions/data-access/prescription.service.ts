import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Prescription, PrescriptionRequest } from '../models/prescription.model';

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/prescriptions`;

  findAll(patientId?: number): Observable<Prescription[]> {
    const options = patientId ? { params: new HttpParams().set('patientId', patientId) } : undefined;
    return this.http.get<Prescription[]>(this.apiUrl, options);
  }

  findById(id: number): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.apiUrl}/${id}`);
  }

  create(payload: PrescriptionRequest): Observable<Prescription> {
    return this.http.post<Prescription>(this.apiUrl, payload);
  }

  update(id: number, payload: PrescriptionRequest): Observable<Prescription> {
    return this.http.put<Prescription>(`${this.apiUrl}/${id}`, payload);
  }
}
