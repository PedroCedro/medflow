import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Intake, IntakeRequest } from '../models/intake.model';

@Injectable({
  providedIn: 'root'
})
export class IntakeService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/intakes`;

  findAll(prescriptionId?: number): Observable<Intake[]> {
    const options = prescriptionId ? { params: new HttpParams().set('prescriptionId', prescriptionId) } : undefined;
    return this.http.get<Intake[]>(this.apiUrl, options);
  }

  findById(id: number): Observable<Intake> {
    return this.http.get<Intake>(`${this.apiUrl}/${id}`);
  }

  create(payload: IntakeRequest): Observable<Intake> {
    return this.http.post<Intake>(this.apiUrl, payload);
  }

  update(id: number, payload: IntakeRequest): Observable<Intake> {
    return this.http.put<Intake>(`${this.apiUrl}/${id}`, payload);
  }
}
