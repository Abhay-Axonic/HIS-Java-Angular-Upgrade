"""
Shared paths and constants for the angular-audit tool suite.

Path resolution is anchored on this file's own location, not on the
caller's working directory or a hardcoded username/home path -- run these
scripts from anywhere and they still find the right files.
"""
from pathlib import Path

TOOLS_DIR = Path(__file__).resolve().parent
REPO_ROOT = TOOLS_DIR.parent.parent
WEBAPP = REPO_ROOT / "legacy" / "HIS-WAR" / "src" / "main" / "webapp"
APP_JS = WEBAPP / "resources" / "assets" / "js" / "config" / "app.js"
INDEX_HTML = WEBAPP / "index.html"
DOCS_DIR = REPO_ROOT / "docs"

DATA_DIR = TOOLS_DIR / "data"
STATES_JSON = DATA_DIR / "states.json"
DEAD_STATES_JSON = DATA_DIR / "dead_states.json"
SHADOWED_STATES_JSON = DATA_DIR / "shadowed_states.json"
CONTROLLER_INDEX_JSON = DATA_DIR / "controller_index.json"
CONTROLLER_STATS_JSON = DATA_DIR / "controller_stats.json"
STATES_ENRICHED_JSON = DATA_DIR / "states_enriched.json"

INVENTORY_DOC = DOCS_DIR / "ANGULAR_PHASE0_SCREEN_INVENTORY.md"

# Vendor libraries bundled into the app tree -- not app code, excluded from
# controller-registration scanning.
VENDOR_DIR_NAMES = {"angular", "kendo", "ckeditor", "prnJs", "dateTimePicker"}
# resources/assets/css/angular.js is the AngularJS *library* itself, misplaced
# in a css/ folder -- its own source comments register example controllers
# that are not real app controllers.
VENDOR_FILE_PATHS = {
    "resources/assets/css/angular.js",
    "resources/assets/css/angular.min.js",
    # Bundled third-party libraries not caught by VENDOR_DIR_NAMES (they
    # don't live under a directory literally named one of those). Found
    # 2026-07-18 while extending extract_states.py to scan the whole tree:
    # jQuery/jQuery UI/Kendo UI all have their own, unrelated `.state()`
    # widget methods, which false-positive-matched the ui-router state
    # parser and produced name-less junk entries.
    "resources/assets/js/jquery.min.js",
    "resources/assets/js/jquery-ui.min.js",
    "resources/assets/js/OT-Schedule-JS/jquery-new2.min.js",
    "resources/assets/js/yearpicker/jquery.min.js",
    "resources/assets/js/common/kendo.min.js",
    "resources/assets/js/common/kendo.all.min.js",
}
# Unresolved git-merge-tool conflict artifacts found in the tree (see
# docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md, "known gaps") -- not legitimate
# active source.
MERGE_ARTIFACT_MARKERS = ("_REMOTE_", "_BASE_", "_BACKUP_", "_LOCAL_")

# Controllers referenced by >= this many states are treated as "shared/generic"
# rather than screen-specific for complexity/coupling attribution purposes.
SHARED_CONTROLLER_THRESHOLD = 4


def ensure_data_dir():
    DATA_DIR.mkdir(parents=True, exist_ok=True)
