#!/usr/bin/env python3
"""
Character-level scanner: try { ... } catch (...) { ... } [catch (...) { ... }]* return new Response(...);
where the trailing return sits immediately after the try/catch chain (outside it), and no catch body
contains a `return` or `throw` statement.
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
    """Blank out comments and string/char literal contents, preserving length & newlines, so brace
    counting and regex matching on the result is safe. Returns scrubbed text (same length as input)."""
    out = list(text)
    n = len(text)
    i = 0
    state = 'code'  # code, line_comment, block_comment, string, char
    while i < n:
        c = text[i]
        if state == 'code':
            if c == '/' and i + 1 < n and text[i+1] == '/':
                state = 'line_comment'
                out[i] = ' '; out[i+1] = ' '
                i += 2
                continue
            if c == '/' and i + 1 < n and text[i+1] == '*':
                state = 'block_comment'
                out[i] = ' '; out[i+1] = ' '
                i += 2
                continue
            if c == '"':
                state = 'string'
                i += 1
                continue
            if c == "'":
                state = 'char'
                i += 1
                continue
            i += 1
        elif state == 'line_comment':
            if c == '\n':
                state = 'code'
            else:
                out[i] = ' '
            i += 1
        elif state == 'block_comment':
            if c == '*' and i + 1 < n and text[i+1] == '/':
                out[i] = ' '; out[i+1] = ' '
                state = 'code'
                i += 2
                continue
            if c != '\n':
                out[i] = ' '
            i += 1
        elif state == 'string':
            if c == '\\' and i + 1 < n:
                out[i] = ' '; out[i+1] = ' '
                i += 2
                continue
            if c == '"':
                state = 'code'
                i += 1
                continue
            if c != '\n':
                out[i] = ' '
            i += 1
        elif state == 'char':
            if c == '\\' and i + 1 < n:
                out[i] = ' '; out[i+1] = ' '
                i += 2
                continue
            if c == "'":
                state = 'code'
                i += 1
                continue
            if c != '\n':
                out[i] = ' '
            i += 1
    return ''.join(out)

def line_of(pos, line_starts):
    import bisect
    return bisect.bisect_right(line_starts, pos)

def matching_close(scrubbed, open_pos):
    """Given position of an opening '{', return position of its matching '}'."""
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
        catches = []
        while True:
            p2 = skip_ws(scrubbed, cursor)
            cm = CATCH_TOKEN_RE.match(scrubbed, p2)
            if not cm:
                break
            paren_open = scrubbed.index('(', p2)
            # find matching close paren
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
            has_return_or_throw = bool(re.search(r'\b(return|throw)\b', body))
            has_logger = bool(re.search(r'logger\.(error|warn)', body, re.IGNORECASE))
            catch_start_line = line_of(p2, line_starts)
            catch_end_line = line_of(catch_close, line_starts)
            catches.append({
                'start_line': catch_start_line,
                'end_line': catch_end_line,
                'has_return_or_throw': has_return_or_throw,
                'has_logger': has_logger,
            })
            cursor = catch_close + 1
        if not catches:
            continue
        # also check for a trailing `finally { ... }` block and skip past it
        p3 = skip_ws(scrubbed, cursor)
        if scrubbed[p3:p3+7] == 'finally':
            fb = skip_ws(scrubbed, p3 + 7)
            if fb < len(scrubbed) and scrubbed[fb] == '{':
                fclose = matching_close(scrubbed, fb)
                if fclose != -1:
                    cursor = fclose + 1
        p4 = skip_ws(scrubbed, cursor)
        rm = RETURN_NEW_RESPONSE_RE.match(scrubbed, p4)
        if rm:
            all_no_return = all(not c['has_return_or_throw'] for c in catches)
            if all_no_return:
                try_line = line_of(try_open, line_starts)
                trailing_line = line_of(p4, line_starts)
                # grab the trailing return statement text (up to ';')
                semi = scrubbed.find(';', p4)
                trailing_text = raw[p4:semi+1].strip() if semi != -1 else raw[p4:p4+80].strip()
                mname, mline = nearest_method_name(lines, try_line)
                results.append({
                    'try_line': try_line,
                    'catches': catches,
                    'trailing_line': trailing_line,
                    'trailing_text': trailing_text,
                    'method': mname,
                    'method_line': mline,
                })
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
                    catch_summary = [(c['start_line'], c['end_line'], 'logged' if c['has_logger'] else 'no-log') for c in r['catches']]
                    print(f"{path}:{r['try_line']} method={r['method']}(line {r['method_line']}) "
                          f"catches={catch_summary} trailing_line={r['trailing_line']} text={r['trailing_text']!r}")
    print(f"\nTOTAL CANDIDATES: {total}", file=sys.stderr)

if __name__ == '__main__':
    main()
