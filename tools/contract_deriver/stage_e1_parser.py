#!/usr/bin/env python3
"""contract_deriver stage E1 — screen -> endpoint edges, PARSER-BASED extraction
(esprima, the ASI-correct statement boundaries by specification).

Replaces the line-scanner's extraction. The legacy JS's missing-semicolon
statement crossings (var data1={...} with no trailing ';') consumed the next
statement in the regex scanner; the parser splits statements per the ES spec,
so the var URI = ... assignment registers and the call resolves correctly."""
import os, re, json, esprima

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
JS_ROOT = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller"
VIEWS_ROOT = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/views"

PATH_SEG = re.compile(r"[\"'](/[\w\-/{}.]+)[\"']")
TEMPLATE_SEG = re.compile(r"`([^`]*)`")

def static_parts(node):
    """collect the static string parts of a Literal/TemplateLiteral/BinaryExpression."""
    if node is None:
        return []
    t = node.get("type")
    if t == "Literal" and isinstance(node.get("value"), str):
        return [node["value"]]
    if t == "TemplateLiteral":
        return [q.get("cooked") or q.get("raw", "") for q in node.get("quasis", []) if q.get("cooked") or q.get("raw")]
    if t == "BinaryExpression" and node.get("operator") == "+":
        return static_parts(node.get("left")) + static_parts(node.get("right"))
    return []

def resolve_expr(node, var_path):
    """the endpoint path from a URL expression node."""
    if node is None:
        return None
    t = node.get("type")
    if t == "Literal" and isinstance(node.get("value"), str):
        m = PATH_SEG.search(node["value"])
        return m.group(1) if m else None
    if t == "TemplateLiteral":
        joined = "".join(q.get("cooked") or q.get("raw", "") for q in node.get("quasis", []))
        m = re.search(r"(?:ROOT_URL|BASE_URL).*?(/[\w\-/{}.]+)", joined)
        if m:
            return m.group(1)
        m2 = PATH_SEG.search(joined)
        return m2.group(1) if m2 else None
    if t == "BinaryExpression" and node.get("operator") == "+":
        parts = static_parts(node)
        joined = "".join(parts)
        segs = PATH_SEG.findall(joined)
        if segs:
            return segs[-1]
        m = re.search(r"/[\w\-/{}.]+", joined)
        return m.group(0) if m else None
    if t == "Identifier":
        return var_path.get(node.get("name"))
    return None

def iter_statements(node):
    """yield (statement_node, in_order) recursively; track assignments in order."""
    # a generator that walks the AST in source order
    yield node
    for key in ("body", "consequent", "alternate", "init", "declarations", "expression",
                "argument", "callee", "left", "right"):
        child = node.get(key)
        if child is None:
            continue
        if isinstance(child, list):
            for c in child:
                if isinstance(c, dict):
                    yield from iter_statements(c)
        elif isinstance(child, dict):
            yield from iter_statements(child)

def extract_endpoints(js_path):
    """(method, path, confidence) list from a JS controller file via the AST."""
    src = open(js_path, encoding="utf-8", errors="replace").read()
    try:
        ast = esprima.parseScript(src, {"tolerant": True, "loc": True})
        ast = ast.toDict()
    except Exception as e:
        return [("?", "<parse-error:" + str(e)[:40] + ">", "weak")]

    # walk the statements in order, tracking the assignments + the call sites
    var_path = {}
    out = []
    def walk(node, order):
        t = node.get("type")
        if t == "VariableDeclaration":
            for d in node.get("declarations", []):
                init = d.get("init")
                if init:
                    p = resolve_expr(init, var_path)
                    if p:
                        var_path[d.get("id", {}).get("name")] = p
        elif t == "ExpressionStatement":
            e = node.get("expression", {})
            if e.get("type") == "AssignmentExpression" and e.get("operator") == "=":
                lhs = e.get("left", {})
                if lhs.get("type") == "Identifier":
                    p = resolve_expr(e.get("right"), var_path)
                    if p:
                        var_path[lhs.get("name")] = p
        elif t == "CallExpression":
            callee = node.get("callee", {})
            if callee.get("type") == "MemberExpression":
                obj = callee.get("object", {})
                prop = callee.get("property", {})
                oname = obj.get("name") if obj.get("type") == "Identifier" else ""
                pname = prop.get("name") if prop.get("type") == "Identifier" else ""
                args = node.get("arguments", [])
                if oname.lower() in ("genericservice", "$http") and pname:
                    if pname == "serviceAction" and len(args) >= 2:
                        method = ""
                        if args[0].get("type") == "Literal":
                            method = str(args[0].get("value", "")).upper()
                        p = resolve_expr(args[1], var_path)
                        if p:
                            out.append((method or "?", p, "strong"))
                        else:
                            nm = args[1].get("name") if args[1].get("type") == "Identifier" else "?"
                            out.append((method or "?", "<dynamic:" + nm + ">", "weak"))
                    elif pname.lower() in ("get", "post", "put", "delete", "patch") and args:
                        p = resolve_expr(args[0], var_path)
                        if p:
                            out.append((pname.upper(), p, "strong"))
                        else:
                            nm = args[0].get("name") if args[0].get("type") == "Identifier" else "?"
                            out.append((pname.upper(), "<dynamic:" + nm + ">", "weak"))

    def rec(node):
        walk(node, 0)
        for key in ("body", "consequent", "alternate", "declarations", "expression",
                    "argument", "arguments", "init", "left", "right", "object",
                    "property", "callee", "params", "elements", "properties", "key", "value",
                    "block", "test", "update"):
            child = node.get(key)
            if child is None:
                continue
            if isinstance(child, list):
                for c in child:
                    if isinstance(c, dict):
                        rec(c)
            elif isinstance(child, dict):
                rec(child)

    rec(ast)
    return out

def screen_controller_map():
    out = {}
    for root, dirs, files in os.walk(VIEWS_ROOT):
        for fn in files:
            if not fn.endswith(".html"):
                continue
            src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
            m = re.search(r'ng-controller\s*=\s*["\']([^"\']+)["\']', src)
            if m:
                out[fn] = m.group(1)
    return out

def main():
    index = json.load(open(f"{OUT}/index.json"))
    edges = []
    for row in index["route_rows"]:
        state = row["state"]
        fields = [f.strip() for f in row["row"].split("|")]
        ctrl_file = fields[4] if len(fields) > 4 else ""
        if not ctrl_file:
            continue
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
                          "target": f"{method} {path}", "derivation_method": "static-callsite-parser",
                          "confidence": conf, "validated": "no",
                          "quirk_flags": "", "detail": f"{fields[2]} -> {ctrl_file}"})
    json.dump(edges, open(f"{OUT}/e1_edges_parser.json", "w"), indent=1)
    print("E1 edges (parser):", len(edges), "over", len(set(e["screen"] for e in edges)), "screens")

if __name__ == "__main__":
    main()
