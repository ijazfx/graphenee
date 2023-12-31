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
package io.graphenee.core.flow;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.documents.GrapheneeDocumentsConfiguration;
import io.graphenee.i18n.GrapheneeI18nConfiguration;
import io.graphenee.security.GrapheneeSecurityConfiguration;
import io.graphenee.sms.GrapheneeSmsConfiguration;

@Configuration
@AutoConfigureAfter({ GrapheneeCoreConfiguration.class, GrapheneeI18nConfiguration.class, GrapheneeSecurityConfiguration.class, GrapheneeSmsConfiguration.class,
		GrapheneeDocumentsConfiguration.class })
public class GrapheneeCoreFlowConfiguration {

}
