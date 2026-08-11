# Blood Group Matching Master — Unit Master 531 blood bank family batch 3 (DQ-2026-08-12-010)

Route `bloodGroupMatchingMaster`, module 741.

Lists the ACTIVE blood groups (GET /global/bloodBank/getActiveBloodGroupMaster/
{org}/{unit}) — the groups for which matching rules are configured. Confirmed
live 2026-08-12: SUCCESS with 3 real rows (A+ / "A Positive (Updated)", etc.).
Save posts the matching list (/api/bloodBank/saveBloodGroupMatchingMaster,
legacy ctrl:187-189).

Zero masked errors in the blood bank family (no correlation envelope, no
fall-through, no phantom column — DQ-2026-08-12-010).
