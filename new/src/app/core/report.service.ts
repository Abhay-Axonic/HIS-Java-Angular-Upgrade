import { Injectable, inject } from '@angular/core';
import { SessionService } from './session.service';

/**
 * Rebuild-side equivalent of legacy's `reportService.generate(URI, data)`
 * (resources/assets/js/service/GenericService.js's sibling
 * `reportService` registration) -- builds a hidden HTML form and submits
 * it POST, target=_blank, so the browser opens the server-generated
 * report (PDF) in a new tab. Not an XHR/fetch call -- the report is a
 * file download/render, not JSON, so this can't go through HttpClient.
 */
@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly session = inject(SessionService);

  generate(uri: string, data: unknown): void {
    const existing = document.getElementById('reportId');
    if (existing) existing.remove();

    const form = document.createElement('form');
    form.setAttribute('id', 'reportId');
    form.setAttribute('method', 'post');
    form.setAttribute('action', uri);
    form.setAttribute('target', '_blank');

    const addHidden = (name: string, value: string) => {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = name;
      input.value = value;
      form.appendChild(input);
    };

    addHidden('reportParam', JSON.stringify(data));
    addHidden('orgId', String(this.session.organizationId ?? ''));
    addHidden('unitId', String(this.session.unitId ?? ''));

    document.body.appendChild(form);
    form.submit();
  }
}
