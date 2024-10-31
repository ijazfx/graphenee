import { q as T, s as v, k as H, r as y, u as h, t as f, v as E, x as n, w as Ie, p as Oe, y as Re, z as je, A as qe, B as He, C as De, D as I, E as Q, G as Ue, H as Be, F as Fe } from "./copilot-5xZabcKF.js";
import { B as We } from "./base-panel-ZMfG2SWA.js";
var D = /* @__PURE__ */ ((t) => (t.disabled = "disabled", t.enabled = "enabled", t.missing_theme = "missing_theme", t))(D || {}), u = /* @__PURE__ */ ((t) => (t.local = "local", t.global = "global", t))(u || {});
function ee(t, e) {
  return `${t}|${e}`;
}
class b {
  constructor(e) {
    this._properties = {}, this._metadata = e;
  }
  get metadata() {
    return this._metadata;
  }
  get properties() {
    return Object.values(this._properties);
  }
  getPropertyValue(e, s) {
    return this._properties[ee(e, s)] || null;
  }
  updatePropertyValue(e, s, i, o) {
    if (!i) {
      delete this._properties[ee(e, s)];
      return;
    }
    let r = this.getPropertyValue(e, s);
    r ? (r.value = i, r.modified = o || !1) : (r = {
      elementSelector: e,
      propertyName: s,
      value: i,
      modified: o || !1
    }, this._properties[ee(e, s)] = r);
  }
  addPropertyValues(e) {
    e.forEach((s) => {
      this.updatePropertyValue(s.elementSelector, s.propertyName, s.value, s.modified);
    });
  }
  getPropertyValuesForElement(e) {
    return this.properties.filter((s) => s.elementSelector === e);
  }
  static combine(...e) {
    if (e.length < 2)
      throw new Error("Must provide at least two themes");
    const s = new b(e[0].metadata);
    return e.forEach((i) => s.addPropertyValues(i.properties)), s;
  }
  static fromServerRules(e, s, i) {
    const o = new b(e);
    return e.elements.forEach((r) => {
      const a = M(r, s), l = i.find((c) => c.selector === a.replace(/ > /g, ">"));
      l && r.properties.forEach((c) => {
        const d = l.properties[c.propertyName];
        d && o.updatePropertyValue(r.selector, c.propertyName, d, !0);
      });
    }), o;
  }
}
function M(t, e) {
  const s = t.selector;
  if (e.themeScope === "global")
    return s;
  if (!e.localClassName)
    throw new Error("Can not build local scoped selector without instance class name");
  const i = s.match(/^[\w\d-_]+/), o = i && i[0];
  if (!o)
    throw new Error(`Selector does not start with a tag name: ${s}`);
  return `${o}.${e.localClassName}${s.substring(o.length, s.length)}`;
}
function Ge(t, e, s, i) {
  const o = M(t, e), r = { [s]: i };
  return s === "border-width" && (parseInt(i) > 0 ? r["border-style"] = "solid" : r["border-style"] = ""), {
    selector: o,
    properties: r
  };
}
function Ke(t) {
  const e = Object.entries(t.properties).map(([s, i]) => `${s}: ${i};`).join(" ");
  return `${t.selector} { ${e} }`;
}
const me = {
  crosshair: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M4 8v-2a2 2 0 0 1 2 -2h2"></path>
   <path d="M4 16v2a2 2 0 0 0 2 2h2"></path>
   <path d="M16 4h2a2 2 0 0 1 2 2v2"></path>
   <path d="M16 20h2a2 2 0 0 0 2 -2v-2"></path>
   <path d="M9 12l6 0"></path>
   <path d="M12 9l0 6"></path>
</svg>`,
  square: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="currentColor" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M3 3m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v14a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z"></path>
</svg>`,
  font: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M4 20l3 0"></path>
   <path d="M14 20l7 0"></path>
   <path d="M6.9 15l6.9 0"></path>
   <path d="M10.2 6.3l5.8 13.7"></path>
   <path d="M5 20l6 -16l2 0l7 16"></path>
</svg>`,
  undo: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M9 13l-4 -4l4 -4m-4 4h11a4 4 0 0 1 0 8h-1"></path>
</svg>`,
  redo: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M15 13l4 -4l-4 -4m4 4h-11a4 4 0 0 0 0 8h1"></path>
</svg>`,
  cross: T`<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" stroke-width="3" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
   <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
   <path d="M18 6l-12 12"></path>
   <path d="M6 6l12 12"></path>
</svg>`
};
let j, ve = "";
function Me(t) {
  j || (j = new CSSStyleSheet(), document.adoptedStyleSheets = [...document.adoptedStyleSheets, j]), ve += t.cssText, j.replaceSync(ve);
}
const Ae = v`
  .editor-row {
    display: flex;
    align-items: baseline;
    padding: var(--theme-editor-section-horizontal-padding);
    gap: 10px;
  }

  .editor-row > .label {
    flex: 0 0 auto;
    width: 120px;
  }

  .editor-row > .editor {
    flex: 1 1 0;
  }
`, ge = "__vaadin-theme-editor-measure-element", fe = /((::before)|(::after))$/, ye = /::part\(([\w\d_-]+)\)$/;
Me(v`
  .__vaadin-theme-editor-measure-element {
    position: absolute;
    top: 0;
    left: 0;
    visibility: hidden;
  }
`);
async function Ze(t) {
  const e = new b(t), s = document.createElement(t.tagName);
  s.classList.add(ge), document.body.append(s), t.setupElement && await t.setupElement(s);
  const i = {
    themeScope: u.local,
    localClassName: ge
  };
  try {
    t.elements.forEach((o) => {
      be(s, o, i, !0);
      let r = M(o, i);
      const a = r.match(fe);
      r = r.replace(fe, "");
      const l = r.match(ye), c = r.replace(ye, "");
      let d = document.querySelector(c);
      if (d && l) {
        const k = `[part~="${l[1]}"]`;
        d = d.shadowRoot.querySelector(k);
      }
      if (!d)
        return;
      d.style.transition = "none";
      const C = a ? a[1] : null, _ = getComputedStyle(d, C);
      o.properties.forEach(($) => {
        const k = _.getPropertyValue($.propertyName) || $.defaultValue || "";
        e.updatePropertyValue(o.selector, $.propertyName, k);
      }), be(s, o, i, !1);
    });
  } finally {
    try {
      t.cleanupElement && await t.cleanupElement(s);
    } finally {
      s.remove();
    }
  }
  return e;
}
function be(t, e, s, i) {
  if (e.stateAttribute) {
    if (e.stateElementSelector) {
      const o = M(
        {
          ...e,
          selector: e.stateElementSelector
        },
        s
      );
      t = document.querySelector(o);
    }
    t && (i ? t.setAttribute(e.stateAttribute, "") : t.removeAttribute(e.stateAttribute));
  }
}
function we(t) {
  return t.trim();
}
function Je(t) {
  const e = t.element;
  if (!e)
    return null;
  const s = e.querySelector("label");
  if (s && s.textContent)
    return we(s.textContent);
  const i = e.textContent;
  return i ? we(i) : null;
}
class Xe {
  constructor() {
    this._localClassNameMap = /* @__PURE__ */ new Map();
  }
  get stylesheet() {
    return this.ensureStylesheet(), this._stylesheet;
  }
  add(e) {
    this.ensureStylesheet(), this._stylesheet.replaceSync(e);
  }
  clear() {
    this.ensureStylesheet(), this._stylesheet.replaceSync("");
  }
  previewLocalClassName(e, s) {
    if (!e)
      return;
    const i = this._localClassNameMap.get(e);
    i && (e.classList.remove(i), e.overlayClass = null), s ? (e.classList.add(s), e.overlayClass = s, this._localClassNameMap.set(e, s)) : this._localClassNameMap.delete(e);
  }
  ensureStylesheet() {
    this._stylesheet || (this._stylesheet = new CSSStyleSheet(), this._stylesheet.replaceSync(""), document.adoptedStyleSheets = [...document.adoptedStyleSheets, this._stylesheet]);
  }
}
const S = new Xe(), p = {
  index: -1,
  entries: []
};
class Ye {
  constructor(e) {
    this.api = e;
  }
  get allowUndo() {
    return p.index >= 0;
  }
  get allowRedo() {
    return p.index < p.entries.length - 1;
  }
  get allowedActions() {
    return {
      allowUndo: this.allowUndo,
      allowRedo: this.allowRedo
    };
  }
  push(e, s, i) {
    const o = {
      requestId: e,
      execute: s,
      rollback: i
    };
    if (p.index++, p.entries = p.entries.slice(0, p.index), p.entries.push(o), s)
      try {
        s();
      } catch (r) {
        H("Execute history entry failed", r);
      }
    return this.allowedActions;
  }
  async undo() {
    if (!this.allowUndo)
      return this.allowedActions;
    const e = p.entries[p.index];
    p.index--;
    try {
      await this.api.undo(e.requestId), e.rollback && e.rollback();
    } catch (s) {
      H("Undo failed", s);
    }
    return this.allowedActions;
  }
  async redo() {
    if (!this.allowRedo)
      return this.allowedActions;
    p.index++;
    const e = p.entries[p.index];
    try {
      await this.api.redo(e.requestId), e.execute && e.execute();
    } catch (s) {
      H("Redo failed", s);
    }
    return this.allowedActions;
  }
  // Only intended to be used for testing
  static clear() {
    p.entries = [], p.index = -1;
  }
}
var Qe = Object.defineProperty, et = Object.getOwnPropertyDescriptor, x = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? et(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && Qe(e, s, o), o;
};
class tt extends CustomEvent {
  constructor(e, s, i) {
    super("theme-property-value-change", {
      bubbles: !0,
      composed: !0,
      detail: { element: e, property: s, value: i }
    });
  }
}
class g extends E {
  constructor() {
    super(), this.value = "";
  }
  static get styles() {
    return [
      Ae,
      v`
        :host {
          display: block;
        }

        .editor-row .label .modified {
          display: inline-block;
          width: 6px;
          height: 6px;
          background: orange;
          border-radius: 3px;
          margin-left: 3px;
        }
      `
    ];
  }
  update(e) {
    super.update(e), (e.has("propertyMetadata") || e.has("theme")) && this.updateValueFromTheme();
  }
  render() {
    var e;
    return n`
      <div class="editor-row">
        <div class="label">
          ${this.propertyMetadata.displayName}
          ${(e = this.propertyValue) != null && e.modified ? n`<span class="modified"></span>` : null}
        </div>
        <div class="editor">${this.renderEditor()}</div>
      </div>
    `;
  }
  updateValueFromTheme() {
    var e;
    this.propertyValue = this.theme.getPropertyValue(this.elementMetadata.selector, this.propertyMetadata.propertyName), this.value = ((e = this.propertyValue) == null ? void 0 : e.value) || "";
  }
  dispatchChange(e) {
    this.dispatchEvent(new tt(this.elementMetadata, this.propertyMetadata, e));
  }
}
x([
  h({})
], g.prototype, "elementMetadata", 2);
x([
  h({})
], g.prototype, "propertyMetadata", 2);
x([
  h({})
], g.prototype, "theme", 2);
x([
  y()
], g.prototype, "propertyValue", 2);
x([
  y()
], g.prototype, "value", 2);
class U {
  constructor(e) {
    if (this._values = [], this._rawValues = {}, e) {
      const s = e.propertyName, i = e.presets ?? [];
      this._values = (i || []).map((r) => r.startsWith("--") ? `var(${r})` : r);
      const o = document.createElement("div");
      o.style.borderStyle = "solid", o.style.visibility = "hidden", document.body.append(o);
      try {
        this._values.forEach((r) => {
          o.style.setProperty(s, r);
          const a = getComputedStyle(o);
          this._rawValues[r] = a.getPropertyValue(s).trim();
        });
      } finally {
        o.remove();
      }
    }
  }
  get values() {
    return this._values;
  }
  get rawValues() {
    return this._rawValues;
  }
  tryMapToRawValue(e) {
    return this._rawValues[e] ?? e;
  }
  tryMapToPreset(e) {
    return this.findPreset(e) ?? e;
  }
  findPreset(e) {
    const s = e && e.trim();
    return this.values.find((i) => this._rawValues[i] === s);
  }
}
class xe extends CustomEvent {
  constructor(e) {
    super("change", { detail: { value: e } });
  }
}
let B = class extends E {
  constructor() {
    super(), this.value = "", this.showClearButton = !1;
  }
  static get styles() {
    return v`
      :host {
        display: inline-block;
        width: 100%;
        position: relative;
      }

      input {
        width: 100%;
        box-sizing: border-box;
        padding: 0.25rem 0.375rem;
        color: inherit;
        background: rgba(0, 0, 0, 0.2);
        border-radius: 0.25rem;
        border: none;
      }

      button {
        display: none;
        position: absolute;
        right: 4px;
        top: 4px;
        padding: 0;
        line-height: 0;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      button svg {
        width: 16px;
        height: 16px;
      }

      button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      :host(.show-clear-button) input {
        padding-right: 20px;
      }

      :host(.show-clear-button) button {
        display: block;
      }
    `;
  }
  update(t) {
    super.update(t), t.has("showClearButton") && (this.showClearButton ? this.classList.add("show-clear-button") : this.classList.remove("show-clear-button"));
  }
  render() {
    return n`
      <input class="input" .value=${this.value} @change=${this.handleInputChange} />
      <button @click=${this.handleClearClick}>${Ie.cross}</button>
    `;
  }
  handleInputChange(t) {
    const e = t.target;
    this.dispatchEvent(new xe(e.value));
  }
  handleClearClick() {
    this.dispatchEvent(new xe(""));
  }
};
x([
  h({})
], B.prototype, "value", 2);
x([
  h({})
], B.prototype, "showClearButton", 2);
B = x([
  f("copilot-theme-text-input")
], B);
var st = Object.defineProperty, ot = Object.getOwnPropertyDescriptor, X = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? ot(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && st(e, s, o), o;
};
class it extends CustomEvent {
  constructor(e) {
    super("class-name-change", { detail: { value: e } });
  }
}
let V = class extends E {
  constructor() {
    super(), this.editedClassName = "", this.invalid = !1;
  }
  static get styles() {
    return [
      Ae,
      v`
        .editor-row {
          padding-top: 0;
        }

        .editor-row .editor .error {
          display: inline-block;
          color: var(--dev-tools-red-color);
          margin-top: 4px;
        }
      `
    ];
  }
  update(t) {
    super.update(t), t.has("className") && (this.editedClassName = this.className, this.invalid = !1);
  }
  render() {
    return n` <div class="editor-row local-class-name">
      <div class="label">CSS class name</div>
      <div class="editor">
        <copilot-theme-text-input
          type="text"
          .value=${this.editedClassName}
          @change=${this.handleInputChange}></copilot-theme-text-input>
        ${this.invalid ? n`<br /><span class="error">Please enter a valid CSS class name</span>` : null}
      </div>
    </div>`;
  }
  handleInputChange(t) {
    this.editedClassName = t.detail.value;
    const e = /^-?[_a-zA-Z]+[_a-zA-Z0-9-]*$/;
    this.invalid = !this.editedClassName.match(e), !this.invalid && this.editedClassName !== this.className && this.dispatchEvent(new it(this.editedClassName));
  }
};
X([
  h({})
], V.prototype, "className", 2);
X([
  y()
], V.prototype, "editedClassName", 2);
X([
  y()
], V.prototype, "invalid", 2);
V = X([
  f("copilot-theme-class-name-editor")
], V);
var rt = Object.defineProperty, at = Object.getOwnPropertyDescriptor, Y = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? at(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && rt(e, s, o), o;
};
class nt extends CustomEvent {
  constructor(e) {
    super("scope-change", { detail: { value: e } });
  }
}
Me(v`
  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] {
    --lumo-primary-color-50pct: rgba(255, 255, 255, 0.5);
    z-index: 100000 !important;
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector']::part(overlay) {
    background: #333;
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item {
    color: rgba(255, 255, 255, 0.8);
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item::part(content) {
    font-size: 13px;
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item .title {
    color: rgba(255, 255, 255, 0.95);
    font-weight: bold;
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item::part(checkmark) {
    margin: 6px;
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item::part(checkmark)::before {
    color: rgba(255, 255, 255, 0.95);
  }

  vaadin-select-overlay[theme~='copilot-theme-scope-selector'] vaadin-item:hover {
    background: rgba(255, 255, 255, 0.1);
  }
`);
let z = class extends E {
  constructor() {
    super(), this.value = u.local;
  }
  static get styles() {
    return v`
      vaadin-select {
        --lumo-primary-color-50pct: rgba(255, 255, 255, 0.5);
        width: 100px;
      }

      vaadin-select::part(input-field) {
        background: rgba(0, 0, 0, 0.2);
      }

      vaadin-select vaadin-select-value-button,
      vaadin-select::part(toggle-button) {
        color: var(--dev-tools-text-color);
      }

      vaadin-select:hover vaadin-select-value-button,
      vaadin-select:hover::part(toggle-button) {
        color: var(--dev-tools-text-color-emphasis);
      }

      vaadin-select vaadin-select-item {
        font-size: 13px;
      }
    `;
  }
  update(t) {
    var e;
    super.update(t), t.has("metadata") && ((e = this.select) == null || e.requestContentUpdate());
  }
  render() {
    return n` <vaadin-select
      theme="small copilot-theme-scope-selector"
      .value=${this.value}
      .renderer=${this.selectRenderer.bind(this)}
      @value-changed=${this.handleValueChange}></vaadin-select>`;
  }
  selectRenderer(t) {
    var i;
    const e = ((i = this.metadata) == null ? void 0 : i.displayName) || "Component", s = `${e}s`;
    Re(
      n`
        <vaadin-list-box>
          <vaadin-item value=${u.local} label="Local">
            <span class="title">Local</span>
            <br />
            <span>Edit styles for this ${e}</span>
          </vaadin-item>
          <vaadin-item value=${u.global} label="Global">
            <span class="title">Global</span>
            <br />
            <span>Edit styles for all ${s}</span>
          </vaadin-item>
        </vaadin-list-box>
      `,
      t
    );
  }
  handleValueChange(t) {
    const e = t.detail.value;
    e !== this.value && this.dispatchEvent(new nt(e));
  }
};
Y([
  h({})
], z.prototype, "value", 2);
Y([
  h({})
], z.prototype, "metadata", 2);
Y([
  Oe("vaadin-select")
], z.prototype, "select", 2);
z = Y([
  f("copilot-theme-scope-selector")
], z);
var lt = Object.defineProperty, ct = Object.getOwnPropertyDescriptor, dt = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? ct(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && lt(e, s, o), o;
};
let Ce = class extends g {
  static get styles() {
    return [
      g.styles,
      v`
        .editor-row {
          align-items: center;
        }
      `
    ];
  }
  handleInputChange(t) {
    const s = t.target.checked ? this.propertyMetadata.checkedValue : "";
    this.dispatchChange(s || "");
  }
  renderEditor() {
    const t = this.value === this.propertyMetadata.checkedValue;
    return n` <input type="checkbox" .checked=${t} @change=${this.handleInputChange} /> `;
  }
};
Ce = dt([
  f("copilot-theme-checkbox-property-editor")
], Ce);
var ht = Object.defineProperty, pt = Object.getOwnPropertyDescriptor, ut = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? pt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && ht(e, s, o), o;
};
let _e = class extends g {
  handleInputChange(t) {
    this.dispatchChange(t.detail.value);
  }
  renderEditor() {
    var t;
    return n`
      <copilot-theme-text-input
        .value=${this.value}
        .showClearButton=${((t = this.propertyValue) == null ? void 0 : t.modified) || !1}
        @change=${this.handleInputChange}></copilot-theme-text-input>
    `;
  }
};
_e = ut([
  f("copilot-theme-text-property-editor")
], _e);
/**
 * @license
 * Copyright 2018 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const mt = je(class extends qe {
  constructor(t) {
    var e;
    if (super(t), t.type !== He.ATTRIBUTE || t.name !== "class" || ((e = t.strings) == null ? void 0 : e.length) > 2)
      throw Error("`classMap()` can only be used in the `class` attribute and must be the only part in the attribute.");
  }
  render(t) {
    return " " + Object.keys(t).filter((e) => t[e]).join(" ") + " ";
  }
  update(t, [e]) {
    var i, o;
    if (this.it === void 0) {
      this.it = /* @__PURE__ */ new Set(), t.strings !== void 0 && (this.st = new Set(t.strings.join(" ").split(/\s/).filter((r) => r !== "")));
      for (const r in e)
        e[r] && !((i = this.st) != null && i.has(r)) && this.it.add(r);
      return this.render(e);
    }
    const s = t.element.classList;
    for (const r of this.it)
      r in e || (s.remove(r), this.it.delete(r));
    for (const r in e) {
      const a = !!e[r];
      a === this.it.has(r) || (o = this.st) != null && o.has(r) || (a ? (s.add(r), this.it.add(r)) : (s.remove(r), this.it.delete(r)));
    }
    return De;
  }
});
var vt = Object.defineProperty, gt = Object.getOwnPropertyDescriptor, ce = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? gt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && vt(e, s, o), o;
};
let F = class extends g {
  constructor() {
    super(), this.selectedPresetIndex = -1, this.presets = new U();
  }
  static get styles() {
    return [
      g.styles,
      v`
        :host {
          --preset-count: 3;
          --slider-bg: #fff;
          --slider-border: #333;
        }

        .editor-row {
          align-items: center;
        }

        .editor-row > .editor {
          display: flex;
          align-items: center;
          gap: 1rem;
        }

        .editor-row .input {
          flex: 0 0 auto;
          width: 80px;
        }

        .slider-wrapper {
          flex: 1 1 0;
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }

        .icon {
          width: 20px;
          height: 20px;
          color: #aaa;
        }

        .icon.prefix > svg {
          transform: scale(0.75);
        }

        .slider {
          flex: 1 1 0;
          -webkit-appearance: none;
          background: linear-gradient(to right, #666, #666 2px, transparent 2px);
          background-size: calc((100% - 13px) / (var(--preset-count) - 1)) 8px;
          background-position: 5px 50%;
          background-repeat: repeat-x;
        }

        .slider::-webkit-slider-runnable-track {
          width: 100%;
          box-sizing: border-box;
          height: 16px;
          background-image: linear-gradient(#666, #666);
          background-size: calc(100% - 12px) 2px;
          background-repeat: no-repeat;
          background-position: 6px 50%;
        }

        .slider::-moz-range-track {
          width: 100%;
          box-sizing: border-box;
          height: 16px;
          background-image: linear-gradient(#666, #666);
          background-size: calc(100% - 12px) 2px;
          background-repeat: no-repeat;
          background-position: 6px 50%;
        }

        .slider::-webkit-slider-thumb {
          -webkit-appearance: none;
          height: 16px;
          width: 16px;
          border: 2px solid var(--slider-border);
          border-radius: 50%;
          background: var(--slider-bg);
          cursor: pointer;
        }

        .slider::-moz-range-thumb {
          height: 16px;
          width: 16px;
          border: 2px solid var(--slider-border);
          border-radius: 50%;
          background: var(--slider-bg);
          cursor: pointer;
        }

        .custom-value {
          opacity: 0.5;
        }

        .custom-value:hover,
        .custom-value:focus-within {
          opacity: 1;
        }

        .custom-value:not(:hover, :focus-within) {
          --slider-bg: #333;
          --slider-border: #666;
        }
      `
    ];
  }
  update(t) {
    t.has("propertyMetadata") && (this.presets = new U(this.propertyMetadata)), super.update(t);
  }
  renderEditor() {
    var s;
    const t = {
      "slider-wrapper": !0,
      "custom-value": this.selectedPresetIndex < 0
    }, e = this.presets.values.length;
    return n`
      <div class=${mt(t)}>
        ${null}
        <input
          type="range"
          class="slider"
          style="--preset-count: ${e}"
          step="1"
          min="0"
          .max=${(e - 1).toString()}
          .value=${this.selectedPresetIndex}
          @input=${this.handleSliderInput}
          @change=${this.handleSliderChange} />
        ${null}
      </div>
      <copilot-theme-text-input
        class="input"
        .value=${this.value}
        .showClearButton=${((s = this.propertyValue) == null ? void 0 : s.modified) || !1}
        @change=${this.handleValueChange}></copilot-theme-text-input>
    `;
  }
  handleSliderInput(t) {
    const e = t.target, s = parseInt(e.value), i = this.presets.values[s];
    this.selectedPresetIndex = s, this.value = this.presets.rawValues[i];
  }
  handleSliderChange() {
    this.dispatchChange(this.value);
  }
  handleValueChange(t) {
    this.value = t.detail.value, this.updateSliderValue(), this.dispatchChange(this.value);
  }
  dispatchChange(t) {
    const e = this.presets.tryMapToPreset(t);
    super.dispatchChange(e);
  }
  updateValueFromTheme() {
    var t;
    super.updateValueFromTheme(), this.value = this.presets.tryMapToRawValue(((t = this.propertyValue) == null ? void 0 : t.value) || ""), this.updateSliderValue();
  }
  updateSliderValue() {
    const t = this.presets.findPreset(this.value);
    this.selectedPresetIndex = t ? this.presets.values.indexOf(t) : -1;
  }
};
ce([
  y()
], F.prototype, "selectedPresetIndex", 2);
ce([
  y()
], F.prototype, "presets", 2);
F = ce([
  f("copilot-theme-range-property-editor")
], F);
const A = (t, e = 0, s = 1) => t > s ? s : t < e ? e : t, m = (t, e = 0, s = Math.pow(10, e)) => Math.round(s * t) / s, Ve = ({ h: t, s: e, v: s, a: i }) => {
  const o = (200 - e) * s / 100;
  return {
    h: m(t),
    s: m(o > 0 && o < 200 ? e * s / 100 / (o <= 100 ? o : 200 - o) * 100 : 0),
    l: m(o / 2),
    a: m(i, 2)
  };
}, ae = (t) => {
  const { h: e, s, l: i } = Ve(t);
  return `hsl(${e}, ${s}%, ${i}%)`;
}, te = (t) => {
  const { h: e, s, l: i, a: o } = Ve(t);
  return `hsla(${e}, ${s}%, ${i}%, ${o})`;
}, ft = ({ h: t, s: e, v: s, a: i }) => {
  t = t / 360 * 6, e = e / 100, s = s / 100;
  const o = Math.floor(t), r = s * (1 - e), a = s * (1 - (t - o) * e), l = s * (1 - (1 - t + o) * e), c = o % 6;
  return {
    r: m([s, a, r, r, l, s][c] * 255),
    g: m([l, s, s, a, r, r][c] * 255),
    b: m([r, r, l, s, s, a][c] * 255),
    a: m(i, 2)
  };
}, yt = (t) => {
  const { r: e, g: s, b: i, a: o } = ft(t);
  return `rgba(${e}, ${s}, ${i}, ${o})`;
}, bt = (t) => {
  const s = /rgba?\(?\s*(-?\d*\.?\d+)(%)?[,\s]+(-?\d*\.?\d+)(%)?[,\s]+(-?\d*\.?\d+)(%)?,?\s*[/\s]*(-?\d*\.?\d+)?(%)?\s*\)?/i.exec(t);
  return s ? wt({
    r: Number(s[1]) / (s[2] ? 100 / 255 : 1),
    g: Number(s[3]) / (s[4] ? 100 / 255 : 1),
    b: Number(s[5]) / (s[6] ? 100 / 255 : 1),
    a: s[7] === void 0 ? 1 : Number(s[7]) / (s[8] ? 100 : 1)
  }) : { h: 0, s: 0, v: 0, a: 1 };
}, wt = ({ r: t, g: e, b: s, a: i }) => {
  const o = Math.max(t, e, s), r = o - Math.min(t, e, s), a = r ? o === t ? (e - s) / r : o === e ? 2 + (s - t) / r : 4 + (t - e) / r : 0;
  return {
    h: m(60 * (a < 0 ? a + 6 : a)),
    s: m(o ? r / o * 100 : 0),
    v: m(o / 255 * 100),
    a: i
  };
}, xt = (t, e) => {
  if (t === e)
    return !0;
  for (const s in t)
    if (t[s] !== e[s])
      return !1;
  return !0;
}, Ct = (t, e) => t.replace(/\s/g, "") === e.replace(/\s/g, ""), $e = {}, ze = (t) => {
  let e = $e[t];
  return e || (e = document.createElement("template"), e.innerHTML = t, $e[t] = e), e;
}, de = (t, e, s) => {
  t.dispatchEvent(new CustomEvent(e, {
    bubbles: !0,
    detail: s
  }));
};
let R = !1;
const ne = (t) => "touches" in t, _t = (t) => R && !ne(t) ? !1 : (R || (R = ne(t)), !0), ke = (t, e) => {
  const s = ne(e) ? e.touches[0] : e, i = t.el.getBoundingClientRect();
  de(t.el, "move", t.getMove({
    x: A((s.pageX - (i.left + window.pageXOffset)) / i.width),
    y: A((s.pageY - (i.top + window.pageYOffset)) / i.height)
  }));
}, $t = (t, e) => {
  const s = e.keyCode;
  s > 40 || t.xy && s < 37 || s < 33 || (e.preventDefault(), de(t.el, "move", t.getMove({
    x: s === 39 ? 0.01 : s === 37 ? -0.01 : s === 34 ? 0.05 : s === 33 ? -0.05 : s === 35 ? 1 : s === 36 ? -1 : 0,
    y: s === 40 ? 0.01 : s === 38 ? -0.01 : 0
  }, !0)));
};
class he {
  constructor(e, s, i, o) {
    const r = ze(`<div role="slider" tabindex="0" part="${s}" ${i}><div part="${s}-pointer"></div></div>`);
    e.appendChild(r.content.cloneNode(!0));
    const a = e.querySelector(`[part=${s}]`);
    a.addEventListener("mousedown", this), a.addEventListener("touchstart", this), a.addEventListener("keydown", this), this.el = a, this.xy = o, this.nodes = [a.firstChild, a];
  }
  set dragging(e) {
    const s = e ? document.addEventListener : document.removeEventListener;
    s(R ? "touchmove" : "mousemove", this), s(R ? "touchend" : "mouseup", this);
  }
  handleEvent(e) {
    switch (e.type) {
      case "mousedown":
      case "touchstart":
        if (e.preventDefault(), !_t(e) || !R && e.button != 0)
          return;
        this.el.focus(), ke(this, e), this.dragging = !0;
        break;
      case "mousemove":
      case "touchmove":
        e.preventDefault(), ke(this, e);
        break;
      case "mouseup":
      case "touchend":
        this.dragging = !1;
        break;
      case "keydown":
        $t(this, e);
        break;
    }
  }
  style(e) {
    e.forEach((s, i) => {
      for (const o in s)
        this.nodes[i].style.setProperty(o, s[o]);
    });
  }
}
class kt extends he {
  constructor(e) {
    super(e, "hue", 'aria-label="Hue" aria-valuemin="0" aria-valuemax="360"', !1);
  }
  update({ h: e }) {
    this.h = e, this.style([
      {
        left: `${e / 360 * 100}%`,
        color: ae({ h: e, s: 100, v: 100, a: 1 })
      }
    ]), this.el.setAttribute("aria-valuenow", `${m(e)}`);
  }
  getMove(e, s) {
    return { h: s ? A(this.h + e.x * 360, 0, 360) : 360 * e.x };
  }
}
class St extends he {
  constructor(e) {
    super(e, "saturation", 'aria-label="Color"', !0);
  }
  update(e) {
    this.hsva = e, this.style([
      {
        top: `${100 - e.v}%`,
        left: `${e.s}%`,
        color: ae(e)
      },
      {
        "background-color": ae({ h: e.h, s: 100, v: 100, a: 1 })
      }
    ]), this.el.setAttribute("aria-valuetext", `Saturation ${m(e.s)}%, Brightness ${m(e.v)}%`);
  }
  getMove(e, s) {
    return {
      s: s ? A(this.hsva.s + e.x * 100, 0, 100) : e.x * 100,
      v: s ? A(this.hsva.v - e.y * 100, 0, 100) : Math.round(100 - e.y * 100)
    };
  }
}
const Et = ':host{display:flex;flex-direction:column;position:relative;width:200px;height:200px;user-select:none;-webkit-user-select:none;cursor:default}:host([hidden]){display:none!important}[role=slider]{position:relative;touch-action:none;user-select:none;-webkit-user-select:none;outline:0}[role=slider]:last-child{border-radius:0 0 8px 8px}[part$=pointer]{position:absolute;z-index:1;box-sizing:border-box;width:28px;height:28px;display:flex;place-content:center center;transform:translate(-50%,-50%);background-color:#fff;border:2px solid #fff;border-radius:50%;box-shadow:0 2px 4px rgba(0,0,0,.2)}[part$=pointer]::after{content:"";width:100%;height:100%;border-radius:inherit;background-color:currentColor}[role=slider]:focus [part$=pointer]{transform:translate(-50%,-50%) scale(1.1)}', Pt = "[part=hue]{flex:0 0 24px;background:linear-gradient(to right,red 0,#ff0 17%,#0f0 33%,#0ff 50%,#00f 67%,#f0f 83%,red 100%)}[part=hue-pointer]{top:50%;z-index:2}", Nt = "[part=saturation]{flex-grow:1;border-color:transparent;border-bottom:12px solid #000;border-radius:8px 8px 0 0;background-image:linear-gradient(to top,#000,transparent),linear-gradient(to right,#fff,rgba(255,255,255,0));box-shadow:inset 0 0 0 1px rgba(0,0,0,.05)}[part=saturation-pointer]{z-index:3}", q = Symbol("same"), se = Symbol("color"), Se = Symbol("hsva"), oe = Symbol("update"), Ee = Symbol("parts"), W = Symbol("css"), G = Symbol("sliders");
let Tt = class extends HTMLElement {
  static get observedAttributes() {
    return ["color"];
  }
  get [W]() {
    return [Et, Pt, Nt];
  }
  get [G]() {
    return [St, kt];
  }
  get color() {
    return this[se];
  }
  set color(e) {
    if (!this[q](e)) {
      const s = this.colorModel.toHsva(e);
      this[oe](s), this[se] = e;
    }
  }
  constructor() {
    super();
    const e = ze(`<style>${this[W].join("")}</style>`), s = this.attachShadow({ mode: "open" });
    s.appendChild(e.content.cloneNode(!0)), s.addEventListener("move", this), this[Ee] = this[G].map((i) => new i(s));
  }
  connectedCallback() {
    if (this.hasOwnProperty("color")) {
      const e = this.color;
      delete this.color, this.color = e;
    } else
      this.color || (this.color = this.colorModel.defaultColor);
  }
  attributeChangedCallback(e, s, i) {
    const o = this.colorModel.fromAttr(i);
    this[q](o) || (this.color = o);
  }
  handleEvent(e) {
    const s = this[Se], i = { ...s, ...e.detail };
    this[oe](i);
    let o;
    !xt(i, s) && !this[q](o = this.colorModel.fromHsva(i)) && (this[se] = o, de(this, "color-changed", { value: o }));
  }
  [q](e) {
    return this.color && this.colorModel.equal(e, this.color);
  }
  [oe](e) {
    this[Se] = e, this[Ee].forEach((s) => s.update(e));
  }
};
class Ot extends he {
  constructor(e) {
    super(e, "alpha", 'aria-label="Alpha" aria-valuemin="0" aria-valuemax="1"', !1);
  }
  update(e) {
    this.hsva = e;
    const s = te({ ...e, a: 0 }), i = te({ ...e, a: 1 }), o = e.a * 100;
    this.style([
      {
        left: `${o}%`,
        color: te(e)
      },
      {
        "--gradient": `linear-gradient(90deg, ${s}, ${i}`
      }
    ]);
    const r = m(o);
    this.el.setAttribute("aria-valuenow", `${r}`), this.el.setAttribute("aria-valuetext", `${r}%`);
  }
  getMove(e, s) {
    return { a: s ? A(this.hsva.a + e.x) : e.x };
  }
}
const Rt = `[part=alpha]{flex:0 0 24px}[part=alpha]::after{display:block;content:"";position:absolute;top:0;left:0;right:0;bottom:0;border-radius:inherit;background-image:var(--gradient);box-shadow:inset 0 0 0 1px rgba(0,0,0,.05)}[part^=alpha]{background-color:#fff;background-image:url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill-opacity=".05"><rect x="8" width="8" height="8"/><rect y="8" width="8" height="8"/></svg>')}[part=alpha-pointer]{top:50%}`;
class Mt extends Tt {
  get [W]() {
    return [...super[W], Rt];
  }
  get [G]() {
    return [...super[G], Ot];
  }
}
const At = {
  defaultColor: "rgba(0, 0, 0, 1)",
  toHsva: bt,
  fromHsva: yt,
  equal: Ct,
  fromAttr: (t) => t
};
class Vt extends Mt {
  get colorModel() {
    return At;
  }
}
/**
 * @license
 * Copyright (c) 2017 - 2023 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
function zt(t) {
  const e = [];
  for (; t; ) {
    if (t.nodeType === Node.DOCUMENT_NODE) {
      e.push(t);
      break;
    }
    if (t.nodeType === Node.DOCUMENT_FRAGMENT_NODE) {
      e.push(t), t = t.host;
      continue;
    }
    if (t.assignedSlot) {
      t = t.assignedSlot;
      continue;
    }
    t = t.parentNode;
  }
  return e;
}
const ie = {
  start: "top",
  end: "bottom"
}, re = {
  start: "left",
  end: "right"
}, Pe = new ResizeObserver((t) => {
  setTimeout(() => {
    t.forEach((e) => {
      e.target.__overlay && e.target.__overlay._updatePosition();
    });
  });
}), Lt = (t) => class extends t {
  static get properties() {
    return {
      /**
       * The element next to which this overlay should be aligned.
       * The position of the overlay relative to the positionTarget can be adjusted
       * with properties `horizontalAlign`, `verticalAlign`, `noHorizontalOverlap`
       * and `noVerticalOverlap`.
       */
      positionTarget: {
        type: Object,
        value: null
      },
      /**
       * When `positionTarget` is set, this property defines whether to align the overlay's
       * left or right side to the target element by default.
       * Possible values are `start` and `end`.
       * RTL is taken into account when interpreting the value.
       * The overlay is automatically flipped to the opposite side when it doesn't fit into
       * the default side defined by this property.
       *
       * @attr {start|end} horizontal-align
       */
      horizontalAlign: {
        type: String,
        value: "start"
      },
      /**
       * When `positionTarget` is set, this property defines whether to align the overlay's
       * top or bottom side to the target element by default.
       * Possible values are `top` and `bottom`.
       * The overlay is automatically flipped to the opposite side when it doesn't fit into
       * the default side defined by this property.
       *
       * @attr {top|bottom} vertical-align
       */
      verticalAlign: {
        type: String,
        value: "top"
      },
      /**
       * When `positionTarget` is set, this property defines whether the overlay should overlap
       * the target element in the x-axis, or be positioned right next to it.
       *
       * @attr {boolean} no-horizontal-overlap
       */
      noHorizontalOverlap: {
        type: Boolean,
        value: !1
      },
      /**
       * When `positionTarget` is set, this property defines whether the overlay should overlap
       * the target element in the y-axis, or be positioned right above/below it.
       *
       * @attr {boolean} no-vertical-overlap
       */
      noVerticalOverlap: {
        type: Boolean,
        value: !1
      },
      /**
       * If the overlay content has no intrinsic height, this property can be used to set
       * the minimum vertical space (in pixels) required by the overlay. Setting a value to
       * the property effectively disables the content measurement in favor of using this
       * fixed value for determining the open direction.
       *
       * @attr {number} required-vertical-space
       */
      requiredVerticalSpace: {
        type: Number,
        value: 0
      }
    };
  }
  static get observers() {
    return [
      "__positionSettingsChanged(horizontalAlign, verticalAlign, noHorizontalOverlap, noVerticalOverlap, requiredVerticalSpace)",
      "__overlayOpenedChanged(opened, positionTarget)"
    ];
  }
  constructor() {
    super(), this.__onScroll = this.__onScroll.bind(this), this._updatePosition = this._updatePosition.bind(this);
  }
  /** @protected */
  connectedCallback() {
    super.connectedCallback(), this.opened && this.__addUpdatePositionEventListeners();
  }
  /** @protected */
  disconnectedCallback() {
    super.disconnectedCallback(), this.__removeUpdatePositionEventListeners();
  }
  /** @private */
  __addUpdatePositionEventListeners() {
    window.addEventListener("resize", this._updatePosition), this.__positionTargetAncestorRootNodes = zt(this.positionTarget), this.__positionTargetAncestorRootNodes.forEach((s) => {
      s.addEventListener("scroll", this.__onScroll, !0);
    });
  }
  /** @private */
  __removeUpdatePositionEventListeners() {
    window.removeEventListener("resize", this._updatePosition), this.__positionTargetAncestorRootNodes && (this.__positionTargetAncestorRootNodes.forEach((s) => {
      s.removeEventListener("scroll", this.__onScroll, !0);
    }), this.__positionTargetAncestorRootNodes = null);
  }
  /** @private */
  __overlayOpenedChanged(s, i) {
    if (this.__removeUpdatePositionEventListeners(), i && (i.__overlay = null, Pe.unobserve(i), s && (this.__addUpdatePositionEventListeners(), i.__overlay = this, Pe.observe(i))), s) {
      const o = getComputedStyle(this);
      this.__margins || (this.__margins = {}, ["top", "bottom", "left", "right"].forEach((r) => {
        this.__margins[r] = parseInt(o[r], 10);
      })), this.setAttribute("dir", o.direction), this._updatePosition(), requestAnimationFrame(() => this._updatePosition());
    }
  }
  __positionSettingsChanged() {
    this._updatePosition();
  }
  /** @private */
  __onScroll(s) {
    this.contains(s.target) || this._updatePosition();
  }
  _updatePosition() {
    if (!this.positionTarget || !this.opened)
      return;
    const s = this.positionTarget.getBoundingClientRect(), i = this.__shouldAlignStartVertically(s);
    this.style.justifyContent = i ? "flex-start" : "flex-end";
    const o = this.__isRTL, r = this.__shouldAlignStartHorizontally(s, o), a = !o && r || o && !r;
    this.style.alignItems = a ? "flex-start" : "flex-end";
    const l = this.getBoundingClientRect(), c = this.__calculatePositionInOneDimension(
      s,
      l,
      this.noVerticalOverlap,
      ie,
      this,
      i
    ), d = this.__calculatePositionInOneDimension(
      s,
      l,
      this.noHorizontalOverlap,
      re,
      this,
      r
    );
    Object.assign(this.style, c, d), this.toggleAttribute("bottom-aligned", !i), this.toggleAttribute("top-aligned", i), this.toggleAttribute("end-aligned", !a), this.toggleAttribute("start-aligned", a);
  }
  __shouldAlignStartHorizontally(s, i) {
    const o = Math.max(this.__oldContentWidth || 0, this.$.overlay.offsetWidth);
    this.__oldContentWidth = this.$.overlay.offsetWidth;
    const r = Math.min(window.innerWidth, document.documentElement.clientWidth), a = !i && this.horizontalAlign === "start" || i && this.horizontalAlign === "end";
    return this.__shouldAlignStart(
      s,
      o,
      r,
      this.__margins,
      a,
      this.noHorizontalOverlap,
      re
    );
  }
  __shouldAlignStartVertically(s) {
    const i = this.requiredVerticalSpace || Math.max(this.__oldContentHeight || 0, this.$.overlay.offsetHeight);
    this.__oldContentHeight = this.$.overlay.offsetHeight;
    const o = Math.min(window.innerHeight, document.documentElement.clientHeight), r = this.verticalAlign === "top";
    return this.__shouldAlignStart(
      s,
      i,
      o,
      this.__margins,
      r,
      this.noVerticalOverlap,
      ie
    );
  }
  // eslint-disable-next-line max-params
  __shouldAlignStart(s, i, o, r, a, l, c) {
    const d = o - s[l ? c.end : c.start] - r[c.end], C = s[l ? c.start : c.end] - r[c.start], _ = a ? d : C, k = _ > (a ? C : d) || _ > i;
    return a === k;
  }
  /**
   * Returns an adjusted value after resizing the browser window,
   * to avoid wrong calculations when e.g. previously set `bottom`
   * CSS property value is larger than the updated viewport height.
   * See https://github.com/vaadin/web-components/issues/4604
   */
  __adjustBottomProperty(s, i, o) {
    let r;
    if (s === i.end) {
      if (i.end === ie.end) {
        const a = Math.min(window.innerHeight, document.documentElement.clientHeight);
        if (o > a && this.__oldViewportHeight) {
          const l = this.__oldViewportHeight - a;
          r = o - l;
        }
        this.__oldViewportHeight = a;
      }
      if (i.end === re.end) {
        const a = Math.min(window.innerWidth, document.documentElement.clientWidth);
        if (o > a && this.__oldViewportWidth) {
          const l = this.__oldViewportWidth - a;
          r = o - l;
        }
        this.__oldViewportWidth = a;
      }
    }
    return r;
  }
  /**
   * Returns an object with CSS position properties to set,
   * e.g. { top: "100px" }
   */
  // eslint-disable-next-line max-params
  __calculatePositionInOneDimension(s, i, o, r, a, l) {
    const c = l ? r.start : r.end, d = l ? r.end : r.start, C = parseFloat(a.style[c] || getComputedStyle(a)[c]), _ = this.__adjustBottomProperty(c, r, C), $ = i[l ? r.start : r.end] - s[o === l ? r.end : r.start], k = _ ? `${_}px` : `${C + $ * (l ? -1 : 1)}px`;
    return {
      [c]: k,
      [d]: ""
    };
  }
};
var It = Object.defineProperty, jt = Object.getOwnPropertyDescriptor, P = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? jt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && It(e, s, o), o;
};
class qt extends CustomEvent {
  constructor(e) {
    super("color-picker-change", { detail: { value: e } });
  }
}
const Le = v`
  :host {
    --preview-size: 24px;
    --preview-color: rgba(0, 0, 0, 0);
  }

  .preview {
    --preview-bg-size: calc(var(--preview-size) / 2);
    --preview-bg-pos: calc(var(--preview-size) / 4);

    width: var(--preview-size);
    height: var(--preview-size);
    padding: 0;
    position: relative;
    overflow: hidden;
    background: none;
    border: solid 2px #888;
    border-radius: 4px;
    box-sizing: content-box;
  }

  .preview::before,
  .preview::after {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
  }

  .preview::before {
    content: '';
    background: white;
    background-image: linear-gradient(45deg, #666 25%, transparent 25%),
      linear-gradient(45deg, transparent 75%, #666 75%), linear-gradient(45deg, transparent 75%, #666 75%),
      linear-gradient(45deg, #666 25%, transparent 25%);
    background-size: var(--preview-bg-size) var(--preview-bg-size);
    background-position: 0 0, 0 0, calc(var(--preview-bg-pos) * -1) calc(var(--preview-bg-pos) * -1),
      var(--preview-bg-pos) var(--preview-bg-pos);
  }

  .preview::after {
    content: '';
    background-color: var(--preview-color);
  }
`;
let L = class extends E {
  constructor() {
    super(...arguments), this.commitValue = !1;
  }
  static get styles() {
    return [
      Le,
      v`
        #toggle {
          display: block;
        }
      `
    ];
  }
  update(t) {
    super.update(t), t.has("value") && this.overlay && this.overlay.requestContentUpdate();
  }
  firstUpdated() {
    this.overlay = document.createElement("copilot-color-picker-overlay"), this.overlay.renderer = this.renderOverlayContent.bind(this), this.overlay.owner = this, this.overlay.positionTarget = this.toggle, this.overlay.noVerticalOverlap = !0, this.overlay.addEventListener("vaadin-overlay-escape-press", this.handleOverlayEscape.bind(this)), this.overlay.addEventListener("vaadin-overlay-close", this.handleOverlayClose.bind(this)), this.append(this.overlay);
  }
  render() {
    const t = this.value || "rgba(0, 0, 0, 0)";
    return n` <button
      id="toggle"
      class="preview"
      style="--preview-color: ${t}"
      @click=${this.open}></button>`;
  }
  open() {
    this.commitValue = !1, this.overlay.opened = !0, this.overlay.style.zIndex = "1000000";
    const t = this.overlay.shadowRoot.querySelector('[part="overlay"]');
    t.style.background = "#333";
  }
  renderOverlayContent(t) {
    const s = getComputedStyle(this.toggle, "::after").getPropertyValue("background-color");
    Re(
      n` <div>
        <copilot-color-picker-overlay-content
          .value=${s}
          .presets=${this.presets}
          @color-changed=${this.handleColorChange.bind(this)}></copilot-color-picker-overlay-content>
      </div>`,
      t
    );
  }
  handleColorChange(t) {
    this.commitValue = !0, this.dispatchEvent(new qt(t.detail.value)), t.detail.close && (this.overlay.opened = !1, this.handleOverlayClose());
  }
  handleOverlayEscape() {
    this.commitValue = !1;
  }
  handleOverlayClose() {
    const t = this.commitValue ? "color-picker-commit" : "color-picker-cancel";
    this.dispatchEvent(new CustomEvent(t));
  }
};
P([
  h({})
], L.prototype, "value", 2);
P([
  h({})
], L.prototype, "presets", 2);
P([
  Oe("#toggle")
], L.prototype, "toggle", 2);
L = P([
  f("copilot-color-picker")
], L);
let K = class extends E {
  static get styles() {
    return [
      Le,
      v`
        :host {
          display: block;
          padding: 12px;
        }

        .picker::part(saturation),
        .picker::part(hue) {
          margin-bottom: 10px;
        }

        .picker::part(hue),
        .picker::part(alpha) {
          flex: 0 0 20px;
        }

        .picker::part(saturation),
        .picker::part(hue),
        .picker::part(alpha) {
          border-radius: 3px;
        }

        .picker::part(saturation-pointer),
        .picker::part(hue-pointer),
        .picker::part(alpha-pointer) {
          width: 20px;
          height: 20px;
        }

        .swatches {
          display: grid;
          grid-template-columns: repeat(6, var(--preview-size));
          grid-column-gap: 10px;
          grid-row-gap: 6px;
          margin-top: 16px;
        }
      `
    ];
  }
  render() {
    return n` <div>
      <copilot-rgba-string-color-picker
        class="picker"
        .color=${this.value}
        @color-changed=${this.handlePickerChange}></copilot-rgba-string-color-picker>
      ${this.renderSwatches()}
    </div>`;
  }
  renderSwatches() {
    if (!this.presets || this.presets.length === 0)
      return;
    const t = this.presets.map((e) => n` <button
        class="preview"
        style="--preview-color: ${e}"
        @click=${() => this.selectPreset(e)}></button>`);
    return n` <div class="swatches">${t}</div>`;
  }
  handlePickerChange(t) {
    this.dispatchEvent(new CustomEvent("color-changed", { detail: { value: t.detail.value } }));
  }
  selectPreset(t) {
    this.dispatchEvent(new CustomEvent("color-changed", { detail: { value: t, close: !0 } }));
  }
};
P([
  h({})
], K.prototype, "value", 2);
P([
  h({})
], K.prototype, "presets", 2);
K = P([
  f("copilot-color-picker-overlay-content")
], K);
customElements.whenDefined("vaadin-overlay").then(() => {
  const t = customElements.get("vaadin-overlay");
  class e extends Lt(t) {
  }
  customElements.define("copilot-color-picker-overlay", e);
});
customElements.define("copilot-rgba-string-color-picker", Vt);
var Ht = Object.defineProperty, Dt = Object.getOwnPropertyDescriptor, Ut = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? Dt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && Ht(e, s, o), o;
};
let Ne = class extends g {
  constructor() {
    super(...arguments), this.presets = new U();
  }
  static get styles() {
    return [
      g.styles,
      v`
        .editor-row {
          align-items: center;
        }

        .editor-row > .editor {
          display: flex;
          align-items: center;
          gap: 0.5rem;
        }
      `
    ];
  }
  update(t) {
    t.has("propertyMetadata") && (this.presets = new U(this.propertyMetadata)), super.update(t);
  }
  renderEditor() {
    var t;
    return n`
      <copilot-color-picker
        .value=${this.value}
        .presets=${this.presets.values}
        @color-picker-change=${this.handleColorPickerChange}
        @color-picker-commit=${this.handleColorPickerCommit}
        @color-picker-cancel=${this.handleColorPickerCancel}></copilot-color-picker>
      <copilot-theme-text-input
        .value=${this.value}
        .showClearButton=${((t = this.propertyValue) == null ? void 0 : t.modified) || !1}
        @change=${this.handleInputChange}></copilot-theme-text-input>
    `;
  }
  handleInputChange(t) {
    this.value = t.detail.value, this.dispatchChange(this.value);
  }
  handleColorPickerChange(t) {
    this.value = t.detail.value;
  }
  handleColorPickerCommit() {
    this.dispatchChange(this.value);
  }
  handleColorPickerCancel() {
    this.updateValueFromTheme();
  }
  dispatchChange(t) {
    const e = this.presets.tryMapToPreset(t);
    super.dispatchChange(e);
  }
  updateValueFromTheme() {
    var t;
    super.updateValueFromTheme(), this.value = this.presets.tryMapToRawValue(((t = this.propertyValue) == null ? void 0 : t.value) || "");
  }
};
Ne = Ut([
  f("copilot-theme-color-property-editor")
], Ne);
var Bt = Object.defineProperty, Ft = Object.getOwnPropertyDescriptor, pe = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? Ft(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && Bt(e, s, o), o;
};
class Wt extends CustomEvent {
  constructor(e) {
    super("open-css", { detail: { element: e } });
  }
}
let Z = class extends E {
  static get styles() {
    return v`
      .section .header {
        display: flex;
        align-items: baseline;
        justify-content: space-between;
        padding: 0.4rem var(--theme-editor-section-horizontal-padding);
        color: var(--dev-tools-text-color-emphasis);
        background-color: rgba(0, 0, 0, 0.2);
      }

      .section .property-list .property-editor:not(:last-child) {
        border-bottom: solid 1px rgba(0, 0, 0, 0.2);
      }

      .section .header .open-css {
        all: initial;
        font-family: inherit;
        font-size: var(--dev-tools-font-size-small);
        line-height: 1;
        white-space: nowrap;
        background-color: rgba(255, 255, 255, 0.12);
        color: var(--dev-tools-text-color);
        font-weight: 600;
        padding: 0.25rem 0.375rem;
        border-radius: 0.25rem;
      }

      .section .header .open-css:hover {
        color: var(--dev-tools-text-color-emphasis);
      }
    `;
  }
  render() {
    const t = this.metadata.elements.map((e) => this.renderSection(e));
    return n` <div>${t}</div> `;
  }
  renderSection(t) {
    const e = t.properties.map((s) => this.renderPropertyEditor(t, s));
    return n`
      <div class="section" data-testid=${t == null ? void 0 : t.displayName}>
        <div class="header">
          <span> ${t.displayName} </span>
          <button class="open-css" @click=${() => this.handleOpenCss(t)}>Edit CSS</button>
        </div>
        <div class="property-list">${e}</div>
      </div>
    `;
  }
  handleOpenCss(t) {
    this.dispatchEvent(new Wt(t));
  }
  renderPropertyEditor(t, e) {
    let s;
    switch (e.editorType) {
      case Q.checkbox:
        s = I`copilot-theme-checkbox-property-editor`;
        break;
      case Q.range:
        s = I`copilot-theme-range-property-editor`;
        break;
      case Q.color:
        s = I`copilot-theme-color-property-editor`;
        break;
      default:
        s = I`copilot-theme-text-property-editor`;
    }
    return Ue` <${s}
          class="property-editor"
          .elementMetadata=${t}
          .propertyMetadata=${e}
          .theme=${this.theme}
          data-testid=${e.propertyName}
        >
        </${s}>`;
  }
};
pe([
  h({})
], Z.prototype, "metadata", 2);
pe([
  h({})
], Z.prototype, "theme", 2);
Z = pe([
  f("copilot-theme-property-list")
], Z);
const O = () => window.Vaadin.copilotPlugins._internals;
var Gt = Object.defineProperty, Kt = Object.getOwnPropertyDescriptor, N = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? Kt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && Gt(e, s, o), o;
};
let w = class extends Be {
  constructor() {
    super(), this.baseTheme = null, this.editedTheme = null, this.expanded = !1, this.themeEditorState = D.enabled, this.effectiveTheme = null;
  }
  static get styles() {
    return v`
      :host {
        animation: fade-in var(--dev-tools-transition-duration) ease-in;
        --theme-editor-section-horizontal-padding: 0.75rem;
        display: flex;
        flex-direction: column;
        max-height: 400px;
      }

      .notice {
        padding: var(--theme-editor-section-horizontal-padding);
      }

      .notice a {
        color: var(--dev-tools-text-color-emphasis);
      }

      .hint vaadin-icon {
        color: var(--dev-tools-green-color);
        font-size: var(--lumo-icon-size-m);
      }

      .hint {
        display: flex;
        align-items: center;
        gap: var(--theme-editor-section-horizontal-padding);
      }

      .header {
        flex: 0 0 auto;
        border-bottom: solid 1px rgba(0, 0, 0, 0.2);
      }

      .header .picker-row {
        padding: var(--theme-editor-section-horizontal-padding);
        display: flex;
        gap: 20px;
        align-items: center;
        justify-content: space-between;
      }

      .picker {
        flex: 1 1 0;
        min-width: 0;
        display: flex;
        align-items: center;
      }

      .picker button {
        min-width: 0;
        display: inline-flex;
        align-items: center;
        padding: 0;
        line-height: 20px;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      .picker button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      .picker svg,
      .picker .component-type {
        flex: 0 0 auto;
        margin-right: 4px;
      }

      .picker .instance-name {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: #e5a2fce5;
      }

      .picker .instance-name-quote {
        color: #e5a2fce5;
      }

      .picker .no-selection {
        font-style: italic;
      }

      .actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .property-list {
        flex: 1 1 auto;
        overflow-y: auto;
      }

      .link-button {
        all: initial;
        font-family: inherit;
        font-size: var(--dev-tools-font-size-small);
        line-height: 1;
        white-space: nowrap;
        color: inherit;
        font-weight: 600;
        text-decoration: underline;
      }

      .link-button:focus,
      .link-button:hover {
        color: var(--dev-tools-text-color-emphasis);
      }

      .icon-button {
        padding: 0;
        line-height: 0;
        border: none;
        background: none;
        color: var(--dev-tools-text-color);
      }

      .icon-button:disabled {
        opacity: 0.5;
      }

      .icon-button:not(:disabled):hover {
        color: var(--dev-tools-text-color-emphasis);
      }
    `;
  }
  connectedCallback() {
    super.connectedCallback(), this.reaction(
      () => O().copilotUiState.getSelections,
      () => {
        this.refreshPicked(), this.requestUpdate();
      }
    ), this.refreshPicked();
  }
  async refreshPicked() {
    var i;
    if (O().copilotUiState.getSelections.length !== 1) {
      this.context = null;
      return;
    }
    const t = O().copilotUiState.getSelections[0], e = O().getFlowComponent(t.element), s = t.metadata;
    if (!s) {
      this.context = { component: e, scope: ((i = this.context) == null ? void 0 : i.scope) || u.local }, this.baseTheme = null, this.editedTheme = null, this.effectiveTheme = null;
      return;
    }
    await this.refreshComponentAndTheme(e, s);
  }
  firstUpdated() {
    this.history = new Ye(this.api), this.historyActions = this.history.allowedActions, this.undoRedoListener = (t) => {
      var s, i;
      const e = t.key === "Z" || t.key === "z";
      e && (t.ctrlKey || t.metaKey) && t.shiftKey ? (s = this.historyActions) != null && s.allowRedo && this.handleRedo() : e && (t.ctrlKey || t.metaKey) && (i = this.historyActions) != null && i.allowUndo && this.handleUndo();
    }, document.addEventListener("vaadin-theme-updated", () => {
      S.clear(), this.refreshTheme();
    }), document.addEventListener("keydown", this.undoRedoListener), this.dispatchEvent(new CustomEvent("before-open"));
  }
  update(t) {
    super.update(t);
  }
  disconnectedCallback() {
    super.disconnectedCallback(), document.removeEventListener("keydown", this.undoRedoListener), this.dispatchEvent(new CustomEvent("after-close"));
  }
  render() {
    var t, e, s;
    return this.themeEditorState === D.missing_theme ? this.renderMissingThemeNotice() : n`
      <div class="header">
        <div class="picker-row">
          ${this.renderPicker()}
          <div class="actions">
            ${(t = this.context) != null && t.metadata ? n` <copilot-theme-scope-selector
                  .value=${this.context.scope}
                  .metadata=${this.context.metadata}
                  @scope-change=${this.handleScopeChange}></copilot-theme-scope-selector>` : null}
            <button
              class="icon-button"
              data-testid="undo"
              ?disabled=${!((e = this.historyActions) != null && e.allowUndo)}
              @click=${this.handleUndo}>
              ${me.undo}
            </button>
            <button
              class="icon-button"
              data-testid="redo"
              ?disabled=${!((s = this.historyActions) != null && s.allowRedo)}
              @click=${this.handleRedo}>
              ${me.redo}
            </button>
          </div>
        </div>
        ${this.renderLocalClassNameEditor()}
      </div>
      ${this.renderPropertyList()}
    `;
  }
  renderMissingThemeNotice() {
    return n`
      <div class="notice">
        It looks like you have not set up an application theme yet. Theme editor requires an existing theme to work
        with. Please check our
        <a href="https://vaadin.com/docs/latest/styling/application-theme" target="_blank">documentation</a>
        on how to set up an application theme.
      </div>
    `;
  }
  renderPropertyList() {
    if (!this.context)
      return null;
    if (!this.context.metadata) {
      const e = this.context.component.element.localName;
      return n`
        <div class="notice">Styling <code>&lt;${e}&gt;</code> components is not supported at the moment.</div>
      `;
    }
    if (this.context.scope === u.local && !this.context.accessible) {
      const e = this.context.metadata.displayName;
      return n`
        ${this.context.metadata.notAccessibleDescription && this.context.scope === u.local ? n`<div class="notice hint" style="padding-bottom: 0;">
              <vaadin-icon icon="vaadin:lightbulb"></vaadin-icon>
              <div>${this.context.metadata.notAccessibleDescription}</div>
            </div>` : ""}
        <div class="notice">
          The selected ${e} cannot be styled locally. Currently, Theme Editor only supports styling
          instances that are assigned to a local variable, like so:
          <pre><code>Button saveButton = new Button("Save");</code></pre>
          If you want to modify the code so that it satisfies this requirement,
          <button class="link-button" @click=${this.handleShowComponent}>click here</button>
          to open it in your IDE. Alternatively you can choose to style all ${e}s by selecting "Global" from
          the scope dropdown above.
        </div>
      `;
    }
    return n` ${this.context.metadata.description && this.context.scope === u.local ? n`<div class="notice hint">
            <vaadin-icon icon="vaadin:lightbulb"></vaadin-icon>
            <div>${this.context.metadata.description}</div>
          </div>` : ""}
      <copilot-theme-property-list
        class="property-list"
        .metadata=${this.context.metadata}
        .theme=${this.effectiveTheme}
        @theme-property-value-change=${this.handlePropertyChange}
        @open-css=${this.handleOpenCss}></copilot-theme-property-list>`;
  }
  handleShowComponent() {
    if (!this.context)
      return;
    const t = this.context.component, e = {
      nodeId: t.nodeId,
      uiId: t.uiId,
      element: t.element
    };
    O().copilotEventBus.send("showComponentCreateLocation", e);
  }
  async handleOpenCss(t) {
    if (!this.context)
      return;
    await this.ensureLocalClassName();
    const e = {
      themeScope: this.context.scope,
      localClassName: this.context.localClassName
    }, s = M(t.detail.element, e);
    await this.api.openCss(s);
  }
  renderPicker() {
    var e;
    let t;
    if ((e = this.context) != null && e.metadata) {
      const s = this.context.scope === u.local ? this.context.metadata.displayName : `All ${this.context.metadata.displayName}s`, i = n`<span class="component-type">${s}</span>`, o = this.context.scope === u.local ? Je(this.context.component) : null, r = o ? n` <span class="instance-name-quote">"</span><span class="instance-name">${o}</span
            ><span class="instance-name-quote">"</span>` : null;
      t = n`${i} ${r}`;
    } else
      t = n`<span class="no-selection">Pick a single element to get started</span>`;
    return n` <div class="picker">${t}</div> `;
  }
  renderLocalClassNameEditor() {
    var s;
    const t = ((s = this.context) == null ? void 0 : s.scope) === u.local && this.context.accessible;
    if (!this.context || !t)
      return null;
    const e = this.context.localClassName || this.context.suggestedClassName;
    return n` <copilot-theme-class-name-editor
      .className=${e}
      @class-name-change=${this.handleClassNameChange}>
    </copilot-theme-class-name-editor>`;
  }
  async handleClassNameChange(t) {
    if (!this.context)
      return;
    const e = this.context.localClassName, s = t.detail.value;
    if (e) {
      const i = this.context.component.element;
      this.context.localClassName = s;
      const o = await this.api.setLocalClassName(this.context.component, s);
      this.historyActions = this.history.push(
        o.requestId,
        () => S.previewLocalClassName(i, s),
        () => S.previewLocalClassName(i, e)
      );
    } else
      this.context = {
        ...this.context,
        suggestedClassName: s
      };
  }
  handleScopeChange(t) {
    this.context && this.refreshTheme({
      ...this.context,
      scope: t.detail.value
    });
  }
  async handlePropertyChange(t) {
    if (!this.context || !this.baseTheme || !this.editedTheme)
      return;
    const { element: e, property: s, value: i } = t.detail;
    this.editedTheme.updatePropertyValue(e.selector, s.propertyName, i, !0), this.effectiveTheme = b.combine(this.baseTheme, this.editedTheme), await this.ensureLocalClassName();
    const o = {
      themeScope: this.context.scope,
      localClassName: this.context.localClassName
    }, r = Ge(e, o, s.propertyName, i);
    try {
      const a = await this.api.setCssRules([r]);
      this.historyActions = this.history.push(a.requestId);
      const l = Ke(r);
      S.add(l);
    } catch (a) {
      H("Failed to update property value", a);
    }
  }
  async handleUndo() {
    this.historyActions = await this.history.undo(), await this.refreshComponentAndTheme();
  }
  async handleRedo() {
    this.historyActions = await this.history.redo(), await this.refreshComponentAndTheme();
  }
  async ensureLocalClassName() {
    if (!this.context || this.context.scope === u.global || this.context.localClassName)
      return;
    if (!this.context.localClassName && !this.context.suggestedClassName)
      throw new Error(
        "Cannot assign local class name for the component because it does not have a suggested class name"
      );
    const t = this.context.component.element, e = this.context.suggestedClassName;
    this.context.localClassName = e;
    const s = await this.api.setLocalClassName(this.context.component, e);
    this.historyActions = this.history.push(
      s.requestId,
      () => S.previewLocalClassName(t, e),
      () => S.previewLocalClassName(t)
    );
  }
  async refreshComponentAndTheme(t, e) {
    var i, o, r;
    if (t = t || ((i = this.context) == null ? void 0 : i.component), e = e || ((o = this.context) == null ? void 0 : o.metadata), !t || !e)
      return;
    const s = await this.api.loadComponentMetadata(t);
    S.previewLocalClassName(t.element, s.className), await this.refreshTheme({
      scope: ((r = this.context) == null ? void 0 : r.scope) || u.local,
      metadata: e,
      component: t,
      localClassName: s.className,
      suggestedClassName: s.suggestedClassName,
      accessible: s.accessible
    });
  }
  async refreshTheme(t) {
    const e = t || this.context;
    if (!e || !e.metadata)
      return;
    if (e.scope === u.local && !e.accessible) {
      this.context = e, this.baseTheme = null, this.editedTheme = null, this.effectiveTheme = null;
      return;
    }
    let i = new b(e.metadata);
    if (!(e.scope === u.local && !e.localClassName)) {
      const a = {
        themeScope: e.scope,
        localClassName: e.localClassName
      }, l = e.metadata.elements.map(
        (d) => M(d, a)
      ), c = await this.api.loadRules(l);
      i = b.fromServerRules(e.metadata, a, c.rules);
    }
    const r = await Ze(e.metadata);
    this.context = e, this.baseTheme = r, this.editedTheme = i, this.effectiveTheme = b.combine(r, this.editedTheme);
  }
};
N([
  h({})
], w.prototype, "expanded", 2);
N([
  h({})
], w.prototype, "themeEditorState", 2);
N([
  h()
], w.prototype, "api", 2);
N([
  y()
], w.prototype, "historyActions", 2);
N([
  y()
], w.prototype, "context", 2);
N([
  y()
], w.prototype, "effectiveTheme", 2);
w = N([
  f("copilot-theme-editor")
], w);
var le = /* @__PURE__ */ ((t) => (t.state = "copilot-theme-editor-state", t.response = "copilot-theme-editor-response", t.loadComponentMetadata = "copilot-theme-editor-metadata", t.setLocalClassName = "copilot-theme-editor-local-class-name", t.setCssRules = "copilot-theme-editor-rules", t.loadRules = "copilot-theme-editor-load-rules", t.history = "copilot-theme-editor-history", t.openCss = "copilot-theme-editor-open-css", t))(le || {});
class Zt {
  constructor() {
    this.pendingRequests = {}, this.requestCounter = 0;
  }
  sendRequest(e, s) {
    const i = (this.requestCounter++).toString(), o = s.uiId ?? this.getGlobalUiId();
    return new Promise((r, a) => {
      O().copilotEventBus.send(e, {
        ...s,
        requestId: i,
        uiId: o
      }), this.pendingRequests[i] = {
        resolve: r,
        reject: a
      };
    });
  }
  handleResponse(e) {
    const s = this.pendingRequests[e.requestId];
    if (!s) {
      console.warn("Received response for unknown request");
      return;
    }
    delete this.pendingRequests[e.requestId], e.code === "ok" ? s.resolve(e) : s.reject(e);
  }
  loadComponentMetadata(e) {
    return this.sendRequest("copilot-theme-editor-metadata", { nodeId: e.nodeId });
  }
  setLocalClassName(e, s) {
    return this.sendRequest("copilot-theme-editor-local-class-name", { nodeId: e.nodeId, className: s });
  }
  setCssRules(e) {
    return this.sendRequest("copilot-theme-editor-rules", { rules: e });
  }
  loadRules(e) {
    return this.sendRequest("copilot-theme-editor-load-rules", { selectors: e });
  }
  undo(e) {
    return this.sendRequest("copilot-theme-editor-history", { undo: e });
  }
  redo(e) {
    return this.sendRequest("copilot-theme-editor-history", { redo: e });
  }
  openCss(e) {
    return this.sendRequest("copilot-theme-editor-open-css", { selector: e });
  }
  getGlobalUiId() {
    if (this.globalUiId === void 0) {
      const e = window.Vaadin;
      if (e && e.Flow) {
        const { clients: s } = e.Flow, i = Object.keys(s);
        for (const o of i) {
          const r = s[o];
          if (r.getNodeId) {
            this.globalUiId = r.getUIId();
            break;
          }
        }
      }
    }
    return this.globalUiId ?? -1;
  }
}
var Jt = Object.defineProperty, Xt = Object.getOwnPropertyDescriptor, ue = (t, e, s, i) => {
  for (var o = i > 1 ? void 0 : i ? Xt(e, s) : e, r = t.length - 1, a; r >= 0; r--)
    (a = t[r]) && (o = (i ? a(e, s, o) : a(o)) || o);
  return i && o && Jt(e, s, o), o;
};
const Te = window.Vaadin.devTools;
let J = class extends We {
  constructor() {
    super(), this.api = new Zt(), this.handleStateEvent = (t) => {
      this.themeEditorState = t.data.state;
    }, this.handleServerEvent = (t) => {
      this.api.handleResponse(t.data);
    }, this.expanded = !0, this.themeEditorState = D.disabled;
  }
  connectedCallback() {
    super.connectedCallback(), this.onCommand(le.state, this.handleStateEvent), this.onCommand(le.response, this.handleServerEvent);
  }
  render() {
    return n` <copilot-theme-editor
      .expanded=${this.expanded}
      .themeEditorState=${this.themeEditorState}
      .api=${this.api}
      @before-open=${this.disableJavaLiveReload}
      @after-close=${this.enableJavaLiveReload}></copilot-theme-editor>`;
  }
  disableJavaLiveReload() {
    var t;
    (t = Te.javaConnection) == null || t.setActive(!1);
  }
  enableJavaLiveReload() {
    var t;
    (t = Te.javaConnection) == null || t.setActive(!0);
  }
};
ue([
  y()
], J.prototype, "expanded", 2);
ue([
  y()
], J.prototype, "themeEditorState", 2);
J = ue([
  f("copilot-theme-editor-panel")
], J);
const Yt = {
  header: "Theme Editor",
  expanded: !0,
  draggable: !0,
  panelOrder: 0,
  panel: "right",
  floating: !1,
  tag: "copilot-theme-editor-panel",
  showOn: [Fe.Flow]
}, Qt = {
  init(t) {
    t.addPanel(Yt);
  }
};
window.Vaadin.copilotPlugins.push(Qt);
export {
  J as CopilotThemeEditorPanel
};
