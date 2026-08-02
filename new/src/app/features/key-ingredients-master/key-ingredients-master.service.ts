import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

export interface KeyIngredientOption {
  id?: number;
  uomId?: number | string;
  nutritionId?: number | string;
  unitName?: string;
  uomName?: string;
  nutritionName?: string;
}

export interface NutritionLine {
  nutritionId: number | string;
  value: string | number;
  uomId: number | string;
  ingredientNutritionId?: number | string;
}

export interface KeyIngredientRow {
  keyIngredientId?: number;
  ingredientCode: string;
  ingredientDescription: string;
  ingredientValue: string | number;
  uomId?: number | string;
  uomName?: string;
  status?: string;
  nutriciansList?: NutritionLine[];
}

@Injectable({ providedIn: 'root' })
export class KeyIngredientsMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  list(orgId: number, unitId: number): Observable<{ status?: string; listObject?: KeyIngredientRow[] }> {
    return this.http.get<{ status?: string; listObject?: KeyIngredientRow[] }>(
      `${this.base}/api/kitchen/master/keyIngredientsMaster?organizationId=${orgId}&unitId=${unitId}`,
    );
  }

  units(orgId: number, unitId: number): Observable<{ listObject?: KeyIngredientOption[] }> {
    return this.http.get<{ listObject?: KeyIngredientOption[] }>(
      `${this.base}/api/kitchen/master/keyIngredientsMaster/getUnitMasterDetails?organizationId=${orgId}&unitId=${unitId}`,
    );
  }

  nutritions(orgId: number, unitId: number): Observable<{ listObject?: KeyIngredientOption[] }> {
    return this.http.get<{ listObject?: KeyIngredientOption[] }>(
      `${this.base}/api/kitchen/master/nutritionalMaster/getActive?organizationId=${orgId}&unitId=${unitId}`,
    );
  }

  create(payload: Record<string, unknown>) {
    return this.http.post<{ status?: string }>(`${this.base}/api/kitchen/master/keyIngredientsMaster`, payload);
  }

  update(payload: Record<string, unknown>) {
    return this.http.put<{ status?: string }>(`${this.base}/api/kitchen/master/keyIngredientsMaster`, payload);
  }

  updateStatus(payload: Record<string, unknown>) {
    return this.http.put<{ status?: string }>(`${this.base}/api/kitchen/master/keyIngredientsMaster/updateStatus`, payload);
  }
}
