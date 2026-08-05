#!/usr/bin/env python3
"""KIOSK bucket audit: for each of the 47 entries — every caller (named file:line),
the frontend call shape (serviceAction vs RAW), the controller's HTTP verb and
method name (read vs write). The public-by-design judgment, config dependencies,
and the write+no-authenticated-caller flag are derived in the report."""
import re, subprocess, os, sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
WEB = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp")
JAVA = os.path.join(ROOT, "legacy")

K47 = [l for l in open("/tmp/kiosk47.txt").read().splitlines() if l.strip()]

def grep(pattern, path, globs):
    cmd = ["grep", "-rn", "-F"] + [f"--include={g}" for g in globs] + [pattern, path]
    r = subprocess.run(cmd, capture_output=True, text=True, cwd=ROOT)
    return [l for l in r.stdout.splitlines() if l.strip() and "/target/" not in l]

def callers(frag):
    hits = grep(frag, WEB, ("*.js", "*.html"))
    named = []
    for h in hits[:6]:
        parts = h.split(":", 2)
        if len(parts) < 3:
            continue
        rel, ln = parts[0], parts[1]
        try:
            flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
            ctx = flines[max(0, int(ln) - 2):int(ln) + 8]
        except Exception:
            continue
        shape = "serviceAction" if any("serviceAction" in l for l in ctx) else (
            "RAW" if re.search(r"\$http|\$\.ajax|\$\.get|\$\.post|axios|fetch\(", " ".join(ctx)) else "?")
        named.append(f"{os.path.basename(rel)}:{ln}[{shape}]")
    return named

def controller(frag):
    # find the controller mapping: the fragment's last path segment in the java.
    # Return the annotation + method name so the read/write verb is from the REAL mapping.
    seg = frag.rstrip("/").split("/")[-1]
    hits = grep(seg, JAVA, ("*.java",))
    out = []
    for h in hits[:6]:
        if "JwtTokenFilter" in h:
            continue
        parts = h.split(":", 2)
        rel, ln = parts[0], int(parts[1])
        try:
            flines = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read().splitlines()
        except Exception:
            continue
        ctx = flines[max(0, ln - 4):ln + 3]
        anno = next((l.strip() for l in ctx if "Mapping(" in l or "Mapping (value" in l), "")
        method = next((l.strip() for l in ctx if re.match(r"\s*(public|private|protected)\s", l)), "")
        verb = ("write" if any(a in anno for a in ("PostMapping", "PutMapping", "DeleteMapping"))
                else "read" if "GetMapping" in anno else "?")
        out.append(f"{rel.split('/')[-1]}:{ln}[{verb}]")
    return out

print("=== KIOSK 47 audit ===")
write_count = 0
no_auth_write = []
for frag in K47:
    cs = callers(frag)
    m = controller(frag)
    shapes = [c.split("[")[1].rstrip("]") for c in cs]
    has_raw = any(s == "RAW" for s in shapes)
    has_sa = any(s == "serviceAction" for s in shapes)
    verb = "?"
    write_verbs = [mm for mm in m if "[write]" in mm]
    read_verbs = [mm for mm in m if "[read]" in mm]
    if write_verbs:
        verb = "write"
    elif read_verbs:
        verb = "read"
    else:
        seg = frag.rstrip("/").split("/")[-1].lower()
        verb = "write" if any(w in seg for w in ("save", "update", "delete", "feedback", "registration")) else "read"
    if verb == "write":
        write_count += 1
        if not has_sa:
            no_auth_write.append((frag, cs))
    print(f"[{verb}] {frag}")
    print(f"   callers: {len(cs)}" + ("  (" + ", ".join(cs) + ")" if cs else "  NONE FOUND"))
    if m:
        print(f"   controller mappings: {', '.join(m[:3])}")

print(f"\n=== FLAG: write AND no authenticated (serviceAction) caller — the saveMedication/saveAndPay shape: {len(no_auth_write)} ===")
for frag, cs in no_auth_write:
    print(f"  {frag}")
    for c in cs:
        print(f"     {c}")
print(f"\nwrites among the 47: {write_count}")
