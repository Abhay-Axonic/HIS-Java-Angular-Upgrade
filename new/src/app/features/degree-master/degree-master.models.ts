/** Row shape returned by getDegreeMasterList (ctrl:106-119) -- the only
 * fields the legacy view renders (degree-master.html:126-135). */
export interface DegreeMasterListRow {
  degreeId: number;
  degreeCode: string;
  degreeDesc: string;
  status: 'A' | 'I';
}

/** Shape returned by getDegreeMasterById (ctrl:257-282) -- only degreeId/
 * degreeCode/degreeDesc are read off `listObject[0]`, matching legacy. */
export interface DegreeMasterDetail {
  degreeId: number;
  degreeCode: string;
  degreeDesc: string;
}

/** Form/edit-state shape -- mirrors $scope.degreeMasterObj (ctrl:60, 273-275). */
export interface DegreeMasterFormRecord {
  degreeId: number;
  degreeCode: string;
  degreeDesc: string;
}

/** Matches ctrl:60's `$scope.degreeMasterObj = {}` reset on init(). */
export function emptyDegreeMaster(): DegreeMasterFormRecord {
  return {
    degreeId: 0,
    degreeCode: '',
    degreeDesc: '',
  };
}
