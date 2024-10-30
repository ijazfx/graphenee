import { i as s, a as p, b as h, x as f, c as l, d as v, e as m, f as b, h as C, j as c, k as x, l as w, m as y, n as I, o as P, p as A, F as E, t as F } from "./copilot-5xZabcKF.js";
import { B as D } from "./base-panel-ZMfG2SWA.js";
const _ = "copilot-outline-panel{padding:0;position:relative;height:var(--default-content-height)}copilot-outline-panel vaadin-grid{background-color:transparent;--vaadin-grid-cell-background: transparent;--vaadin-grid-cell-padding: var(--space-100) var(--space-150);font:inherit;color:inherit}copilot-outline-panel vaadin-grid::part(cell){cursor:default;min-height:auto}copilot-outline-panel vaadin-grid::part(row):hover{outline:1px solid var(--selection-color);outline-offset:-1px}copilot-outline-panel vaadin-grid::part(selected-row){background:var(--blue-100);color:var(--color-high-contrast)}copilot-outline-panel vaadin-grid::part(selected-row):hover{outline:0}copilot-outline-panel vaadin-grid::part(cell):focus-visible,copilot-outline-panel vaadin-grid::part(row):focus-visible{outline:2px solid var(--blue-300);outline-offset:-2px}copilot-outline-panel vaadin-grid-tree-toggle::part(toggle){color:var(--border-color-high-contrast);opacity:0}copilot-outline-panel:hover vaadin-grid-tree-toggle::part(toggle){opacity:1}";
var $ = Object.defineProperty, k = Object.getOwnPropertyDescriptor, u = (e, t, i, r) => {
  for (var n = r > 1 ? void 0 : r ? k(t, i) : t, o = e.length - 1, a; o >= 0; o--)
    (a = e[o]) && (n = (r ? a(t, i, n) : a(n)) || n);
  return r && n && $(t, i, n), n;
};
function g(e) {
  if (e.currentTarget)
    return e.currentTarget.getEventContext(e).item;
}
let d = class extends D {
  constructor() {
    super(...arguments), this.expandedItems = [], this.initialExpandDone = !1, this.filter = (e) => s(e) ? !0 : !!p(e), this.getFilteredChildren = (e) => {
      const t = h(e);
      if (t.length === 0)
        return [];
      const i = t.filter(this.filter);
      return i.length === t.length ? t : t.flatMap((r) => i.includes(r) ? r : this.getFilteredChildren(r));
    }, this.dataProvider = (e, t) => {
      if (!this.reactApp)
        t([], 0);
      else if (!e.parentItem)
        t([this.reactApp], 1);
      else {
        const i = this.getFilteredChildren(e.parentItem);
        t(i, i.length);
      }
    };
  }
  connectedCallback() {
    super.connectedCallback(), this.componentTreeUpdated(), this.onEventBus("component-tree-updated", () => this.componentTreeUpdated());
  }
  render() {
    return f`
      <style>
        ${_}
      </style>
      <vaadin-grid
        all-rows-visible
        .dataProvider=${this.dataProvider}
        .selectedItems=${l.getSelections.map((e) => v(e.element))}
        @keydown=${this.gridKeyDown}
        @mousemove=${this.gridItemMouseMove}
        @click=${this.gridItemClick}
        theme="no-border no-row-borders">
        <vaadin-grid-tree-column
          auto-width
          .__getToggleContent=${this.renderToggleColumn}
          .__isLeafItem=${this.isLeafItem.bind(this)}></vaadin-grid-tree-column>
      </vaadin-grid>
    `;
  }
  renderToggleColumn(e, t) {
    let i = "";
    return p(t) ? i = "♦ " : m(t) && (i = "</> "), `${i}${b(t)}`;
  }
  isLeafItem(e) {
    return this.getFilteredChildren(e).length === 0;
  }
  gridKeyDown(e) {
    e.altKey || e.metaKey || e.ctrlKey || e.shiftKey || (e.code === "Space" ? (e.preventDefault(), e.stopPropagation()) : (e.key === "Backspace" || e.key === "Delete") && (C.emit("delete-selected", {}), e.preventDefault(), e.stopPropagation()));
  }
  gridItemMouseMove(e) {
    let t;
    const i = g(e);
    i && s(i) && (t = c(i)), t ? l.setHighlighted({ element: t }) : l.setHighlighted(void 0), e.preventDefault(), e.stopPropagation();
  }
  gridItemClick(e) {
    const t = g(e);
    if (!t || !s(t))
      return;
    !e.metaKey && !e.ctrlKey && l.clearSelection();
    const i = c(t);
    i ? l.isSelected(i) ? l.deselect(i) : l.select(i) : x("Unable to find element for selection", t), w("use-outline");
  }
  updated(e) {
    super.updated(e), this.initialExpandDone || this.expandAll();
  }
  expandAll() {
    this.reactApp && this.grid && (this.grid.expandedItems = [this.reactApp, ...y(this.reactApp)], this.initialExpandDone = !0);
  }
  componentTreeUpdated() {
    if (this.reactApp = I(), this.grid) {
      if (this.reactApp) {
        const e = this.grid.expandedItems.map((t) => P(t));
        e.length > 0 && !e.includes(this.reactApp) ? this.expandAll() : this.grid.expandedItems = e;
      }
      this.grid.clearCache();
    }
    this.requestUpdate();
  }
};
u([
  A("vaadin-grid")
], d.prototype, "grid", 2);
d = u([
  F("copilot-outline-panel")
], d);
const K = {
  header: "Outline",
  expanded: !0,
  draggable: !0,
  panelOrder: 0,
  panel: "left",
  floating: !1,
  tag: "copilot-outline-panel",
  showOn: [E.HillaReact]
}, O = {
  init(e) {
    e.addPanel(K);
  }
};
window.Vaadin.copilotPlugins.push(O);
export {
  d as CopilotOutlinePanel
};
