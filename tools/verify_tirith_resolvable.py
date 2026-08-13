#!/usr/bin/env python3
"""Startup check: verify the tirith security scanner is resolvable AND functional.

Mechanism DQ-2026-08-12-052 (12th pass-while-structurally-incapable mechanism,
first on the SECURITY layer): Hermes's tirith integration returns
{"action": "allow"} on an unresolvable or non-functional scanner whenever
tirith_fail_open is true (the default) — see hermes-agent
tools/tirith_security.py check_command_security: the path-resolves-to-None,
OSError-spawn, timeout, and unknown-exit-code paths all silently allow.
Consequence: commands run unscanned and LOOK approved. The gate's health is
invisible without an explicit check (in this session the agent shell's PATH
lacked ~/.hermes/bin, so a manual `tirith` invocation failed while the gate
itself was live via the $HERMES_HOME/bin fallback).

This check makes the scanner's health LOUD: it FAILS (non-zero exit) when
tirith cannot be resolved, when it cannot be executed, or when the positive
control does not block.

Usage:
    python3 tools/verify_tirith_resolvable.py              # startup check (PASS required)
    TIRITH_BIN=/nonexistent/tirith python3 tools/verify_tirith_resolvable.py
        # negative control — must FAIL loudly (exit 3), never pass silently

Exit codes:
    0 = scanner resolvable AND functional (positive control blocked)
    1 = scanner present but INERT (positive control did not block)
    2 = scanner resolved but unusable (not executable / probe crashed)
    3 = scanner NOT RESOLVABLE (loud failure — do not proceed as if scanned)
"""

from __future__ import annotations

import json
import os
import shutil
import subprocess
import sys

# Positive control: a plain-HTTP URL passed to a network command. The
# tirith rule plain_http_to_sink must BLOCK this (exit code 1). If it does
# not, the scanner is inert and cannot be relied on for anything else.
POSITIVE_CONTROL = "curl -s http://evil-example.com/x"
EXPECTED_BLOCK_EXIT = 1

DEFAULT_HERMES_HOME = os.path.join(os.path.expanduser("~"), ".hermes")


def get_hermes_home() -> str:
    return os.environ.get("HERMES_HOME", DEFAULT_HERMES_HOME)


def load_configured_path() -> str:
    """Mirror tirith_security._load_security_config: TIRITH_BIN env override,
    else config.yaml security.tirith_path, else the bare default 'tirith'."""
    env_path = os.environ.get("TIRITH_BIN")
    if env_path:
        return env_path
    try:
        import yaml

        cfg_path = os.path.join(get_hermes_home(), "config.yaml")
        with open(cfg_path, encoding="utf-8") as fh:
            cfg = yaml.safe_load(fh) or {}
        sec = cfg.get("security", {}) or {}
        return sec.get("tirith_path", "tirith")
    except Exception:
        return "tirith"


def resolve_tirith() -> tuple[str | None, str]:
    """Resolve the tirith binary, mirroring _resolve_tirith_path's order:
    explicit path (file/executable, then PATH), else PATH lookup, else
    $HERMES_HOME/bin/tirith. Returns (path, reason)."""
    configured = os.path.expanduser(load_configured_path())
    explicit = configured != "tirith"

    if explicit:
        if os.path.isfile(configured) and os.access(configured, os.X_OK):
            return configured, ""
        found = shutil.which(configured)
        if found:
            return found, ""
        return None, f"configured tirith_path {configured!r} not found (explicit path is authoritative)"

    found = shutil.which("tirith")
    if found:
        return found, ""

    hermes_bin = os.path.join(get_hermes_home(), "bin", "tirith")
    if os.path.isfile(hermes_bin) and os.access(hermes_bin, os.X_OK):
        return hermes_bin, ""

    return None, (
        "tirith not found: not on PATH and not at $HERMES_HOME/bin/tirith "
        f"(checked {hermes_bin}). With tirith_fail_open: true this means every "
        "command is silently allowed UNSCANNED. Fix the path or set "
        "tirith_fail_open: false before proceeding."
    )


def probe_scanner(path: str) -> tuple[int, str]:
    """Run the positive control through the scanner. Returns (exit_code, detail)."""
    try:
        result = subprocess.run(
            [path, "check", "--json", "--non-interactive", "--shell", "posix", "--", POSITIVE_CONTROL],
            capture_output=True,
            text=True,
            encoding="utf-8",
            errors="replace",
            timeout=10,
            stdin=subprocess.DEVNULL,
        )
    except OSError as exc:
        return -1, f"spawn failed: {exc}"
    except subprocess.TimeoutExpired:
        return -2, "timed out after 10s"
    detail = result.stdout.strip()[:300] or result.stderr.strip()[:300]
    return result.returncode, detail


def main() -> int:
    path, reason = resolve_tirith()
    if path is None:
        print(f"FAIL [exit 3] tirith NOT RESOLVABLE — {reason}", file=sys.stderr)
        return 3

    if not (os.path.isfile(path) and os.access(path, os.X_OK)):
        print(f"FAIL [exit 2] tirith at {path} is not an executable file", file=sys.stderr)
        return 2

    rc, detail = probe_scanner(path)
    if rc == -1 or rc == -2:
        print(f"FAIL [exit 2] tirith at {path} unusable ({detail})", file=sys.stderr)
        return 2

    if rc != EXPECTED_BLOCK_EXIT:
        print(
            f"FAIL [exit 1] tirith at {path} is INERT: positive control "
            f"{POSITIVE_CONTROL!r} returned exit {rc}, expected {EXPECTED_BLOCK_EXIT} "
            f"(block). A scanner that does not block this cannot be relied on. "
            f"Detail: {detail}",
            file=sys.stderr,
        )
        return 1

    print(f"PASS — tirith resolvable at {path} and functional (positive control blocked, exit {rc})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
