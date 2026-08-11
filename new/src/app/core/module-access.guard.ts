import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionService } from './session.service';

/**
 * Parity fix for docs/ANGULAR_PENDING_FINDINGS.md entry 13: legacy gates
 * every module-specific state behind a resolve block that checks
 * `localStorage.getItem('permissions').modules` (see app.js's many
 * `.state(...)` resolve blocks, e.g. organizationMaster at app.js:8279),
 * calling `$state.go("accessDenied")` on failure. There is no equivalent
 * check anywhere on the backend (confirmed: no @PreAuthorize/@Secured/
 * hasRole usage anywhere in legacy/, and JwtTokenFilter only validates
 * the JWT itself, never module/role permission) -- this guard is the
 * only enforcement layer, exactly mirroring where legacy's own enforcement
 * lives, not adding a new layer legacy doesn't have.
 *
 * Route `data` shape: `{ moduleId?: number, moduleIds?: number[],
 * requireSuperUser?: boolean }`. A route with no moduleId in its data
 * (matching a legacy state with no resolve block at all, e.g.
 * billingGroupMaster/periodicityService) still gets the baseline "must be
 * logged in" check every legacy controller does individually
 * (`cookieObject == undefined -> $state.go('login')`), but no module check
 * -- legacy itself doesn't gate those two screens by module, so this guard
 * doesn't invent a stricter rule than legacy actually has.
 *
 * The `moduleIds` array form exists for the commonMaster dispatcher family:
 * legacy's commonMaster state grants when ANY of the family's node ids is in
 * the user's permissions (its resolve filters for a 45-id array and denies
 * only when the filtered array is empty). A single-id gate would silently
 * deny every branch whose id is not the one checked, so the array form
 * mirrors legacy exactly.
 */
export const moduleAccessGuard: CanActivateFn = async (route) => {
  const session = inject(SessionService);
  const router = inject(Router);

  if (!session.current) {
    return router.parseUrl('/accessDenied?reason=notLoggedIn');
  }

  const moduleId = route.data['moduleId'] as number | undefined;
  const moduleIds = route.data['moduleIds'] as number[] | undefined;
  const requireSuperUser = route.data['requireSuperUser'] === true;

  if (moduleId !== undefined || moduleIds !== undefined) {
    // Track A #65: wait for the permissions bootstrap (one fetch per session),
    // then distinguish WHY the gate failed — the store never populated
    // (permissionsUnavailable) vs the module genuinely absent (denied). The
    // distinction is what makes a permissions-plumbing failure observable
    // instead of reading as an individual dead screen.
    await session.ensurePermissions();
    if (!session.permissionsLoaded()) {
      return router.parseUrl('/accessDenied?reason=permissionsUnavailable');
    }
    // The commonMaster family (one state, 43 dispatch branches) is gated by a
    // node-id ARRAY in legacy: its resolve grants when ANY family id is in the
    // user's permissions (arr.length <= 0 -> accessDenied). A single-id gate
    // would silently deny every branch whose id is not the one checked, so
    // the array form mirrors legacy exactly.
    const ids: number[] = moduleIds ?? (moduleId !== undefined ? [moduleId] : []);
    const anyGranted = ids.some((id) => session.hasModuleAccess(id));
    if (!anyGranted) {
      return router.parseUrl('/accessDenied?reason=denied');
    }
  }
  if (requireSuperUser && !session.isSuperUser) {
    return router.parseUrl('/accessDenied?reason=denied');
  }

  return true;
};
