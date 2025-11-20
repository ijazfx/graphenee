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
package io.graphenee.documents;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.graphenee.core.GrapheneeCoreConfiguration;

@Configuration
@EnableScheduling
@AutoConfigureAfter(GrapheneeCoreConfiguration.class)
@ComponentScan(GrapheneeDocumentsConfiguration.COMPONENT_SCAN_BASE_PACKAGE)
public class GrapheneeDocumentsConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.documents";

}
