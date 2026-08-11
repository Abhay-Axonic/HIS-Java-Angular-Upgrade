#!/usr/bin/env python3
"""enumerate_getdate_null.py — ITEM 4 v2: the getDate(null) enumeration.

Every call site passing a DTO-supplied date into
GlobalCommonDateUtils.getDate(...) WITHOUT a null guard, on Billing +
Global (billing-adjacent) modules. Classified:

  GUARDED — a guard (if/ternary `!= null` / `!isEmpty()`) precedes the call
            in the enclosing method (NOT a defect);
  UNGUARDED — no guard. Then classified by the enclosing method's catch:
    LOUD-THROW    — catch rethrows ServiceException/ApplicationException
                    (global handler -> 500 envelope with the message);
    LOUD-ERROR    — catch returns an ERROR envelope with a populated
                    message (ERR_500/COMMON_ERROR_MESSAGE);
    SWALLOWED-SUCCESS — catch returns SUCCESS or the method falls through
                    to a SUCCESS return (the false-success class);
    EMPTY-CATCH   — catch swallows ({} or no return) -> masked envelope;
    NO-CATCH      — no catch in the method (exception propagates, LOUD by
                    default).

Arg origin: the argument is traced to a DTO getter when the arg is a plain
variable assigned from `xxx.getXxxDate...()` earlier in the method, or when
it IS a getter call. Calls with literal/new/TimeZoneComponent args are
skipped.
"""
import os
import re

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
MODULES = ["legacy/Billing", "legacy/Global"]

CALL = re.compile(r"GlobalCommonDateUtils\.getDate\(\s*([A-Za-z0-9_.]+\(?\)?)\s*[,)]")
GETTER = re.compile(r"\.(get[A-Za-z0-9_]*Date[A-Za-z0-9_]*)\(")
SIG = re.compile(r"(public|private|protected)\s+[^\n{;]*\w+\s*\([^)]*\)\s*(throws[^{;]*)?(\{|$)")

rows = []


class StringStripper:
    """Stateful string/char-literal stripper (multi-line strings)."""

    def __init__(self):
        self.in_str = False
        self.in_ch = False

    def __call__(self, line):
        out = []
        i = 0
        n = len(line)
        while i < n:
            c = line[i]
            if self.in_str:
                if c == "\\":
                    i += 2
                    continue
                if c == '"':
                    self.in_str = False
                i += 1
                continue
            if self.in_ch:
                if c == "\\":
                    i += 2
                    continue
                if c == "'":
                    self.in_ch = False
                i += 1
                continue
            if c == '"':
                self.in_str = True
                i += 1
                continue
            if c == "'":
                self.in_ch = True
                i += 1
                continue
            out.append(c)
            i += 1
        return "".join(out)


def build_enclosing(src):
    """enclosing[i] = the method-signature line index enclosing line i."""
    n = len(src)
    enclosing = [0] * n
    cur = 0
    depth = 0
    strip = StringStripper()
    for i in range(n):
        stripped = strip(src[i])
        if SIG.search(stripped):
            cur = i
        enclosing[i] = cur
        depth += stripped.count("{") - stripped.count("}")
        # if depth drops to 0 we are between methods; next signature updates cur
    return enclosing


def method_region(src, i, enclosing):
    """Return the text of the method containing line i."""
    j = enclosing[i]
    depth = 0
    k = j
    strip = StringStripper()
    while k < len(src):
        stripped = strip(src[k])
        depth += stripped.count("{") - stripped.count("}")
        if depth <= 0 and k > j:
            break
        k += 1
    return src[j:k + 1]


def classify_guard(region, arg):
    base = arg
    m = GETTER.search(arg)
    if m:
        base = (m.group(1) or "").split("(")[0]
    else:
        fm = re.search(r"\.([A-Za-z0-9_]+Date[A-Za-z0-9_]*)\b", arg)
        if fm:
            base = fm.group(1)
    if not base:
        return False
    # guard patterns: `x != null`, `x == null`, `!x.isEmpty()`, ternary
    for pat in [
        re.compile(r"\b" + re.escape(base) + r"\(?\)?\s*(!= null|== null|!=null|==null)"),
        re.compile(r"!\s*" + re.escape(base) + r"\(?\)?\.isEmpty\(\)"),
        re.compile(r"\b" + re.escape(base) + r"\(?\)?\s*!=\s*[\"']"),
    ]:
        if pat.search(region):
            return True
    return False


def classify_env(region):
    """The catch in the method region + what it does."""
    catches = re.findall(r"catch\s*\([^)]*\)\s*\{([\s\S]*?)(?=\n\s*(catch|finally|\}))", region)
    if not catches:
        # maybe the method has no catch -> exception propagates (LOUD)
        return "NO-CATCH"
    for body in catches:
        b = body[0]
        if re.search(r"return\s+new\s+Response\s*\(\s*SUCCESS", b):
            return "SWALLOWED-SUCCESS"
        if re.search(r"throw\s+new\s+(ServiceException|ApplicationException)", b) or re.search(r"throw\s+\w+;", b):
            return "LOUD-THROW"
        if re.search(r"ERR_500|COMMON_ERROR|COMMON_ERROR_MESSAGE", b):
            return "LOUD-ERROR"
        if re.search(r"return\s+new\s+Response\s*\(\s*ERROR", b):
            return "LOUD-ERROR"
        if not re.search(r"return|throw", b):
            return "EMPTY-CATCH"
    return "UNKNOWN"


def trace_arg(src, i, arg):
    """Is the plain-variable arg assigned from a DTO getter in the method?"""
    if GETTER.search(arg) or re.search(r"\.([A-Za-z0-9_]+Date[A-Za-z0-9_]*)\b", arg):
        return True  # it IS a getter/field access
    if not re.match(r"^[a-z][A-Za-z0-9_]*$", arg):
        return False
    j = i
    while j >= 0 and not SIG.search(src[j]):
        j -= 1
    if j < 0:
        return False
    head = "".join(src[j:i])
    # assigned from a getter: `arg = xxx.get...Date...` or `arg = xxx.get...`
    return bool(re.search(r"\b" + re.escape(arg) + r"\s*=\s*[A-Za-z0-9_.]+\.get[A-Za-z0-9_]*Date", head)) or \
           bool(re.search(r"\b" + re.escape(arg) + r"\s*=\s*[A-Za-z0-9_.]+\.get[A-Za-z0-9_]*\(", head))


def scan_file(path):
    src = open(path, encoding="utf-8", errors="replace").readlines()
    enclosing = build_enclosing(src)
    for i, line in enumerate(src):
        m = CALL.search(line)
        if not m:
            continue
        arg = m.group(1)
        if arg.startswith("TimeZoneComponent") or arg.startswith("new ") or arg.startswith('"'):
            continue
        if not trace_arg(src, i, arg):
            continue
        region = method_region(src, i, enclosing)
        if region is None:
            continue
        guarded = classify_guard("".join(region), arg)
        env = classify_env("".join(region))
        rel = os.path.relpath(path, ROOT)
        rows.append((rel, i + 1, arg, "GUARDED" if guarded else "UNGUARDED", env))


def main():
    for mod in MODULES:
        for dirpath, _dirs, files in os.walk(os.path.join(ROOT, mod)):
            for f in files:
                if f.endswith(".java"):
                    scan_file(os.path.join(dirpath, f))
    unguarded = [r for r in rows if r[3] == "UNGUARDED"]
    guarded = [r for r in rows if r[3] == "GUARDED"]
    print(f"TOTAL: {len(rows)} | GUARDED: {len(guarded)} | UNGUARDED: {len(unguarded)}")
    print()
    print("=== UNGUARDED ===")
    for rel, ln, arg, _g, env in sorted(unguarded):
        print(f"{rel}:{ln} | {arg} | {env}")
    # write-path subset: Billing DAO/Service
    wp = [r for r in unguarded if r[0].startswith("legacy/Billing") and
          ("/dao/" in r[0] or "/service/" in r[0])]
    print()
    print(f"=== BILLING WRITE-PATH SUBSET (DAO/Service unguarded): {len(wp)} ===")
    for rel, ln, arg, _g, env in wp:
        print(f"{rel}:{ln} | {arg} | {env}")


if __name__ == "__main__":
    main()
