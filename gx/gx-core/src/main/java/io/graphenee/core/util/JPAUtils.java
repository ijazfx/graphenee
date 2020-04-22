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
package io.graphenee.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class provides the basic Utility Methods for Session Beans
 *
 * @author tahamalik
 */
public class JPAUtils {
	public static <E, B> List<B> applyMapper(List<E> entities, Function<E, B> mapper) {
		List<B> beans = new ArrayList<>();
		for (E entity : entities) {
			beans.add(mapper.apply(entity));
		}

		return beans;
	}

}
