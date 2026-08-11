import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

@Injectable({ providedIn: 'root' })
export class EncounterService {
  private base = inject(API_BASE_URL);

  constructor(private http: HttpClient) {}

  searchEncounters(payload: any): Observable<any> {
    return this.http.post<any>(
      `${this.base}/api/opd/getEncounterDetails`,
      payload
    );
  }

  getEncounterById(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/opd/encounter/${id}`);
  }

  getList(): Observable<any> {
    return this.http.get<any>(`${this.base}/api/opd/getListOfEncounterMaster`);
  }
}
