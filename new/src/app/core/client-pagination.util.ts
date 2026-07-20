/**
 * Faithful port of legacy's PaginationService
 * (resources/assets/js/service/paginationService.js) -- a client-side
 * "fetch everything once, filter+paginate in the browser" pattern, DIFFERENT
 * from the Phase 3 scaffold's server-side offset/limit pagination. Several
 * Asset Management screens (scheduleWorklist, PMSchedule) fetch a full
 * result set from one endpoint and page/search it locally exactly this way
 * -- shared here rather than reimplemented per screen.
 *
 * Legacy's `searchData` uses AngularJS's built-in `$filter('filter')(data,
 * {$: search})` -- a generic "does any field contain this substring"
 * match. Replicated here via a JSON-stringify substring check, a close
 * but not byte-for-byte identical approximation (AngularJS's filter does
 * per-field string coercion and skips $$-prefixed/function properties;
 * this checks the whole serialized row instead) -- disclosed as a
 * reasonable approximation for a search-box convenience feature, not
 * business logic.
 */
export interface PaginationState<T> {
  currentPage: number;
  totalPages: number;
  pageSize: string;
  search: string;
  filteredData: T[];
  data: T[];
}

export function createPaginationState<T>(pageSize = '10'): PaginationState<T> {
  return { currentPage: 1, totalPages: 0, pageSize, search: '', filteredData: [], data: [] };
}

export function setPagedData<T>(p: PaginationState<T>): void {
  const limit = parseInt(p.pageSize, 10);
  const offset = (p.currentPage - 1) * limit;
  p.data = p.filteredData.slice(offset, offset + limit);
}

export function setTotalPages<T>(p: PaginationState<T>): void {
  p.currentPage = 1;
  p.totalPages = Math.ceil(p.filteredData.length / parseInt(p.pageSize, 10));
  setPagedData(p);
}

export function searchData<T>(data: T[], p: PaginationState<T>): void {
  if (p.search) {
    const term = p.search.toLowerCase();
    p.filteredData = data.filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  } else {
    p.filteredData = data;
  }
  setTotalPages(p);
}

export function getPagesList<T>(p: PaginationState<T>): number[] {
  let rangeSize = 5;
  const pages: number[] = [];
  let start = p.currentPage;
  if (p.totalPages < rangeSize) rangeSize = p.totalPages;
  if (start > p.totalPages - rangeSize) start = p.totalPages - rangeSize + 1;
  for (let i = start; i < start + rangeSize; i++) pages.push(i);
  return pages;
}

export function setPage<T>(currentPage: number, p: PaginationState<T>): void {
  p.currentPage = currentPage;
  setPagedData(p);
}
export function setFirstPage<T>(p: PaginationState<T>): void {
  setPage(1, p);
}
export function setPreviousPage<T>(p: PaginationState<T>): void {
  setPage(p.currentPage - 1, p);
}
export function setNextPage<T>(p: PaginationState<T>): void {
  setPage(p.currentPage + 1, p);
}
export function setLastPage<T>(p: PaginationState<T>): void {
  setPage(p.totalPages, p);
}
