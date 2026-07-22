/**
 * Field set taken directly from
 * `resources/assets/js/nabhJSON/employee-satisfication-survey.json` (the
 * static seed list of survey questions, fetched fresh on every load AND
 * again after every successful save -- see `geInitialJson()`,
 * `EmployeeSurveyController.js:26-38/94`) and the backend
 * `EmployeeSurveyListDto`/`EmployeeSurveyDto`
 * (legacy/Global/src/main/java/com/param/global/org/common/dto/).
 */
export interface SurveyPoint {
  id: number;
  name: string;
  /** '' (unanswered) | 'stronglyDisagree' | 'disagree' | 'agree' | 'stronglyAgree' */
  selectedOption: string;
}

/** Shape of the raw static JSON file's top-level array (ctrl:27-30 reads
 * `response.data[0].ESSList`). */
export interface SurveyQuestionSet {
  ESS: number;
  ESSName: string;
  ESSList: SurveyPoint[];
}

/**
 * POST body for /api/common/empSurvey/saveEmployeeSurvey (ctrl:72-83).
 * `rating` is a String on the backend DTO (`EmployeeSurveyDto.rating`) even
 * though the view's input is `type="number"` -- sent as a string, matching
 * legacy's raw `$scope.survey.rating` (whatever the native number input
 * currently holds, never coerced).
 */
export interface EmployeeSurveySavePayload {
  satisfactionList: SurveyPoint[];
  createdBy: number;
  updatedBy: number;
  userId: number;
  updatedDate: string;
  createdDate: string;
  organizationId: number;
  rating: string;
  unitId: number;
  status: 'A';
}
