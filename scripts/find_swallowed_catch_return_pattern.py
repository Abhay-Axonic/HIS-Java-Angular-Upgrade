#!/usr/bin/env python3
"""
Character-level scanner, sibling to find_swallowed_return_pattern2.py: finds the COMPLEMENTARY
shape that scanner explicitly excludes -- a catch block whose OWN body directly returns a
fabricated Response (`return new Response(...)`) instead of rethrowing or letting control fall
through to a trailing return outside the try/catch. This is the TrustMasterServiceimpl shape:
    try { ... } catch (Exception e) { return new Response(ERROR, ...); }
as opposed to the already-scanned shape:
    try { ... } catch (Exception e) { /* no return/throw */ } return new Response(ERROR, ...);

Reuses the same scrub/brace-matching infrastructure as find_swallowed_return_pattern2.py for
consistency with that scan's results.
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

def scrub(text):
    out = list(text)
    n = len(text)
    i = 0
    state = 'code'
    while i < n:
        c = text[i]
        if state == 'code':
            if c == '/' and i + 1 < n and text[i+1] == '/':
                state = 'line_comment'; out[i] = ' '; out[i+1] = ' '; i += 2; continue
            if c == '/' and i + 1 < n and text[i+1] == '*':
                state = 'block_comment'; out[i] = ' '; out[i+1] = ' '; i += 2; continue
            if c == '"':
                state = 'string'; i += 1; continue
            if c == "'":
                state = 'char'; i += 1; continue
            i += 1
        elif state == 'line_comment':
            if c == '\n': state = 'code'
            else: out[i] = ' '
            i += 1
        elif state == 'block_comment':
            if c == '*' and i + 1 < n and text[i+1] == '/':
                out[i] = ' '; out[i+1] = ' '; state = 'code'; i += 2; continue
            if c != '\n': out[i] = ' '
            i += 1
        elif state == 'string':
            if c == '\\' and i + 1 < n:
                out[i] = ' '; out[i+1] = ' '; i += 2; continue
            if c == '"': state = 'code'; i += 1; continue
            if c != '\n': out[i] = ' '
            i += 1
        elif state == 'char':
            if c == '\\' and i + 1 < n:
                out[i] = ' '; out[i+1] = ' '; i += 2; continue
            if c == "'": state = 'code'; i += 1; continue
            if c != '\n': out[i] = ' '
            i += 1
    return ''.join(out)

def line_of(pos, line_starts):
    import bisect
    return bisect.bisect_right(line_starts, pos)

def matching_close(scrubbed, open_pos):
    depth = 0
    i = open_pos
    n = len(scrubbed)
    while i < n:
        if scrubbed[i] == '{':
            depth += 1
        elif scrubbed[i] == '}':
            depth -= 1
            if depth == 0:
                return i
        i += 1
    return -1

def skip_ws(scrubbed, pos):
    n = len(scrubbed)
    while pos < n and scrubbed[pos].isspace():
        pos += 1
    return pos

def nearest_method_name(lines, line_no_1based):
    for k in range(line_no_1based - 2, max(-1, line_no_1based - 40), -1):
        m = METHOD_SIG_RE.match(lines[k])
        if m:
            return m.group(2), k + 1
    return None, None

TRY_TOKEN_RE = re.compile(r'\btry\b')
CATCH_TOKEN_RE = re.compile(r'catch\s*\(')
RETURN_NEW_RESPONSE_RE = re.compile(r'return\s+new\s+Response\b')

def scan_file(path):
    with open(path, 'r', errors='replace') as f:
        raw = f.read()
    scrubbed = scrub(raw)
    lines = raw.split('\n')
    line_starts = [0]
    for i, ch in enumerate(raw):
        if ch == '\n':
            line_starts.append(i + 1)

    results = []
    for m in TRY_TOKEN_RE.finditer(scrubbed):
        pos = m.end()
        pos = skip_ws(scrubbed, pos)
        if pos >= len(scrubbed) or scrubbed[pos] != '{':
            continue
        try_open = pos
        try_close = matching_close(scrubbed, try_open)
        if try_close == -1:
            continue
        cursor = try_close + 1
        while True:
            p2 = skip_ws(scrubbed, cursor)
            cm = CATCH_TOKEN_RE.match(scrubbed, p2)
            if not cm:
                break
            paren_open = scrubbed.index('(', p2)
            depth = 0
            ppos = paren_open
            while ppos < len(scrubbed):
                if scrubbed[ppos] == '(':
                    depth += 1
                elif scrubbed[ppos] == ')':
                    depth -= 1
                    if depth == 0:
                        break
                ppos += 1
            brace_pos = skip_ws(scrubbed, ppos + 1)
            if brace_pos >= len(scrubbed) or scrubbed[brace_pos] != '{':
                break
            catch_close = matching_close(scrubbed, brace_pos)
            if catch_close == -1:
                break
            body = scrubbed[brace_pos+1:catch_close]
            # THE TARGET SHAPE: catch body directly returns a fabricated Response, with no
            # rethrow anywhere in that same catch body (a catch that both logs-and-rethrows,
            # or rethrows a wrapped exception, is a different -- likely fine -- shape).
            has_throw = bool(re.search(r'\bthrow\b', body))
            fab_return_match = RETURN_NEW_RESPONSE_RE.search(body)
            if fab_return_match and not has_throw:
                has_logger = bool(re.search(r'logger\.(error|warn)', body, re.IGNORECASE))
                catch_start_line = line_of(p2, line_starts)
                catch_end_line = line_of(catch_close, line_starts)
                return_pos_abs = brace_pos + 1 + fab_return_match.start()
                return_line = line_of(return_pos_abs, line_starts)
                semi = scrubbed.find(';', return_pos_abs)
                return_text = raw[return_pos_abs:semi+1].strip() if semi != -1 else raw[return_pos_abs:return_pos_abs+80].strip()
                try_line = line_of(try_open, line_starts)
                mname, mline = nearest_method_name(lines, try_line)
                results.append({
                    'try_line': try_line,
                    'catch_start_line': catch_start_line,
                    'catch_end_line': catch_end_line,
                    'return_line': return_line,
                    'return_text': return_text,
                    'has_logger': has_logger,
                    'method': mname,
                    'method_line': mline,
                })
            cursor = catch_close + 1
    return results

def main():
    total = 0
    logged = 0
    unlogged = 0
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
                    if r['has_logger']:
                        logged += 1
                    else:
                        unlogged += 1
                    print(f"{path}:{r['catch_start_line']} method={r['method']}(line {r['method_line']}) "
                          f"catch_lines=({r['catch_start_line']}-{r['catch_end_line']}) "
                          f"logged={r['has_logger']} return_line={r['return_line']} text={r['return_text']!r}")
    print(f"\nTOTAL CANDIDATES: {total} (logged={logged}, unlogged={unlogged})", file=sys.stderr)

if __name__ == '__main__':
    main()
