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
package io.graphenee.workshop;

import static org.junit.Assert.assertNotEquals;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.graphenee.i18n.api.LocalizerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalizerServiceTest {

	@Autowired
	LocalizerService localizerService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void supportedLocaleTest() {
		String key = "app.title";
		String value = localizerService.getSingularValue("app.title");
		System.err.println(value);
		Locale arabicLocale = new Locale("ar", "SA");
		value = localizerService.getSingularValue(arabicLocale, "app.title");
		System.err.println(value);

		assertNotEquals(key, value);
	}

}
