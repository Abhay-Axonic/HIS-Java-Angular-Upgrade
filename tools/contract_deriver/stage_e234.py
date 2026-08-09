#!/usr/bin/env python3
"""contract_deriver stages E2-E4 — endpoint -> controller -> service -> DAO -> query.
File-level chain resolution (the controller's service field -> the impl's DAO field)
with method-name matching at each hop. derivation_method = static-chain."""
import os, re, json

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"

# ---------- index the backend java files ----------
def java_files():
    for root, dirs, files in os.walk(f"{ROOT}/legacy"):
        if "/target" in root or "/Testing" in root:
            continue
        for fn in files:
            if fn.endswith(".java"):
                yield os.path.join(root, fn)

JAVA = {}
for p in java_files():
    src = open(p, encoding="utf-8", errors="replace").read()
    JAVA[p] = src

# class simple name -> path
CLASS2PATH = {}
for p, src in JAVA.items():
    m = re.search(r"(?:public\s+)?(?:abstract\s+)?class\s+(\w+)", src)
    if m:
        CLASS2PATH.setdefault(m.group(1), p)

# ---------- E2: endpoint -> controller class + method ----------
# controller mappings: (class, class-prefix, method, method-path, http-method)
CTRL = []
for p, src in JAVA.items():
    if not p.endswith("Controller.java"):
        continue
    cm = re.search(r"class\s+(\w+)(?:\s+extends\s+[\w<>,\. ]+)?(?:\s+implements\s+[\w<>,\. ]+)?\s*\{", src)
    if not cm:
        continue
    cls = cm.group(1)
    # class-level prefix: only the annotations BEFORE the class declaration
    header = src[:cm.start()]
    pre = re.search(r"@RequestMapping\s*\(\s*(?:value\s*=\s*)?\"([^\"]+)\"", header)
    prefix = pre.group(1) if pre else ""
    for m in re.finditer(
            r"@(RequestMapping|GetMapping|PostMapping|PutMapping|DeleteMapping)\s*\(([^)]*)\)\s*"
            r"[\s\S]{0,200}?public\s+(?:(?:@[\w\.]+|[\w<>,\[\] ])+\s+)*?(\w+)\s*\(", src):
        ann, args, mname = m.group(1), m.group(2), m.group(3)
        mval = re.search(r'value\s*=\s*"([^"]*)"', args)
        mpath = mval.group(1) if mval else ""
        hm = re.search(r"method\s*=\s*RequestMethod\.(\w+)", args)
        if not hm:
            hm = {"GetMapping": "GET", "PostMapping": "POST",
                  "PutMapping": "PUT", "DeleteMapping": "DELETE"}.get(ann, "ANY")
        else:
            hm = hm.group(1)
        CTRL.append({"class": cls, "prefix": prefix, "method": mname,
                     "mpath": mpath, "http": hm, "file": p.split("/legacy/")[-1]})
print("controller method mappings:", len(CTRL))

def e2_match(http, path):
    """match an (http, path) endpoint to a controller method. Parameterized
    templates ({orgId} etc.) normalize to a wildcard for the comparison.
    The rebuild's HTTP calls carry an '/api[/<module>]' base the backend
    controllers do not map, so the stripped variant is also compared."""
    path = path.rstrip("/")
    candidates = [path]
    stripped = re.sub(r"^/api(?:/[a-zA-Z]+)?", "", path)
    if stripped != path and stripped:
        candidates.append(stripped)
    path_norm = [re.sub(r"\{[^}]+\}", "*", c) for c in candidates]
    hits = []
    for c in CTRL:
        # prefix may carry no leading slash ("api/global") and mpath may start
        # with one — normalize both to exactly one slash
        full = ("/" + c["prefix"].lstrip("/") + "/" + c["mpath"].lstrip("/")).rstrip("/") if c["prefix"] else c["mpath"].rstrip("/")
        full_norm = re.sub(r"\{[^}]+\}", "*", full)
        if full_norm in path_norm:
            if c["http"] in (http, "ANY"):
                hits.append(c)
    return hits

# ---------- E3: controller -> service (field type) -> impl -> DAO ----------
def field_service(ctrl_file, method_name):
    """the service interface the controller calls for a method, via the field
    declaration + the method's body's iService.method( call."""
    src = JAVA.get(ctrl_file, "")
    if not src:
        return None
    # find the method body
    mm = re.search(r"\b" + re.escape(method_name) + r"\s*\([^)]*\)[^{]{0,120}\{", src)
    if not mm:
        return None
    # the called service fields in the body: iXxxService.method(
    calls = re.findall(r"\bi([A-Z][\w]*Service)\s*\.\s*(\w+)\s*\(", src[mm.start():mm.start() + 4000])
    return calls or None

def service_to_impl(svc_interface_name):
    """the impl file for a service interface (the field's type is the interface;
    the impl is <name>Impl or the interface name + 'Impl')."""
    for cand in (svc_interface_name + "Impl", svc_interface_name.replace("Service", "ServiceImpl", 1)):
        p = CLASS2PATH.get(cand)
        if p:
            return p
    return None

def impl_dao_calls(impl_path, method_name):
    """the DAO calls in the impl's method body: daoVar.method( or the interface calls."""
    src = JAVA.get(impl_path, "")
    if not src:
        return []
    mm = re.search(r"\b" + re.escape(method_name) + r"\s*\([^)]*\)\s*\{", src)
    if not mm:
        return []
    body = src[mm.start():mm.start() + 4000]
    # iXxxDao.method(  and  xxxDao.method(
    calls = re.findall(r"\b(i?[A-Z][\w]*Dao)\s*\.\s*(\w+)\s*\(", body)
    return calls

# ---------- E4: DAO method -> query ----------
def dao_query_refs(dao_path, method_name):
    """the query refs in the DAO impl method: getNamedQuery("N"), createSQLQuery("..."),
    createQuery("..."), session.save/update/delete on entities."""
    src = JAVA.get(dao_path, "")
    if not src:
        return []
    mm = re.search(r"\b" + re.escape(method_name) + r"\s*\([^)]*\)[^{]{0,120}\{", src)
    if not mm:
        return []
    body = src[mm.start():mm.start() + 6000]
    refs = []
    for q in re.finditer(r'getNamedQuery\s*\(\s*"([^"]+)"', body):
        refs.append({"kind": "named", "name": q.group(1)})
    for q in re.finditer(r'create(?:SQL)?Query\s*\(\s*"([^"]+)"', body):
        refs.append({"kind": "raw", "sql": q.group(1)[:200]})
    for q in re.finditer(r"session\.(save|update|delete|saveOrUpdate|merge)\s*\(\s*(\w+)", body):
        refs.append({"kind": "entity-" + q.group(1), "entity": q.group(2)})
    return refs

# ---------- run the sample trace + the bulk ----------
def trace_chain(endpoint_path, http="POST"):
    """E2 -> E3 -> E4 for one endpoint. Returns the chain or the broken point."""
    hits = e2_match(http, endpoint_path)
    if not hits:
        return {"E2": "BROKEN", "detail": f"no controller for {http} {endpoint_path}"}
    c = hits[0]
    ctrl_file = CLASS2PATH.get(c["class"], "")
    svc_calls = field_service(ctrl_file, c["method"]) if ctrl_file else None
    if not svc_calls:
        return {"E2": c, "E3": "BROKEN", "detail": "no service call found"}
    chain = {"E2": {"controller": c["class"], "method": c["method"], "file": c["file"]}, "E3": [], "E4": []}
    for svc, m in svc_calls[:3]:
        impl = service_to_impl(svc)
        dao_calls = impl_dao_calls(impl, m) if impl else []
        dao_entries = []
        for dao, dm in dao_calls[:3]:
            cands = [dao, dao + "Impl", dao.lstrip("i") + "Impl"]
            dao_path = next((CLASS2PATH.get(c) for c in cands if CLASS2PATH.get(c)), None)
            qrefs = dao_query_refs(dao_path, dm) if dao_path else []
            dao_entries.append({"dao": dao, "method": dm, "queries": qrefs})
        chain["E3"].append({"service": svc, "method": m, "impl": impl.split("/")[-1] if impl else None, "dao": dao_entries})
    return chain

# the sample: the KNOWN sms-utility-config chain (the Rule-10 trace)
print("\n=== SAMPLE TRACE: POST /sms-utility-config (the KNOWN chain) ===")
print(json.dumps(trace_chain("/sms-utility-config", "POST"), indent=1)[:1200])
