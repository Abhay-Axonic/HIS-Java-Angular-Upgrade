#!/usr/bin/env python3
"""
gov.py — governance-worktree guard (owner-approved 2026-08-10).

PREVENTS the quiet-checkout class at the CAUSE (QF-2026-08-10-B): a
repo-root `git checkout` while the dev server compiles from the repo-root
new/ silently changes what the browser serves. The dedicated governance
worktree (.claude/worktrees/gov-main, permanently on main) plus a
post-checkout guard keep the repo root ON the branch under test during a
screen.

Mechanism note: git has NO pre-checkout hook (only post-checkout, which runs
AFTER the switch). The guard therefore runs as a post-checkout hook that
REFUSES-AND-RESTORES: if a checkout moved the repo root OFF an un-merged
non-main branch while an ng-serve compiles from the repo root, the hook
immediately checks the branch under test back out and prints the error
naming the governance worktree. The serve's compile tree therefore never
stays off the branch under test — the refusal is enforced, with the fix
named.

Guards:
  1. CHECKOUT GUARD (the cause, not the symptom): a post-checkout hook
     refuses-and-restores any repo-root checkout that would leave an
     un-merged screen branch while ng-serve compiles from the repo root.
  2. WORKTREE CLAIM: main is permanently checked out in the governance
     worktree, so `git checkout main` at the repo root fails at the git
     level ("already used by worktree at .../gov-main") — the strongest
     possible guard for the main case.
  3. GOVERNANCE WORKTREE: governance commits (RULES.md, docs/) happen in
     .claude/worktrees/gov-main via `gov.py commit ...`; the repo root stays
     on the screen branch.

Usage:
  gov.py install            install the post-checkout hook
  gov.py post-checkout <new> <old> <flag>   the hook entrypoint (git calls this)
  gov.py commit <msg>       commit staged changes IN THE GOVERNANCE WORKTREE
  gov.py status             show worktree + guard state

Negative control (owner-required): with ng-serve running from the repo root,
a repo-root `git checkout <other-branch>` must refuse-and-restore with the
error naming the worktree; `git checkout main` is refused by git itself
(worktree claim).
"""
import os
import subprocess
import sys

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GOV_WORKTREE = os.path.join(REPO_ROOT, '.claude', 'worktrees', 'gov-main')
HOOK_PATH = os.path.join(REPO_ROOT, '.githooks', 'post-checkout')  # core.hooksPath = .githooks


def serve_running_from_repo_root() -> bool:
    """True if any ng-serve process has INIT_CWD under the repo root."""
    try:
        out = subprocess.run(['pgrep', '-fl', 'ng serve'],
                             capture_output=True, text=True, timeout=10).stdout
    except Exception:
        return False
    for line in out.splitlines():
        if 'INIT_CWD=' in line and REPO_ROOT in line.split('INIT_CWD=', 1)[1]:
            return True
    return False


def current_branch() -> str:
    r = subprocess.run(['git', '-C', REPO_ROOT, 'branch', '--show-current'],
                       capture_output=True, text=True)
    return r.stdout.strip()


def is_merged_into_main(branch: str) -> bool:
    """True if branch is an ancestor of main (already merged)."""
    if branch == 'main':
        return True
    r = subprocess.run(['git', '-C', REPO_ROOT, 'merge-base', '--is-ancestor',
                        branch, 'main'], capture_output=True, text=True)
    return r.returncode == 0


def post_checkout(prev_ref: str, new_ref: str, flag: str) -> int:
    """Refuse-and-restore: if the checkout left an un-merged screen branch
    while the serve compiles from the repo root, restore it and name the fix.

    git's post-checkout hook args: $1 = ref of the PREVIOUS HEAD (the branch
    we left), $2 = ref of the new HEAD, $3 = flag (1 branch, 0 file).
    """
    if os.environ.get('GOV_RESTORING') == '1':
        return 0  # this is our own restore — never recurse
    if flag == '0':
        return 0  # file checkout (checkout -- <path>) — no branch switch to protect
    # the branch we LEFT = the branch(es) pointing at the PREVIOUS HEAD ($1).
    r = subprocess.run(['git', '-C', REPO_ROOT, 'for-each-ref',
                        '--points-at', prev_ref, 'refs/heads/',
                        '--format=%(refname:short)'],
                       capture_output=True, text=True)
    prev = r.stdout.strip().splitlines()
    # multiple branches may point at the old commit; protect any un-merged one
    unmerged = [b for b in prev if b != 'main' and not is_merged_into_main(b)]
    if not unmerged:
        return 0  # old HEAD is main or only merged branches — designed transition
    if not serve_running_from_repo_root():
        return 0
    prev = unmerged[0]
    # violation: repo root left an un-merged screen branch while serve runs
    print(
        f'\n[GOV-GUARD] BLOCKED: the checkout moved the repo root off '
        f'{prev!r} (an un-merged screen branch) while the dev server compiles '
        f'from the repo root. This is the quiet-checkout class (QF-2026-08-10-B) '
        f'— a PASS here would be a green light for a tree that does not contain '
        f'the route under test. Restoring {prev!r}.\n'
        f'[GOV-GUARD] FIX: use the GOVERNANCE WORKTREE for main-side work: '
        f'{GOV_WORKTREE} (on main). To switch screen branches, stop the dev '
        f'server first, or use a worktree.\n',
        file=sys.stderr,
    )
    env = dict(os.environ, GOV_RESTORING='1')
    subprocess.run(['git', '-C', REPO_ROOT, 'checkout', '-q', prev],
                   env=env, capture_output=True, text=True)
    return 1


def main() -> int:
    cmd = sys.argv[1] if len(sys.argv) > 1 else 'status'
    if cmd == 'install':
        hook = (
            '#!/bin/sh\n'
            '# gov.py post-checkout guard (owner-approved 2026-08-10)\n'
            f'python3 {os.path.join(REPO_ROOT, "tools", "gov.py")} post-checkout "$@"\n'
        )
        with open(HOOK_PATH, 'w') as f:
            f.write(hook)
        os.chmod(HOOK_PATH, 0o755)
        print(f'installed post-checkout hook at {HOOK_PATH}')
        return 0
    if cmd == 'post-checkout':
        return post_checkout(*sys.argv[2:5])
    if cmd == 'commit':
        msg = ' '.join(sys.argv[2:])
        if not msg:
            print('usage: gov.py commit <message>', file=sys.stderr)
            return 2
        return subprocess.run(['git', '-C', GOV_WORKTREE, 'commit', '-m', msg]).returncode
    if cmd == 'status':
        print(f'repo root   : {REPO_ROOT}')
        print(f'gov worktree: {GOV_WORKTREE} (exists: {os.path.isdir(GOV_WORKTREE)})')
        print(f'serve from repo root: {serve_running_from_repo_root()}')
        print(f'current branch: {current_branch()}')
        return 0
    print(__doc__)
    return 2


if __name__ == '__main__':
    sys.exit(main())
