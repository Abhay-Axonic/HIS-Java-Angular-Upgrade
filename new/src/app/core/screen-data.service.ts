import { Injectable } from '@angular/core';

/**
 * Rebuild-side equivalent of legacy's `screenDataService`
 * (resources/assets/js/service/screenDataService.js) -- an app-wide
 * array every "search screen" pushes its last search filters into
 * (keyed by a `screenName` field on each pushed object, so a screen's
 * own `initialCall()` finds its own entry by filtering on that name).
 * Legacy never clears/replaces by name, only appends -- faithfully
 * matched here (repeated searches on the same screen accumulate
 * multiple entries; whichever a screen's own `find`/`forEach` visits
 * last wins, same as legacy).
 */
@Injectable({ providedIn: 'root' })
export class ScreenDataService {
  private data: { screenName: string; searchrequest: Record<string, unknown> }[] = [];

  setScreenData(entry: { screenName: string; searchrequest: Record<string, unknown> }): void {
    this.data.push(entry);
  }

  getScreenData(): { screenName: string; searchrequest: Record<string, unknown> }[] {
    return this.data;
  }
}
