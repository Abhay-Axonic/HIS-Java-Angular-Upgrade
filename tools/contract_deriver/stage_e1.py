#!/usr/bin/env python3
"""contract_deriver stage E1 — screen -> endpoint edges from the legacy AngularJS
call sites. Part of the contract deriver (Track A phase 0, owner-directed run).

Edge: screen -> endpoint. source = the screen's state name; target = the
resolved HTTP path (method + path) called by the screen's JS controller.
derivation_method = static-callsite; confidence = strong when the URI resolves
from literal concatenation, weak when the path is dynamic.
"""
import os, re, json, sqlite3

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
JS_ROOT = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller"
VIEWS_ROOT = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/views"

# ---- the call-site regexes ----
# GenericService.serviceAction("METHOD", URI, data)  /  $http.post(URI, data) / etc.
CALL_RE = re.compile(
    r"(?:GenericService\.serviceAction|genericService\.serviceAction|serviceAction)\s*\(\s*[\"']([A-Z]+)[\"']\s*,\s*([^,)]+)",
    re.S)
HTTP_RE = re.compile(r"\$http\.(get|post|put|delete)\s*\(\s*([^,\)]+)", re.S)

# a URI argument that is a literal concatenation: BASE_URL + ROOT_URL + "/path" or
# template literal with embedded path, or a bare "/path" or "path"
def resolve_uri(expr):
    """Return the endpoint path + method hint from a JS URI expression, or None."""
    expr = expr.strip()
    # template literal
    tl = re.search(r"`([^`]*)`", expr)
    if tl:
        t = tl.group(1)
        m = re.search(r"(?:ROOT_URL|BASE_URL).*?(/[\w\-/{}.]+)", t)
        return (m.group(1) if m else None)
    # quoted path segments inside the concatenation
    segs = re.findall(r"[\"'](/[\w\-/{}.]+)[\"']", expr)
    if segs:
        # the ROOT_URL-relative path is usually the last absolute-looking segment
        path = segs[-1]
        return path
    # bare absolute path
    m = re.fullmatch(r"\s*[\"'](/[\w\-/{}.]+)[\"']\s*", expr)
    if m:
        return m.group(1)
    return None

def screen_controller_map():
    """view template -> ng-controller name, from the views."""
    out = {}
    for root, dirs, files in os.walk(VIEWS_ROOT):
        for fn in files:
            if not fn.endswith(".html"):
                continue
            p = os.path.join(root, fn)
            src = open(p, encoding="utf-8", errors="replace").read()
            m = re.search(r'ng-controller\s*=\s*["\']([^"\']+)["\']', src)
            if m:
                rel = p.replace(VIEWS_ROOT + "/", "")
                out[rel] = m.group(1)
    return out

def controller_file_for(name):
    """JS controller file for an Angular controller name (the controller's own
    definition, by the $scope/angular.module registration or the file name)."""
    name_l = name.lower()
    # try the direct file-name match first
    for root, dirs, files in os.walk(JS_ROOT):
        for fn in files:
            if fn.lower() == name_l + ".js":
                return os.path.join(root, fn)
    # controller( name, ...) registration search
    for root, dirs, files in os.walk(JS_ROOT):
        for fn in files:
            if not fn.endswith(".js"):
                continue
            p = os.path.join(root, fn)
            src = open(p, encoding="utf-8", errors="replace").read()
            if re.search(r"\.controller\(\s*[\"']" + re.escape(name) + r"[\"']", src):
                return p
    return None

def extract_endpoints(js_path):
    """(method, path, confidence) list from a JS controller file.
    Sequential scan: each call site resolves against the most recent
    assignment of the variable in source order."""
    src = open(js_path, encoding="utf-8", errors="replace").read()
    out = []
    var_path = {}
    # tokenize the source into ordered statements
    tok = re.compile(
        r"var\s+([A-Za-z_$][\w$]*)\s*=\s*([^;]+);"
        r"|serviceAction\s*\(\s*[\"']([A-Z]+)[\"']\s*,\s*([^,)]+)"
        r"|\$http\.(get|post|put|delete)\s*\(\s*([^,\)]+)"
        r"|([A-Za-z_$][\w$]*)\s*\+=\s*[\"'](/[\w\-/{}.]+)[\"']")
    for m in tok.finditer(src):
        if m.group(1):  # var X = expr;
            p = resolve_uri(m.group(2))
            if p:
                var_path[m.group(1)] = p
        elif m.group(3):  # serviceAction("M", arg)
            method, arg = m.group(3), m.group(4).strip()
            p = resolve_uri(arg)
            if not p and arg in var_path:
                p = var_path[arg]
            if p:
                out.append((method, p, "strong"))
            else:
                out.append((method, "<dynamic:" + arg + ">", "weak"))
        elif m.group(5):  # $http.get(arg)
            method, arg = m.group(5).upper(), m.group(6).strip()
            p = resolve_uri(arg)
            if not p and arg in var_path:
                p = var_path[arg]
            if p:
                out.append((method, p, "strong"))
            else:
                out.append((method, "<dynamic:" + arg + ">", "weak"))
        elif m.group(7):  # URI += "/path";
            if m.group(7) in var_path:
                var_path[m.group(7)] += m.group(8)
    return out

def main():
    svc = screen_controller_map()
    print("views with ng-controller:", len(svc))
    edges = []
    index = json.load(open(f"{OUT}/index.json"))
    for row in index["route_rows"]:
        state = row["state"]
        fields = [f.strip() for f in row["row"].split("|")]
        # fields: ['', state, url, template, controller, ...] (leading pipe -> index 4)
        ctrl_file = fields[4] if len(fields) > 4 else ""
        if not ctrl_file:
            continue
        # resolve the controller JS file anywhere under the js root
        js = None
        ctrl_base = ctrl_file.replace(".js", "")
        for root, dirs, files in os.walk(JS_ROOT):
            if ctrl_base + ".js" in files:
                js = os.path.join(root, ctrl_base + ".js")
                break
        if not js:
            continue
        eps = extract_endpoints(js)
        for method, path, conf in eps:
            edges.append({"screen": state, "edge_type": "E1", "source": state,
                          "target": f"{method} {path}", "derivation_method": "static-callsite",
                          "confidence": conf, "validated": "no",
                          "quirk_flags": "", "detail": f"{fields[2]} -> {ctrl_file}"})
    json.dump(edges, open(f"{OUT}/e1_edges.json", "w"), indent=1)
    print("E1 edges:", len(edges), "over", len(set(e["screen"] for e in edges)), "screens")
    known = [e for e in edges if e["screen"] == "smsConfiguration"]
    print("smsConfiguration E1 edges:", [(e["target"], e["confidence"]) for e in known][:8])

if __name__ == "__main__":
    main()
