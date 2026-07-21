import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs';
import { SessionService } from '../core/session.service';
import { SHELL_NAV_SECTIONS } from './shell-nav.data';
import { ShellNavSection } from './shell.models';

/**
 * Shared header + sidebar wrapping every routed screen, per
 * docs/ANGULAR_HYBRID_DEPLOYMENT_PROPOSAL.md Part 2/3. Wired as a parent
 * route's component in app.routes.ts (all existing screen routes nested
 * as children) rather than baked into `App`/each screen -- this is what
 * keeps retrofit cost at "add route metadata" rather than "touch every
 * screen," per the proposal's Part 3.
 *
 * Visual reference: legacy's `views/common/top-bar.html` (live nav bar,
 * ~lines 163-317) and `views/common/new-side-bar.html` (wrapper +
 * `.sidebar-nav` styling, its first ~120 lines) -- navy `#00003B`
 * background on both bars, 230px-wide sidebar, `#2A83F2` left-border +
 * `rgba(255,255,255,0.2)` background on the active item, matching
 * exactly. Per-item PNG icons (legacy has one bespoke image per menu
 * entry, `resources/assets/images/logo/Side_bar/**`) are NOT ported --
 * using Font Awesome glyphs instead is a disclosed simplification, not
 * an attempt at pixel-for-pixel icon parity.
 *
 * Nav-item visibility reuses the SAME permission check `moduleAccessGuard`
 * already enforces (`SessionService.hasModuleAccess`), read off each nav
 * item's own route `data.moduleId` via the Router's own route config --
 * not a second, independently-maintained permission list -- so a route's
 * moduleId only ever needs to be set once, in app.routes.ts.
 */
@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './shell.html',
  styleUrl: './shell.scss',
})
export class Shell {
  private readonly router = inject(Router);
  protected readonly session = inject(SessionService);

  protected readonly collapsed = signal(false);
  protected readonly pageTitle = signal('');

  protected readonly sections = computed<ShellNavSection[]>(() =>
    SHELL_NAV_SECTIONS.map((section) => ({
      ...section,
      items: section.items.filter((item) => this.isVisible(item.path)),
    })).filter((section) => section.items.length > 0),
  );

  constructor() {
    this.updatePageTitle();
    this.router.events.pipe(filter((e) => e instanceof NavigationEnd)).subscribe(() => this.updatePageTitle());
  }

  protected get unitName(): string {
    const cookie = this.session.current as Record<string, unknown> | null;
    return (cookie?.['unitNme'] as string) || (this.session.unitId ? `Unit ${this.session.unitId}` : '');
  }

  protected get userName(): string {
    const cookie = this.session.current as Record<string, unknown> | null;
    return (cookie?.['firstName'] as string) || (this.session.userId ? `User ${this.session.userId}` : '');
  }

  toggleCollapsed(): void {
    this.collapsed.update((v) => !v);
  }

  logout(): void {
    document.cookie = 'cookieObject=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
    localStorage.removeItem('permissions');
    sessionStorage.clear();
    window.location.href = '/';
  }

  private isVisible(path: string): boolean {
    const wantedPath = path.slice(1);
    const allRoutes = this.router.config.flatMap((r) => r.children ?? [r]);
    const route = allRoutes.find((r) => r.path === wantedPath);
    const moduleId = route?.data?.['moduleId'] as number | undefined;
    return moduleId === undefined || this.session.hasModuleAccess(moduleId);
  }

  private updatePageTitle(): void {
    let route = this.router.routerState.snapshot.root;
    while (route.firstChild) {
      route = route.firstChild;
    }
    this.pageTitle.set((route.data?.['title'] as string) ?? '');
  }
}
