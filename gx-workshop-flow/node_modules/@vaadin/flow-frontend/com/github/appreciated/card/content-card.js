import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

class Card extends PolymerElement {
    static get is() {
        return 'content-card'
    }

    static get template() {
        return html`
        <style>
            #card-content {
                transition: box-shadow 0.35s ease;
                box-shadow: 0 0 0 0 rgba(0, 0, 0, 0), 0 0 0 0 rgba(0, 0, 0, 0);
                border-radius: var(--lumo-border-radius, 4px);
                background-color: rgba(255, 255, 255, 0.06);
                width: 100%;
                height: 100%;
            }

            #card-content[elevation="1"] {
                box-shadow: var(--lumo-box-shadow-s, var(--material-shadow-elevation-2dp));
            }

            #card-content[elevation="2"] {
                box-shadow: var(--lumo-box-shadow-m, var(--material-shadow-elevation-4dp));
            }

            #card-content[elevation="3"] {
                box-shadow: var(--lumo-box-shadow-l, var(--material-shadow-elevation-8dp));
            }

            #card-content[elevation="4"] {
                box-shadow: var(--lumo-box-shadow-l, var(--material-shadow-elevation-16dp));
            }
        </style>
        <div id="card-content">
            <slot></slot>
        </div>`;
    }
}

customElements.define(Card.is, Card);
