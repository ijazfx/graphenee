import { K as a, E as r, N as t, J as e } from "./copilot-5xZabcKF.js";
const i = {
  tagName: "vaadin-avatar",
  displayName: "Avatar",
  elements: [
    {
      selector: "vaadin-avatar",
      displayName: "Root element",
      properties: [
        a.backgroundColor,
        a.borderColor,
        a.borderWidth,
        {
          propertyName: "--vaadin-avatar-size",
          displayName: "Size",
          editorType: r.range,
          presets: t.lumoSize,
          icon: "square"
        }
      ]
    },
    {
      selector: "vaadin-avatar::part(abbr)",
      displayName: "Abbreviation",
      properties: [e.textColor, e.fontWeight]
    }
  ]
};
export {
  i as default
};
