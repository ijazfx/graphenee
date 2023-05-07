import { LitElement, html } from 'lit-element';

class GxEcho extends LitElement {
	static get properties() {
		return {
			"message": {type: String}
		}
	}
	
	render() {
		return html`<p>Echo: ${this.message}</p>`;
	}
}
customElements.define('gx-echo', GxEcho);