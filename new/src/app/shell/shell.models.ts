/**
 * Nav item metadata for the shared shell's sidebar. Intentionally NOT a
 * port of legacy's full `views/common/new-side-bar.html` (5,361 lines,
 * ~1,023 `<li>` entries across the whole hospital's module list) -- see
 * docs/ANGULAR_HYBRID_DEPLOYMENT_PROPOSAL.md Part 2. This list covers only
 * the screens actually rebuilt so far and grows batch by batch, alongside
 * a "Legacy" escape-hatch link back to the full app for everything else.
 */
export interface ShellNavItem {
  path: string;
  title: string;
}

export interface ShellNavSection {
  heading: string;
  items: ShellNavItem[];
}
