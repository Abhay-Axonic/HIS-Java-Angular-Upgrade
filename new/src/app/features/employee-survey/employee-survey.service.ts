import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { EmployeeSurveySavePayload, SurveyQuestionSet } from './employee-survey.models';

/**
 * Endpoint paths taken directly from `EmployeeSurveyController.js` and
 * `EmployeeSurveyController.java` (`legacy/Global/src/main/java/com/param/
 * global/org/common/controller/`, mapped under `/api/common/empSurvey`) --
 * not guessed. Full chain confirmed real: Controller -> `IEmployeeSurveyService`
 * (`EmployeeSurveyServiceImpl`) -> `IEmployeeSurveyDao` (`EmployeeSurveyDaoImpl`,
 * table `public.t_employee_survey`).
 *
 * `GET /api/common/empSurvey/getEmployeeSurveyById/{userId}/{organizationId}/{unitId}`
 * is ALSO a real, fully-implemented backend endpoint (confirmed same chain),
 * but legacy's own `$scope.getEmployeeSurvey()` that would call it is
 * commented out (`EmployeeSurveyController.js:171`) and never invoked --
 * deliberately NOT modeled here, matching legacy's actual (dead) behavior.
 * Every visit to this screen starts from the static blank question list
 * below, never a previously-submitted answer set.
 */
@Injectable({ providedIn: 'root' })
export class EmployeeSurveyService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /**
   * ctrl:26-38 (`geInitialJson`) -- GET of a static JSON file bundled with
   * the legacy webapp (not a backend API call, no ApiResponse envelope).
   * Proxied in dev via `/resources/**` (see `new/proxy.conf.example.json`),
   * same file legacy itself serves.
   */
  getSurveyQuestions(): Observable<SurveyQuestionSet[]> {
    return this.http.get<SurveyQuestionSet[]>(
      `${this.base}/resources/assets/js/nabhJSON/employee-satisfication-survey.json`,
    );
  }

  // ctrl:55-111 (`saveSurvey`) -- real POST.
  saveEmployeeSurvey(payload: EmployeeSurveySavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/common/empSurvey/saveEmployeeSurvey`, payload);
  }
}
