import { InjectionToken } from '@angular/core';

/**
 * Mirrors legacy `resources/assets/js/config/url.config.js`'s BASE_URL/ROOT_URL
 * (both empty strings in the current deployment -- API calls are same-origin,
 * relative paths). Kept as an injection token rather than a hardcoded
 * constant so a real deployment config (or the ngUpgrade host page) can
 * override it without touching every service.
 */
export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL', {
  providedIn: 'root',
  factory: () => '',
});
