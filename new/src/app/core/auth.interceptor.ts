import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { SessionService } from './session.service';
import { NotificationService } from './notification.service';

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
 *
 * On a 401 response, redirects to `/accessDenied` and notifies -- matching
 * what EVERY legacy `serviceAction` variant already does on 401
 * (`$state.go('login')` + a "Token Expired!" growl,
 * resources/assets/js/service/GenericService.js:196-208 and its 6 other
 * near-identical copies in the same file, including
 * `GenericServiceForWithoutLogin`, ~line 197 -- confirmed live against the
 * `display` screen: a session-less request gets 401'd on every kiosk
 * endpoint and legacy reactively bounces to `#/login`, despite `display`'s
 * own route having no proactive login gate at all).
 *
 * Redirects to `/accessDenied` rather than a literal `/login` -- the
 * Angular app has no login route of its own yet during this hybrid
 * rollout phase (login still happens via legacy, per SessionService's own
 * doc comment), so this reuses the one redirect convention that already
 * exists (`moduleAccessGuard`'s `/accessDenied?reason=...` pattern) rather
 * than inventing a second one. This was previously a silent no-op for
 * every one of the 10 already-rebuilt pilot screens too, not something
 * `display` introduces -- fixed here, at the shared interceptor, rather
 * than duplicated per-screen. See docs/ANGULAR_PENDING_FINDINGS.md entry 33.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const session = inject(SessionService);
  const router = inject(Router);
  const notify = inject(NotificationService);

  const authToken = session.authToken;
  const authedReq = authToken
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
          userId: String(session.userId ?? ''),
          unitId: String(session.unitId ?? ''),
        },
      })
    : req;

  return next(authedReq).pipe(
    catchError((error) => {
      if (error?.status === 401) {
        notify.error('Token Expired !');
        router.navigate(['/accessDenied'], { queryParams: { reason: 'sessionExpired' } });
      }
      return throwError(() => error);
    }),
  );
};
