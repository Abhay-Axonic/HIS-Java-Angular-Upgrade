export type TestStatus = 'A' | 'I';

/**
 * Row shape returned by GET /LIS/unit/listTestMaster/{orgId}/{isHistoCyto}/
 * {unitId}/{offset}/{size}/{searchKey} for the histopathology ('H') branch
 * (legacy histopathologyTestController.js:430-431; isHistoCyto hardcoded 'H'
 * at :407). Projection of TestMasterDto as populated by
 * SingleParamTestMasterDaoImpl.listTestMaster via the
 * GET_TEST_MASTER_LIST_ON_SINGLE_PARAMETER query (lines 560-611): mapper-copied
 * test fields plus sampleName, techniqueName, prerequisitName, normlTime,
 * urgentTime, deptName and status.
 *
 * Dev-empty: when nothing matches, the DAO answers status 'error' with the
 * plain message "Test not found." (TEST_NOT_FOUND, ICommonConstants.java:626,
 * returned at SingleParamTestMasterDaoImpl.java:617) -- the component surfaces
 * that message verbatim instead of masking it behind a generic banner.
 */
export interface HistopathologyTestRow {
  testId: number;
  testCode: string;
  testDesc: string;
  testAlies: string;
  deptName: string;
  sampleName: string;
  techniqueName: string;
  prerequisitName: string;
  normlTime: string;
  urgentTime: string;
  status: TestStatus;
}
