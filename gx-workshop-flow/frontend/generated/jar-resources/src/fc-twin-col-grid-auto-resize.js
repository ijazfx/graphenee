/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2022 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
 
let resizeObserver = null;

window.fcTwinColGridAutoResize  = {

	observe: function(twincolgrid) {

		resizeObserver = new ResizeObserver(entries => {
			requestAnimationFrame(() => {
				for (let entry of entries) {
					if (entry.contentBoxSize) {
						var width = twincolgrid.shadowRoot.host.offsetWidth;
						var height = twincolgrid.shadowRoot.host.offsetHeight;
						twincolgrid.$server.updateOrientationOnResize(width, height);
					}
				}
			});
		});

		resizeObserver.observe(twincolgrid);
	},

	unobserve: function(twincolgrid) {
		if (resizeObserver != null) {
			resizeObserver.unobserve(twincolgrid);
		}
	},

}