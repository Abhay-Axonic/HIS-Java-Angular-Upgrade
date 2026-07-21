/**
 * Field shapes taken directly from
 * legacy/Global/src/main/java/com/param/global/dto/UserCounterMapperDto.java,
 * the `@NamedNativeQuery` aliases in
 * legacy/Entity/src/main/java/com/param/global/model/UserCounterMapper.java
 * (USER_COUNTER_MAPPER_LIST / GET_COUNTER_BY_USERID / TOTAL_USER_MAPPER_COUNT),
 * legacy/Entity/src/main/java/com/param/global/model/CounterMaster.java
 * (GET_COUNTERS -- id/name/code), and
 * legacy/Entity/src/main/java/com/param/adt/master/unit/model/UserNursingStationMapper.java
 * (GET_LIST_OF_USER -- userId/employeeName/empCode) -- not guessed. See
 * user-counter-mapper.ts's class doc for the silent bug reproduced against
 * these exact fields.
 */

/** Left-hand "User" <select> options -- GET_USER_LIST, served by
 * UserNursingStationMapperController (a DIFFERENT controller from
 * UserCounterMapperController), ctrl:150-167. */
export interface UserOption {
  userId: number;
  employeeName: string;
}

/** Left-hand "Counter" checkbox list -- /api/global/unit/counter/organization/{orgId}/unit/{unitId}
 * (searchGroupList, ctrl:95-117). Not served by UserCounterMapperController --
 * a separate CounterMasterController endpoint (GET_COUNTERS: id/name/code),
 * path taken verbatim from the controller. */
export interface CounterOption {
  id: number;
  name: string;
  code: string;
}

/** An entry in the working list (`selectCounter`) or the committed list
 * (`selectedCounterList`) -- see addCounter()/listOfCounters() (ctrl:122-146). */
export interface SelectedCounter {
  id: number;
  name: string;
  code: string;
}

/** USER_COUNTER_MAPPER_LIST row (the bottom "commonList" table, distinct
 * per user). */
export interface UserCounterMapperRow {
  userId: number;
  status: 'A' | 'I';
  employeeCode: string;
  employeeName: string;
}

/** GET_COUNTER_BY_USERID's listObject item -- real DTO/query fields are
 * `userCounterMpprId`/`userId`/`counterId`/`status`/`organisationId`/`unitId`/
 * `isDeleted`/`counterCode`/`name` (aliased from counter_desc)/`createdDate`/
 * `employeeName`. The Java DAO (UserCounterMapperDaoImpl.getUserCounterMapperById)
 * returns this SAME list in both the `listObject` and `object` slots of the
 * Response -- that's why legacy's showUpdateBtn() reads `response.data.object[0].userId`
 * for the user id but `response.data.listObject` for the counters, not a bug. */
export interface UserCounterMapperByIdItem {
  userCounterMpprId: number;
  userId: number;
  counterId: number;
  counterCode: string;
  name: string;
  employeeName: string;
}

/** The info-icon "Details" modal (view:135/214-217, getCounterListMasterByUserId,
 * ctrl:499-518). Its URL constant (GET_DETAILED_LIST_USER_COUNTER_MASTER =
 * "/api/global/userCounterMapper/user") is IDENTICAL to
 * GET_USER_COUNTER_MASTER_BY_ID's -- both hit `getUserCounterMapperById` ->
 * the GET_COUNTER_BY_USERID named query, which aliases the description column
 * as `name` (not `counterDesc`). The view's modal template correctly reads
 * `{{info.name}}` (view:216) -- confirming this, not a bug. The DISTINCT
 * `GET_COUNTER_BY_USER` named query (which does alias `counterDesc`) backs
 * `getUserCounterMapperViewById` at a different endpoint
 * (`/api/global/userCounterMapper/userView/...`) that no legacy JS URL
 * constant ever references -- a dead/unused Java endpoint, not wired to this
 * screen. Reuses `UserCounterMapperByIdItem`'s shape (same query, same DTO). */
export type DetailedCounterItem = UserCounterMapperByIdItem;

/** Body for ADD_USER_COUNTER_MASTER / UPDATE_USER_COUNTER_MASTER (ctrl:258-278/400-420).
 * Field names/casing (`organisationId`, not `organizationId`) match
 * UserCounterMapperDto.java verbatim -- required for correct JSON
 * deserialization server-side. Legacy sets a duplicate/dead `status` key
 * (once from `$scope.common.status`, immediately overwritten by a hardcoded
 * `"A"`) -- harmless, sent once cleanly here. */
export interface UserCounterMapperSavePayload {
  /** Legacy sends whatever the plain `<select>` gave it -- always a string
   * (ctrl:358 explicitly coerces with `+ ""` on edit-populate too). */
  userId: string;
  status: 'A';
  counterDesc: string;
  counterCode: string;
  employeeName: string;
  employeeCode: string;
  isDeleted: 'N';
  organisationId: number;
  unitId: number;
  createdBy: number;
  createdDate: string;
  updatedBy: number;
  updatedDate: string;
  empName: string;
  selectedCounterList: number[];
}
