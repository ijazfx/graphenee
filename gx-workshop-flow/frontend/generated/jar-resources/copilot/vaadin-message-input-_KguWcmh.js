import { K as e, E as a, N as r, R as t } from "./copilot-5xZabcKF.js";
import { inputFieldProperties as s } from "./vaadin-text-field-GsooWDDF.js";
const p = {
  tagName: "vaadin-message-input",
  displayName: "Message Input",
  elements: [
    {
      selector: "vaadin-message-input vaadin-text-area::part(input-field)",
      displayName: "Text area",
      properties: s
    },
    {
      selector: "vaadin-message-input vaadin-button",
      displayName: "Button",
      properties: [
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
        t.paddingInline
      ]
    }
  ]
};
export {
  p as default
};
