#!/usr/bin/env python3
"""capture_sc_time_model.py — capture the component's model for fromTime/toTime
when the inputs are typed by a USER (keystrokes, not page.fill). Reproduces
the owner's click-through: speciality + doctor + dates + typed times, then
reads (a) each input's DOM value, (b) the ngModel-bound component state via
the Angular devtools global (dev mode), (c) validity.
"""
import json
import time
import urllib.parse

from playwright.sync_api import sync_playwright

TOKEN = json.load(open("tools/.token_cache.json"))["token"]
BASE = "http://localhost:4201"


def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        page = ctx.new_page()
        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.goto(BASE, wait_until="domcontentloaded")
        page.evaluate(
            "() => localStorage.setItem('permissions', JSON.stringify({modules: [{id: 710}]}))"
        )
        page.goto(BASE + "/surgicalCalendar", wait_until="domcontentloaded")
        time.sleep(4)
        page.select_option('select[name="specialityId"]', label="General Medicine")
        time.sleep(3)
        page.select_option('select[name="doctorId"]', index=1)
        page.fill('input[name="fromDate"]', '2026-08-02')
        page.fill('input[name="toDate"]', '2026-08-09')

        # ---- TYPE like a user: keystrokes into the native time inputs
        ft = page.locator('input[name="fromTime"]')
        ft.click()
        ft.press_sequentially("09:00", delay=80)
        tt = page.locator('input[name="toTime"]')
        tt.click()
        tt.press_sequentially("12:00", delay=80)
        time.sleep(1)

        # (a) DOM values + (b) component model via Angular devtools global
        state = page.evaluate("""() => {
          const out = {};
          for (const name of ['fromTime', 'toTime', 'specialityId', 'doctorId', 'fromDate', 'toDate']) {
            const el = document.querySelector(`input[name="${name}"]`) ||
                       document.querySelector(`select[name="${name}"]`);
            out[name + '_dom'] = el ? el.value : null;
            if (el) {
              out[name + '_valid'] = el.validity ? el.validity.valid : null;
            }
          }
          // Angular component state via ng global (dev mode)
          let comp = null;
          try {
            const el = document.querySelector('app-surgical-calendar');
            comp = window.ng ? window.ng.getComponent(el) : null;
          } catch (e) { comp = 'err:' + e.message; }
          if (comp) {
            out.model = {
              fromTime: comp.form.fromTime,
              toTime: comp.form.toTime,
              specialityId: comp.form.specialityId,
              doctorId: comp.form.doctorId,
              fromDate: comp.form.fromDate,
              toDate: comp.form.toDate,
            };
            out.formValid = comp.formValid();
          } else {
            out.model = 'ng.getComponent unavailable: ' + (typeof window.ng);
          }
          return out;
        }""")
        print(json.dumps(state, indent=1))
        browser.close()


if __name__ == "__main__":
    main()
