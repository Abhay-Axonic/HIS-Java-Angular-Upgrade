#!/usr/bin/env python3
"""Live verification of the vaccination-master (screen 10) feeds, per the
owner's standing rule: endpoints verified BY CALLING with the cached token.
Calls the three mapped endpoints (list, count, ROA) and reports each URL,
status, and envelope. Any non-200 or error envelope is a FINDING: named,
logged, not fixed."""
import json
import re
import urllib.error
import urllib.request

cache = json.load(open('tools/.token_cache.json'))
tok = cache['token']
H = {
    'Authorization': f'Bearer {tok}',
    'Content-Type': 'application/json',
    'userid': '1',
    'unitid': '1',
}
base = 'http://localhost:8080'


def call(method, path, body=None):
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(base + path, data=data, headers=H, method=method)
    try:
        r = urllib.request.urlopen(req, timeout=15)
        b = r.read().decode()
        m = re.search(r'"status":\s*"([^"]+)"', b)
        code = re.search(r'"code":\s*"([^"]+)"', b)
        n = b.count('vaccinationId')
        print(f'{method} {path} -> HTTP {r.status} | envelope status={m.group(1) if m else "?"} '
              f'code={code.group(1) if code else "?"} | vaccinationId hits={n}')
        return r.status, (m.group(1) if m else None), b
    except urllib.error.HTTPError as e:
        body = e.read().decode()
        print(f'{method} {path} -> HTTP {e.code} | body={body[:300]}')
        return e.code, None, body
    except Exception as e:
        print(f'{method} {path} -> ERR {e}')
        return 0, None, str(e)


# 1. POST list — org+unit scoped grid feed
call('POST', '/api/billing/getVaccinationMasterList', {'organizationId': 1, 'unitId': 1})
# 2. POST count
call('POST', '/api/billing/getVaccinationMasterCount', {'organizationId': 1, 'unitId': 1})
# 3. GET ROA — org+unit path params
call('GET', '/api/billing/getRouteOfAdministrationList/1/1')
