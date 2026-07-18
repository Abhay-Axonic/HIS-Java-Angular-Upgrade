import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModuleNode } from './organization-master.models';

/**
 * Minimal recursive checkbox tree -- rebuild-scoped equivalent of the
 * legacy `ivh-treeview` directive used for the "Module Access" modal's
 * HIS tab (the only one of the modal's 3 tabs actually reachable in the
 * legacy view; BI/OTIMS tab headers are commented out there too, so not
 * rebuilt here either -- see docs/PHASE2_PILOT_AUDIT.md section 4).
 * Deliberately simple: functional parity (select/deselect a node and its
 * visible state), not a port of ivh-treeview's full feature set.
 */
@Component({
  selector: 'app-module-tree-node',
  imports: [ModuleTreeNodeComponent],
  template: `
    <div class="tree-node">
      <label>
        <input type="checkbox" [checked]="node.selected" (change)="onToggle($any($event.target).checked)" />
        {{ node.moduleName }}
      </label>
      @if (node.children.length) {
        <div class="tree-children">
          @for (child of node.children; track child.moduleId) {
            <app-module-tree-node [node]="child" (toggled)="toggled.emit($event)" />
          }
        </div>
      }
    </div>
  `,
  styles: [
    `
      .tree-node {
        margin-left: 4px;
      }
      .tree-children {
        margin-left: 18px;
      }
    `,
  ],
})
export class ModuleTreeNodeComponent {
  @Input({ required: true }) node!: ModuleNode;
  @Output() toggled = new EventEmitter<ModuleNode>();

  onToggle(checked: boolean): void {
    this.node.selected = checked;
    this.toggled.emit(this.node);
  }
}
