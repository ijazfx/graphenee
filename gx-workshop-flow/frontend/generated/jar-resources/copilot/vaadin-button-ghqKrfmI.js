import { P as t, K as e, E as a, N as r, R as o } from "./copilot-5xZabcKF.js";
const s = [
  e.backgroundColor,
  e.borderColor,
  e.borderWidth,
  e.borderRadius,
  {
    propertyName: "--lumo-button-size",
    displayName: "Size",
    editorType: a.range,
    presets: r.lumoSize,
    icon: "square"
  },
  o.paddingInline
], d = {
  tagName: "vaadin-button",
  displayName: "Button",
  elements: [
    {
      selector: "vaadin-button",
      displayName: "Root element",
      properties: s
    },
    {
      selector: "vaadin-button::part(label)",
      displayName: "Label",
      properties: t
    }
  ]
};
export {
  d as default,
  s as standardButtonProperties
};
