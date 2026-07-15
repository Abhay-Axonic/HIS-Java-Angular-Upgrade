#!/usr/bin/env python3
"""
Detect: try { ... return X; } catch (...) { logger.error(...); [no return/throw] }
        [more catch blocks, same shape]
        return new Response(...);   <-- unconditional, right after the try/catch, discards real exception

Heuristic, line-based scanner over Java source. Prints candidates for manual verification.
"""
import re
import sys
import os

MODULES = sys.argv[1:] if len(sys.argv) > 1 else [
    "legacy/CSSDMaster", "legacy/Global", "legacy/Billing", "legacy/Pharmacy", "legacy/ADT"
]

METHOD_SIG_RE = re.compile(
    r'^\s*(public|private|protected)\b.*\s(\w+)\s*\([^)]*\)\s*(throws\s+[\w.,\s]+)?\s*\{?\s*$'
)
CATCH_RE = re.compile(r'^\s*\}?\s*catch\s*\(')
TRY_RE = re.compile(r'^\s*try\s*\{?\s*$')
TRAILING_RETURN_RE = re.compile(r'^\s*return\s+new\s+Response')

def find_comment_lines(lines):
    """Return set of line indices (0-based) that are fully or partly inside a /* */ block comment."""
    in_block = False
    comment_lines = set()
    for i, line in enumerate(lines):
        s = line
        j = 0
        line_has_comment = False
        while j < len(s):
            if in_block:
                line_has_comment = True
                end = s.find('*/', j)
                if end == -1:
                    j = len(s)
                else:
                    in_block = False
                    j = end + 2
            else:
                start = s.find('/*', j)
                lc = s.find('//', j)
                if start != -1 and (lc == -1 or start < lc):
                    in_block = True
                    line_has_comment = True
                    j = start + 2
                else:
                    break
        if line_has_comment:
            comment_lines.add(i)
    return comment_lines

def brace_delta(line):
    # crude: ignore braces inside "..." string literals and // comments
    s = line
    # strip line comment (not inside a string) - simplistic
    out = []
    in_str = False
    esc = False
    i = 0
    while i < len(s):
        c = s[i]
        if in_str:
            out.append(' ')
            if esc:
                esc = False
            elif c == '\\':
                esc = True
            elif c == '"':
                in_str = False
            i += 1
            continue
        if c == '"':
            in_str = True
            out.append(' ')
            i += 1
            continue
        if c == '/' and i + 1 < len(s) and s[i+1] == '/':
            break
        out.append(c)
        i += 1
    cleaned = ''.join(out)
    return cleaned.count('{') - cleaned.count('}'), cleaned

def nearest_method_name(lines, idx):
    for k in range(idx - 1, max(-1, idx - 40), -1):
        m = METHOD_SIG_RE.match(lines[k])
        if m:
            return m.group(2), k + 1
    return None, None

def scan_file(path):
    with open(path, 'r', errors='replace') as f:
        lines = f.read().split('\n')
    comment_lines = find_comment_lines(lines)
    results = []
    i = 0
    n = len(lines)
    while i < n:
        if i in comment_lines:
            i += 1
            continue
        if TRY_RE.match(lines[i]):
            try_line_idx = i
            # find matching close of try block
            depth, _ = brace_delta(lines[i])
            j = i + 1
            while j < n and depth > 0:
                if j not in comment_lines:
                    d, _ = brace_delta(lines[j])
                    depth += d
                j += 1
            # j now points just past the try's closing brace line
            try_close_idx = j - 1
            catches = []
            k = j
            # consume all attached catch blocks
            while k < n and (k in comment_lines or CATCH_RE.match(lines[k]) or (lines[k].strip() == '' and k + 1 < n)):
                if k in comment_lines:
                    k += 1
                    continue
                if lines[k].strip() == '':
                    # allow blank between catches? be conservative - only if next non-blank is catch
                    look = k + 1
                    while look < n and lines[look].strip() == '':
                        look += 1
                    if look < n and CATCH_RE.match(lines[look]):
                        k = look
                        continue
                    else:
                        break
                if not CATCH_RE.match(lines[k]):
                    break
                catch_start = k
                cdepth, _ = brace_delta(lines[k])
                m2 = k + 1
                while m2 < n and cdepth > 0:
                    if m2 not in comment_lines:
                        d, _ = brace_delta(lines[m2])
                        cdepth += d
                    m2 += 1
                catch_end = m2 - 1
                body = '\n'.join(l for idx2, l in enumerate(lines[catch_start:catch_end+1]) if (catch_start+idx2) not in comment_lines)
                has_return_or_throw = bool(re.search(r'\b(return|throw)\b', body))
                has_logger = bool(re.search(r'logger\.(error|warn)', body, re.IGNORECASE))
                catches.append((catch_start + 1, catch_end + 1, has_return_or_throw, has_logger))
                k = m2
            if catches:
                # look at next non-blank, non-comment line after last catch
                p = k
                while p < n and (p in comment_lines or lines[p].strip() == ''):
                    p += 1
                if p < n and TRAILING_RETURN_RE.match(lines[p]):
                    all_no_return = all(not c[2] for c in catches)
                    all_logged = all(c[3] for c in catches)
                    if all_no_return:
                        mname, mline = nearest_method_name(lines, try_line_idx)
                        results.append({
                            'try_line': try_line_idx + 1,
                            'catches': catches,
                            'trailing_return_line': p + 1,
                            'trailing_return_text': lines[p].strip(),
                            'all_logged': all_logged,
                            'method': mname,
                            'method_line': mline,
                        })
            i = k
            continue
        i += 1
    return results

def main():
    total = 0
    for mod in MODULES:
        for root, dirs, files in os.walk(mod):
            for fn in files:
                if not fn.endswith('.java'):
                    continue
                path = os.path.join(root, fn)
                try:
                    res = scan_file(path)
                except Exception as e:
                    print(f"# SCAN ERROR {path}: {e}", file=sys.stderr)
                    continue
                for r in res:
                    total += 1
                    print(f"{path}:{r['try_line']} method={r['method']}(line {r['method_line']}) "
                          f"catches={[(c[0],c[1],'logged' if c[3] else 'no-log') for c in r['catches']]} "
                          f"trailing_return_line={r['trailing_return_line']} text={r['trailing_return_text']!r}")
    print(f"\nTOTAL CANDIDATES: {total}", file=sys.stderr)

if __name__ == '__main__':
    main()
