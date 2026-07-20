import { Injectable, signal } from '@angular/core';

/**
 * Rebuild-side equivalent of legacy's `commonDetailService.setDataId`/
 * `getDataId` (resources/assets/js/service/commonDetailService.js) --
 * a single, app-wide numeric-ID handoff slot reused by many unrelated
 * "search screen -> detail screen" pairs across the app (this service
 * has ~15 other, unrelated getter/setter pairs bolted onto it for other
 * screens' handoffs; only setDataId/getDataId are ported here, since
 * that's the only part any screen in this batch uses). Deliberately a
 * plain service, not Router `extras.state`, matching every other
 * handoff service in this rebuild -- legacy's own slot does not survive
 * a real page refresh either.
 */
@Injectable({ providedIn: 'root' })
export class CommonDetailHandoffService {
  private readonly dataId = signal<number | string>(0);

  setDataId(id: number | string): void {
    this.dataId.set(id);
  }

  getDataId(): number | string {
    return this.dataId();
  }
}
