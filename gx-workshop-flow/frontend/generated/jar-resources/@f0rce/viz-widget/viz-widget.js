/**
 * @license MIT
 * Copyright 2021 David "F0rce" Dodlek
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import Viz from "viz.js";
import { Module, render } from "viz.js/full.render.js";

import svgPanZoom from "svg-pan-zoom";

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";

var viz = new Viz({ Module, render });

class VizWidget extends PolymerElement {
  static get is() {
    return "viz-widget";
  }

  static get template() {
    return html`
      <style>
        :host {
          display: flex;
          display: -webkit-flex;
          flex-direction: column;
          -webkit-flex-direction: column;
          width: 100%;
          height: 100%;
        }
        #output {
          flex: 1 1 auto;
          -webkit-flex: 1 1 auto;
          position: relative;
          overflow: auto;
        }
        #output svg {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
        }
        #output #text {
          font-size: 12px;
          font-family: monaco, courier, monospace;
          white-space: pre;
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          overflow: auto;
        }
        #output img {
          display: block;
          margin: 0 auto;
        }
        #output.working svg,
        #output.error svg,
        #output.working #text,
        #output.error #text,
        #output.working img,
        #output.error img {
          opacity: 0.4;
        }
        #output.error #error {
          display: inherit;
        }
        #output #error {
          display: none;
          position: absolute;
          top: 20px;
          left: 20px;
          margin-right: 20px;
          background: red;
          color: white;
          z-index: 1;
        }
      </style>
      <div id="output" class="">
        <div id="error"></div>
      </div>
    `;
  }

  static get properties() {
    return {
      graph: {
        type: String,
        observer: "updateGraph",
      },
      controlIconsEnabled: {
        type: Boolean,
        value: true,
      },
      engine: {
        type: String,
        value: "dot",
        observer: "updateGraph",
      },
      format: {
        type: String,
        value: "svg",
        observer: "updateGraph",
      },
      mimeType: {
        type: String,
        value: "image/png",
        observer: "updateGraph",
      },
      mouseWheelZoomEnabled: {
        type: Boolean,
        value: true,
      }
    };
  }

  connectedCallback() {
    super.connectedCallback();

    this.parser = new DOMParser();
    this.running = false;
    this._graph;
    this.result;

    let div = this.$.output;
    div.style.width = "100%";
    div.style.height = "100%";

    this.updateGraph();
    this.updateOutput();
  }

  updateGraph() {
    if (this.running) return;
    if (!this.graph) return;
    const output = this.shadowRoot.getElementById("output");

    output.classList.add("working");
    output.classList.remove("error");

    this._graph = this.graph;

    if (this.format == "img") {
      this.running = true;
      console.log(this.mimeType);
      viz
        .renderImageElement(this.graph, {
          engine: this.engine,
          mimeType: this.mimeType,
        })
        .then((e) => {
          output.classList.remove("working");
          output.classList.remove("error");

          this.result = e;

          this.updateOutput();
        })
        .catch((e) => {
          viz = new Viz({ Module, render });
          output.classList.remove("working");
          output.classList.add("error");

          var message =
            e.message === undefined
              ? "An error occurred while processing the graph input."
              : e.message;

          var error = this.shadowRoot.getElementById("error");
          while (error.firstChild) {
            error.removeChild(error.firstChild);
          }

          error.appendChild(document.createTextNode(message));

          console.error(e);
          this.running = false;
        });
    } else {
      this.running = true;
      viz
        .renderString(this.graph, { engine: this.engine, format: this.format })
        .then((e) => {
          output.classList.remove("working");
          output.classList.remove("error");

          this.result = e;

          this.updateOutput();
        })
        .catch((e) => {
          viz = new Viz({ Module, render });
          output.classList.remove("working");
          output.classList.add("error");

          var message =
            e.message === undefined
              ? "An error occurred while processing the graph input."
              : e.message;

          var error = this.shadowRoot.getElementById("error");
          while (error.firstChild) {
            error.removeChild(error.firstChild);
          }

          error.appendChild(document.createTextNode(message));

          console.error(e);
          this.running = false;
        });
    }
  }

  updateOutput() {
    var graph = this.shadowRoot.getElementById("output");

    var svg = graph.querySelector("svg");
    if (svg) {
      graph.removeChild(svg);
    }

    var text = graph.querySelector("#text");
    if (text) {
      graph.removeChild(text);
    }

    var img = graph.querySelector("img");
    if (img) {
      graph.removeChild(img);
    }

    if (!this.result) {
      return;
    }

    if (this.format == "svg") {
      var svg = this.parser.parseFromString(this.result, "image/svg+xml")
        .documentElement;
      svg.id = "svg_output";
      graph.appendChild(svg);

      svgPanZoom(svg, {
        zoomEnabled: true,
        controlIconsEnabled: this.controlIconsEnabled,
        fit: true,
        center: true,
        minZoom: 0.1,
        mouseWheelZoomEnabled: this.mouseWheelZoomEnabled,
      });
    } else if (this.format == "img") {
      graph.appendChild(this.result);
    } else {
      var text = document.createElement("div");
      text.id = "text";
      text.appendChild(document.createTextNode(this.result));
      graph.appendChild(text);
    }

    this.running = false;
    if (this._graph !== this.graph) {
      this.updateGraph();
    }
  }
}

customElements.define(VizWidget.is, VizWidget);
