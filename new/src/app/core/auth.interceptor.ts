import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SessionService } from './session.service';

/**
 * Attaches the same three headers legacy's GenericService.serviceAction
 * attaches to every request (resources/assets/js/service/GenericService.js:9-13):
 * `Authorization: Bearer <authToken>`, `userId`, `unitId`, all sourced from
 * the same `cookieObject` cookie SessionService already reads. Without this,
 * every backend call this app makes gets a 401 -- found live via golden-trace
 * style comparison against the legacy app: `getActiveSpecialityList` (an
 * `/adt/...` endpoint) tolerated the missing header, but `getBillingGroupMasterList`/
 * `getBillingGroupMasterCount` (`/billing/...` endpoints) both returned 401,
 * silently leaving the list table empty with no visible error.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const session = inject(SessionService);
  const authToken = session.authToken;
  if (!authToken) {
    return next(req);
  }
  return next(
    req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`,
        userId: String(session.userId ?? ''),
        unitId: String(session.unitId ?? ''),
      },
    }),
  );
};
