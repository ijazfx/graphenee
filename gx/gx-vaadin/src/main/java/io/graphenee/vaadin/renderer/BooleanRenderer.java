/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin.renderer;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.renderers.ImageRenderer;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

public class BooleanRenderer extends ImageRenderer {

	public static final CheckBoxConverter CHECK_BOX_CONVERTER = new CheckBoxConverter();
	public static final SwitchConverter SWITCH_CONVERTER = new SwitchConverter();
	public static final StatusConverter STATUS_CONVERTER = new StatusConverter();

	public BooleanRenderer() {
	}

	public BooleanRenderer(RendererClickListener listener) {
		super(listener);
	}

	public static abstract class BooleanConverter implements Converter<Resource, Boolean> {

		@Override
		public Boolean convertToModel(Resource value, Class<? extends Boolean> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
			if (value == trueStateResource()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}

		@Override
		public Resource convertToPresentation(Boolean value, Class<? extends Resource> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			if (value == Boolean.TRUE) {
				return trueStateResource();
			}
			return falseStateResource();
		}

		@Override
		public Class<Boolean> getModelType() {
			return Boolean.class;
		}

		@Override
		public Class<Resource> getPresentationType() {
			return Resource.class;
		}

		protected abstract Resource trueStateResource();

		protected abstract Resource falseStateResource();

	}

	public static class CheckBoxConverter extends BooleanConverter {

		Resource trueStateResource;
		Resource falseStateResource;

		public CheckBoxConverter() {
			trueStateResource = GrapheneeTheme.themeResource("images/checked_checkbox.png");
			falseStateResource = GrapheneeTheme.themeResource("images/unchecked_checkbox.png");
		}

		@Override
		protected Resource trueStateResource() {
			return trueStateResource;
		}

		@Override
		protected Resource falseStateResource() {
			return falseStateResource;
		}

	}

	public static class SwitchConverter extends BooleanConverter {

		Resource trueStateResource;
		Resource falseStateResource;

		public SwitchConverter() {
			trueStateResource = GrapheneeTheme.themeResource("images/toggle-on.png");
			falseStateResource = GrapheneeTheme.themeResource("images/toggle-off.png");
		}

		@Override
		protected Resource trueStateResource() {
			return trueStateResource;
		}

		@Override
		protected Resource falseStateResource() {
			return falseStateResource;
		}

	}

	public static class StatusConverter extends BooleanConverter {

		Resource trueStateResource;
		Resource falseStateResource;

		public StatusConverter() {
			trueStateResource = GrapheneeTheme.themeResource("images/ok.png");
			falseStateResource = GrapheneeTheme.themeResource("images/warn.png");
		}

		@Override
		protected Resource trueStateResource() {
			return trueStateResource;
		}

		@Override
		protected Resource falseStateResource() {
			return falseStateResource;
		}

	}

}
