/**
 * Shape of one entry in `localStorage.getItem('counterList')` -- written
 * at login, read (never fetched by this service itself) by
 * `$rootScope.getCounterDetails()` (app.js:1840-1867). Legacy's own field
 * names (`id`/`name`), not `counterId`/`counterDesc` -- those only appear
 * on the *selected* shape (`CounterMst` below).
 */
export interface CounterListItem {
  id: number;
  name: string;
}

/**
 * Shape of `sessionStorage.getItem('counterMstObj')` -- what's actually
 * selected/confirmed. Matches `$rootScope.counterMstObj`
 * (app.js:1812-1838).
 */
export interface CounterMst {
  counterId: number | string;
  counterDesc: string;
}
