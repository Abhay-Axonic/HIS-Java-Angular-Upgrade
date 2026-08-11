export type ChartStatus = 'A' | 'I';

/**
 * Row shape returned by GET /api/global/growthChartlist/{org}/{unit}/{offset}/
 * {size} (legacy growthChartMasterController.js:169; GROWTH_CHART_LIST =
 * "/api/global/growthChartlist" from global.url.config.js:251). Confirmed
 * live 2026-08-12: SUCCESS with 1 real row — growthChartId 15, gender
 * "Female Updated", height 70-71 cm, weight 90-91 kg, circumference 45 cm,
 * ageInMonths 1, status 'A'.
 */
export interface GrowthChartRow {
  growthChartId: number;
  gender: string;
  ageInMonths: number;
  heightFrom: number;
  heightTo: number;
  weightFrom: number;
  weightTo: number;
  circumferenceFrom: number;
  circumferenceTo: number;
  status: ChartStatus;
}
