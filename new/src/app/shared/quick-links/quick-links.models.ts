/**
 * One level-1 tile of the "Quick Links" grid embedded (via `ng-include`)
 * on `dashboardInfo`/`nurseDashboardInfo`/`doctorDashboardInfo`'s shared
 * view partial, `views/common/dashboard-quick-links.html`. That partial is
 * 18,911 lines and, per `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md:1028`, is
 * `ng-include`d by 353 different legacy states -- it is, functionally, the
 * whole application's navigation tree embedded as a widget, not content
 * that belongs to any single screen.
 *
 * Scope decision (see `dashboard-info`/`doctor-dashboard-info`/
 * `nurse-dashboard-info`'s own component docs for the full writeup): only
 * the level-1 tile grid -- the ~32 top-level category tiles actually
 * visible on these 3 screens' initial view -- is rebuilt here, with full
 * fidelity (every tile's real `checkAccess` module id, real icon, real
 * click-index, and whether it's actually wired to a click handler in
 * legacy or dead/decorative despite looking clickable). The ~600+
 * individual per-category sub-items (legacy's "level 3" panels, one
 * `<a href="#/<state>">` per real screen, spanning literally every module
 * in the app) are NOT ported here -- that's a separate, much larger
 * rebuild unit of its own, exactly the class of thing
 * `docs/ANGULAR_REBUILD_PLAYBOOK.md`'s "grow the sidebar incrementally,
 * batch by batch -- do not attempt to replicate legacy's full sidebar in
 * one pass" standing policy already covers for the Shell's own nav; this
 * partial is the same shape of problem, just embedded in a different
 * widget. Flagged to the owner as a new finding, not silently scoped out.
 */
export interface QuickLinkTile {
  /** Exact legacy tile label (`<h4>` text). */
  label: string;
  /** Relative path under `resources/assets/images/icon/`, exactly as legacy's `<img src>`. */
  icon: string;
  /**
   * The `checkAccess(id)` module id gating this tile's visibility in
   * legacy. `undefined` means the tile has NO access gate in legacy --
   * confirmed real, not an omission: "Blood Bank" is the one tile with no
   * `ng-if="checkAccess(...)"` at all in `dashboard-quick-links.html`
   * (:516-533), unlike every other tile.
   */
  moduleId?: number;
  /**
   * Whether legacy's markup actually wires an `ng-click` to this tile.
   * Several tiles keep the `cursor-pointer` styling/hover CSS but have NO
   * `ng-click` anywhere on their markup (dead, decorative-only despite
   * looking interactive) -- confirmed by reading `dashboard-quick-links.html`
   * lines 66-651 tile-by-tile: Diet & Kitchen, House Keeping,
   * Store / Inventory, Pharmacy, and Back Office are all dead this way.
   * Faithfully reproduced as non-interactive, matching the playbook's
   * "live-vs-commented-out guards" divergence pattern.
   */
  clickable: boolean;
  /**
   * The `$scope.<category>` flag `dashboardInfoMasterController.js`'s
   * `click(info)` sets for this tile's click index (see controller
   * lines 39-143). `null` when the tile IS clickable but its click index
   * has no matching `case` in `click(info)` at all -- "Procedure"
   * (`click(33)`) is the one confirmed instance: clicking it opens the
   * sub-view with no category ever resolving, a genuine legacy dead-end,
   * faithfully reproduced (though this rebuild's shared Back button, see
   * `QuickLinksPanel`, avoids literally trapping the user the way
   * legacy's own per-category-duplicated Back button would when no
   * category div matches -- a disclosed, standing shared-component
   * improvement, not a screen-specific behavior change; same class as
   * `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's policy).
   */
  category: string | null;
  /**
   * Confirmed legacy bug, faithfully reproduced: the "MIS Report" tile's
   * live `ng-click="click(17)"` (:485) and the (dead, commented-out only)
   * "Back Office" tile (:466) both target click index 17, which
   * `click(info)` maps to `$scope.backOffice = true` (controller line
   * 92-94) -- there is no separate "misReport" flag anywhere in the
   * controller. Clicking "MIS Report" in legacy therefore actually opens
   * the "Back Office" section, not an MIS Report section. Kept exactly as
   * `category: 'backOffice'` here, not "fixed".
   */
  /** A real `<a href>` legacy wraps this tile in, IN ADDITION to its
   * `ng-click`, navigating away immediately -- only "Tele Consulting"
   * has one (:562, `href="#/videoConferenceNew"`). `videoConferenceNew`
   * is a self-registered legacy state with no Angular route yet, so this
   * is rendered as a plain `href="#/videoConferenceNew"` anchor, the same
   * "real target, out of this batch's scope" convention already used by
   * `asset-scheduler`/`view-asset-scheduler` for `assetPMSScheduler`. */
  externalHref?: string;
}
