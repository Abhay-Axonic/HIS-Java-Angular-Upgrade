#!/usr/bin/env python3
"""Audit: which feature dirs the walk added vs rewrote, vs pre-walk main 288d0bb1."""
import subprocess

out = subprocess.run(
    "git diff --name-status 288d0bb1 main -- new/src/app/features/",
    shell=True, capture_output=True, text=True).stdout

added = set()
modified = set()
for line in out.splitlines():
    parts = line.split("\t")
    if len(parts) >= 2:
        st, path = parts[0], parts[1]
        seg = path.split("/")
        if len(seg) >= 5 and seg[3] == "features":
            d = seg[4]
            if st == "A":
                added.add(d)
            else:
                modified.add(d)

print("ADDED dirs (walk + any other source):", len(added))
print(sorted(added))
print()
print("MODIFIED pre-existing dirs:", len(modified))
print(sorted(modified))
