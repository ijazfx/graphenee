import { g as b, V as h, x as v, M as C, r as I, t as w } from "./copilot-5xZabcKF.js";
import { B as E } from "./base-panel-ZMfG2SWA.js";
var x = function() {
  var e = document.getSelection();
  if (!e.rangeCount)
    return function() {
    };
  for (var t = document.activeElement, a = [], s = 0; s < e.rangeCount; s++)
    a.push(e.getRangeAt(s));
  switch (t.tagName.toUpperCase()) {
    case "INPUT":
    case "TEXTAREA":
      t.blur();
      break;
    default:
      t = null;
      break;
  }
  return e.removeAllRanges(), function() {
    e.type === "Caret" && e.removeAllRanges(), e.rangeCount || a.forEach(function(n) {
      e.addRange(n);
    }), t && t.focus();
  };
}, A = x, g = {
  "text/plain": "Text",
  "text/html": "Url",
  default: "Text"
}, R = "Copy to clipboard: #{key}, Enter";
function S(e) {
  var t = (/mac os x/i.test(navigator.userAgent) ? "⌘" : "Ctrl") + "+C";
  return e.replace(/#{\s*key\s*}/g, t);
}
function D(e, t) {
  var a, s, n, d, l, o, u = !1;
  t || (t = {}), a = t.debug || !1;
  try {
    n = A(), d = document.createRange(), l = document.getSelection(), o = document.createElement("span"), o.textContent = e, o.ariaHidden = "true", o.style.all = "unset", o.style.position = "fixed", o.style.top = 0, o.style.clip = "rect(0, 0, 0, 0)", o.style.whiteSpace = "pre", o.style.webkitUserSelect = "text", o.style.MozUserSelect = "text", o.style.msUserSelect = "text", o.style.userSelect = "text", o.addEventListener("copy", function(c) {
      if (c.stopPropagation(), t.format)
        if (c.preventDefault(), typeof c.clipboardData > "u") {
          a && console.warn("unable to use e.clipboardData"), a && console.warn("trying IE specific stuff"), window.clipboardData.clearData();
          var f = g[t.format] || g.default;
          window.clipboardData.setData(f, e);
        } else
          c.clipboardData.clearData(), c.clipboardData.setData(t.format, e);
      t.onCopy && (c.preventDefault(), t.onCopy(c.clipboardData));
    }), document.body.appendChild(o), d.selectNodeContents(o), l.addRange(d);
    var y = document.execCommand("copy");
    if (!y)
      throw new Error("copy command was unsuccessful");
    u = !0;
  } catch (c) {
    a && console.error("unable to copy using execCommand: ", c), a && console.warn("trying IE specific stuff");
    try {
      window.clipboardData.setData(t.format || "text", e), t.onCopy && t.onCopy(window.clipboardData), u = !0;
    } catch (f) {
      a && console.error("unable to copy using clipboardData: ", f), a && console.error("falling back to prompt"), s = S("message" in t ? t.message : R), window.prompt(s, e);
    }
  } finally {
    l && (typeof l.removeRange == "function" ? l.removeRange(d) : l.removeAllRanges()), o && document.body.removeChild(o), n();
  }
  return u;
}
var T = D;
const $ = /* @__PURE__ */ b(T);
var i = /* @__PURE__ */ ((e) => (e.ACTIVE = "active", e.INACTIVE = "inactive", e.UNAVAILABLE = "unavailable", e.ERROR = "error", e))(i || {}), N = Object.defineProperty, V = Object.getOwnPropertyDescriptor, m = (e, t, a, s) => {
  for (var n = s > 1 ? void 0 : s ? V(t, a) : t, d = e.length - 1, l; d >= 0; d--)
    (l = e[d]) && (n = (s ? l(t, a, n) : l(n)) || n);
  return s && n && N(t, a, n), n;
};
const r = window.Vaadin.devTools;
let p = class extends E {
  constructor() {
    super(), this.handleServerInfoEvent = (e) => {
      this.serverInfo = e.data;
      const t = Math.max(this.getIndex("Flow"), this.getIndex("Vaadin"), this.getIndex("Hilla"));
      this.serverInfo.versions.splice(t + 1, 0, { name: "Copilot", version: h });
    }, this.serverInfo = {
      versions: []
    };
  }
  connectedCallback() {
    super.connectedCallback(), this.onCommand("serverInfo", this.handleServerInfoEvent);
  }
  getIndex(e) {
    return this.serverInfo.versions.findIndex((t) => t.name === e);
  }
  render() {
    return v` <div class="info-tray">
      <button class="button copy" @click=${this.copyInfoToClipboard}>Copy</button>
      <dl>
        ${this.serverInfo.versions.map(
      (e) => v`
            <dt>${e.name}</dt>
            <dd>${e.version}</dd>
          `
    )}
        <dt>Browser</dt>
        <dd>${navigator.userAgent}</dd>
        <dt>
          Live reload
          <label class="switch">
            <input
              id="toggle"
              type="checkbox"
              ?disabled=${!r.conf.enable || (r.frontendStatus === i.UNAVAILABLE || r.frontendStatus === i.ERROR) && (r.javaStatus === i.UNAVAILABLE || r.javaStatus === i.ERROR)}
              ?checked="${r.frontendStatus === i.ACTIVE || r.javaStatus === i.ACTIVE}"
              @change=${(e) => r.setActive(e.target.checked)} />
            <span class="slider"></span>
          </label>
        </dt>
        <dd class="live-reload-status" style="--status-color: ${r.getStatusColor(r.javaStatus)}">
          Java ${r.javaStatus} ${r.conf.backend ? `(${r.conf.backend})` : ""}
        </dd>
        <dd class="live-reload-status" style="--status-color: ${r.getStatusColor(r.frontendStatus)}">
          Front end ${r.frontendStatus}
        </dd>
      </dl>
    </div>`;
  }
  copyInfoToClipboard() {
    const e = this.renderRoot.querySelectorAll(".info-tray dt, .info-tray dd"), t = Array.from(e).map((a) => (a.localName === "dd" ? ": " : `
`) + a.textContent.trim()).join("").replace(/^\n/, "");
    $(t), r.showNotification(
      C.INFORMATION,
      "Environment information copied to clipboard",
      void 0,
      void 0,
      "versionInfoCopied"
    );
  }
};
m([
  I()
], p.prototype, "serverInfo", 2);
p = m([
  w("copilot-info-panel")
], p);
const O = {
  header: "Info",
  expanded: !0,
  draggable: !0,
  panelOrder: 0,
  panel: "right",
  floating: !1,
  tag: "copilot-info-panel"
}, P = {
  init(e) {
    e.addPanel(O);
  }
};
window.Vaadin.copilotPlugins.push(P);
export {
  p as CopilotInfoPanel
};
