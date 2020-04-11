
// eagerly import theme styles so as we can override them
import "@vaadin/vaadin-lumo-styles/all-imports";

import "@vaadin/vaadin-charts/theme/vaadin-chart-default-theme";

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-shade-5pct: rgba(33, 33, 33, 0.05);
      --lumo-shade-10pct: rgba(33, 33, 33, 0.1);
      --lumo-shade-20pct: rgba(33, 33, 33, 0.2);
      --lumo-shade-30pct: rgba(33, 33, 33, 0.3);
      --lumo-shade-40pct: rgba(33, 33, 33, 0.4);
      --lumo-shade-50pct: rgba(33, 33, 33, 0.5);
      --lumo-shade-60pct: rgba(33, 33, 33, 0.6);
      --lumo-shade-70pct: rgba(33, 33, 33, 0.7);
      --lumo-shade-80pct: rgba(33, 33, 33, 0.8);
      --lumo-shade-90pct: rgba(33, 33, 33, 0.9);
      --lumo-primary-color-50pct: rgba(235, 89, 5, 0.5);
      --lumo-primary-color-10pct: rgba(235, 89, 5, 0.1);
      --lumo-error-color-50pct: rgba(231, 24, 24, 0.5);
      --lumo-error-color-10pct: rgba(231, 24, 24, 0.1);
      --lumo-success-color-50pct: rgba(62, 229, 170, 0.5);
      --lumo-success-color-10pct: rgba(62, 229, 170, 0.1);
      --lumo-shade: hsl(0, 0%, 13%);
      --lumo-primary-color: #cc0000;
      --lumo-primary-text-color: #cc0000;
      --lumo-error-color: hsl(0, 81%, 50%);
      --lumo-error-text-color: hsl(0, 86%, 45%);
      --lumo-success-color: hsl(159, 76%, 57%);
      --lumo-success-contrast-color: hsl(159, 29%, 10%);
      --lumo-success-text-color: hsl(159, 61%, 40%);
    }
  </style>
</custom-style>


<custom-style>
  <style>
    html {
      overflow:hidden;
    }
    vaadin-app-layout vaadin-tab a:hover {
      text-decoration: none;
    }
  </style>
</custom-style>

<dom-module id="chart" theme-for="vaadin-chart">
  <template>
    <style include="vaadin-chart-default-theme">
      :host {
        --vaadin-charts-color-0: var(--lumo-primary-color);
        --vaadin-charts-color-1: var(--lumo-error-color);
        --vaadin-charts-color-2: var(--lumo-success-color);
        --vaadin-charts-color-3: var(--lumo-contrast);
      }
      .highcharts-container {
        font-family: var(--lumo-font-family);
      }
      .highcharts-background {
        fill: var(--lumo-base-color);
      }
      .highcharts-title {
        fill: var(--lumo-header-text-color);
        font-size: var(--lumo-font-size-xl);
        font-weight: 600;
        line-height: var(--lumo-line-height-xs);
      }
      .highcharts-legend-item text {
        fill: var(--lumo-body-text-color);
      }
      .highcharts-axis-title,
      .highcharts-axis-labels {
        fill: var(--lumo-secondary-text-color);
      }
      .highcharts-axis-line,
      .highcharts-grid-line,
      .highcharts-tick {
        stroke: var(--lumo-contrast-10pct);
      }
      .highcharts-column-series rect.highcharts-point {
        stroke: var(--lumo-base-color);
      }
    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
