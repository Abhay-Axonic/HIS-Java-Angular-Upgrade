#!/usr/bin/env python3
"""allowlist_audit.py — per-entry audit of the JwtTokenFilter exact allowlist.

For every exact entry: allowlist form, real controller mapping form(s) (class
@RequestMapping + method mapping, all modules), frontend call reference, and a
MATCH/MISMATCH verdict. Rule-10 controls (must hold before any other verdict is
trusted): /adt/userLogin -> MATCH; /checkEmailAvailability, /otpVerification,
/forgotPasswordGeneration -> MISMATCH (real forms /adt/*).

Usage: python3 tools/allowlist_audit.py
"""
import os
import re
import subprocess
import sys

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FILTER = os.path.join(REPO, "legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java")
JAVA_ROOTS = [os.path.join(REPO, "legacy", m) for m in
              os.listdir(os.path.join(REPO, "legacy"))]
WEBAPP = os.path.join(REPO, "legacy/HIS-WAR/src/main/webapp")

MAPPING_ANNOT = re.compile(
    r"@(?:RequestMapping|GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)"
    r"\s*\(\s*(?:value\s*=\s*)?(\{.*?\}|[\"'][^\"']+[\"'])?", re.S)
STRINGS = re.compile(r"[\"']([^\"']+)[\"']")


def strip_comments(src):
    # Protect string literals first — naive comment-stripping eats code after a
    # "/*" that lives inside a string (e.g. headers=("content-type=multipart/*")).
    protected = []

    def stash(m):
        protected.append(m.group(0))
        return f"\x00{len(protected) - 1}\x00"

    def unstash(m):
        return protected[int(m.group(1))]

    src = re.sub(r'"(?:[^"\\]|\\.)*"', stash, src)
    src = re.sub(r"/\*.*?\*/", " ", src, flags=re.S)
    src = re.sub(r"//[^\n]*", " ", src)
    return re.sub(r"\x00(\d+)\x00", unstash, src)


def class_mapping(src):
    """Find the class-level @RequestMapping prefix (nearest before 'class X')."""
    m = re.search(r"\bclass\s+\w+", src)
    if not m:
        return ""
    head = src[:m.start()]
    # find the LAST mapping annotation in the head whose parens close before the class
    for am in reversed(list(MAPPING_ANNOT.finditer(head))):
        # crude paren balance: the annotation text is captured greedily enough
        val = am.group(1)
        if val is None:
            continue
        paths = [s for s in STRINGS.findall(val)]
        if paths:
            return paths[0]
    return ""


def method_mappings(src, start_idx=0):
    """Method-level mappings, scanned only from start_idx (after the class decl)."""
    out = []
    for am in MAPPING_ANNOT.finditer(src, start_idx):
        val = am.group(1)
        if val is None:
            continue
        paths = [s for s in STRINGS.findall(val)]
        for p in paths:
            if p.startswith("/") or p:
                out.append(p)
    return out


def build_real_paths():
    """Return {path: [file...]} for every controller-mapping path, all modules."""
    real = {}
    for root in JAVA_ROOTS:
        for dirpath, _, files in os.walk(root):
            if "/target/" in dirpath:
                continue
            for fn in files:
                if not fn.endswith(".java"):
                    continue
                fp = os.path.join(dirpath, fn)
                try:
                    src = open(fp, encoding="utf-8", errors="replace").read()
                except OSError:
                    continue
                if "@RequestMapping" not in src and "@GetMapping" not in src and \
                   "@PostMapping" not in src and "@PutMapping" not in src and \
                   "@DeleteMapping" not in src and "@PatchMapping" not in src:
                    continue
                clean = strip_comments(src)
                if re.search(r"@(?:RestController|Controller)\b", clean) is None:
                    continue
                cls = class_mapping(clean)
                if cls and not cls.startswith("/"):
                    cls = "/" + cls  # Spring treats slash-less class mappings as root-relative
                class_decl = re.search(r"\bclass\s+\w+", clean)
                mstart = class_decl.start() if class_decl else 0
                for mp in method_mappings(clean, mstart):
                    if not mp.startswith("/"):
                        continue
                    full = (cls.rstrip("/") + mp) if cls else mp
                    if not full.startswith("/"):
                        full = "/" + full
                    full = re.sub(r"/{2,}", "/", full)
                    real.setdefault(full, []).append(fp)
    return real


def allowlist_entries():
    src = open(FILTER, encoding="utf-8").read()
    # the exact allowlist is the FIRST List.of(...) block
    start = src.find("List.of(")
    # find its closing paren (balance)
    depth = 0
    for i in range(start, len(src)):
        if src[i] == "(":
            depth += 1
        elif src[i] == ")":
            depth -= 1
            if depth == 0:
                block = src[start:i + 1]
                break
    entries = [s for s in STRINGS.findall(block)]
    # entries may include comments-adjacent strings; the real ones are /-prefixed paths
    paths = [e for e in entries if e.startswith("/")]
    return paths


def frontend_refs(path):
    """Which JS/HTML files reference the exact path string."""
    try:
        rg = subprocess.run(
            ["grep", "-rln", re.escape(path), WEBAPP, "--include=*.js", "--include=*.html"],
            capture_output=True, text=True, timeout=120)
        return [l for l in rg.stdout.splitlines()]
    except Exception:
        return []


def main():
    allow = allowlist_entries()
    real = build_real_paths()
    print(f"allowlist exact entries parsed: {len(allow)}")
    print(f"real controller-mapping paths indexed: {len(real)}")

    rows = []
    for entry in allow:
        if entry in real:
            fe = frontend_refs(entry)
            verdict = "MATCH" if fe else "MATCH (no frontend ref — partner/external?)"
            real_forms = entry
            fe_forms = "; ".join(os.path.basename(os.path.dirname(f)) + "/" + os.path.basename(f)
                                 for f in fe[:2]) if fe else "-"
        else:
            # find real paths sharing the last segment (the dropped class-prefix class)
            last = entry.rstrip("/").rsplit("/", 1)[-1]
            candidates = sorted(p for p in real if p.rstrip("/").rsplit("/", 1)[-1] == last)
            fe = frontend_refs(entry)
            fe2 = []
            for c in candidates[:3]:
                fe2 += frontend_refs(c)
            verdict = "MISMATCH"
            real_forms = ", ".join(candidates[:3]) if candidates else "(no mapping found)"
            fe_forms = "; ".join(sorted(set(os.path.basename(os.path.dirname(f)) + "/" + os.path.basename(f)
                                            for f in fe + fe2))[:3]) if (fe or fe2) else "-"
        rows.append((entry, real_forms, fe_forms, verdict))

    # Rule-10 controls
    controls = {
        "/adt/userLogin": "MATCH",
        "/checkEmailAvailability": "MISMATCH",
        "/otpVerification": "MISMATCH",
        "/forgotPasswordGeneration": "MISMATCH",
    }
    for c, want in controls.items():
        got = next((r[3] for r in rows if r[0] == c), "NOT FOUND")
        status = "OK" if got.startswith(want) else "CONTROL FAILED"
        print(f"  control {c}: {got} (expected {want}) -> {status}")
        if status == "CONTROL FAILED":
            print("  ABORT: do not trust the audit; fix the parser first.")
            sys.exit(2)

    print(f"\n{'ALLOWLIST FORM':<52} {'REAL CONTROLLER FORM':<52} VERDICT")
    print("-" * 160)
    for entry, rf, ff, verdict in rows:
        print(f"{entry:<52} {rf:<52} {verdict}")
        if verdict == "MISMATCH":
            print(f"    frontend refs: {ff}")
    mismatches = [r for r in rows if r[3] == "MISMATCH"]
    print(f"\nRESULT: {len(rows)} entries, {len(mismatches)} MISMATCH")
    for r in mismatches:
        print(f"  MISMATCH: {r[0]}  real: {r[1]}")
    sys.exit(0 if not mismatches else 1)


if __name__ == "__main__":
    main()
