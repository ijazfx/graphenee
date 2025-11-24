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
package io.graphenee.core;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSecurityPolicyDocument;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.documents.GrapheneeDocumentsConfiguration;
import io.graphenee.i18n.GrapheneeI18nConfiguration;
import io.graphenee.security.GrapheneeSecurityConfiguration;
import io.graphenee.sms.GrapheneeSmsConfiguration;
import io.graphenee.util.TRCalendarUtil;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories({ GrapheneeCoreConfiguration.JPA_REPOSITORIES_BASE_PACKAGE })
@EntityScan({ GrapheneeCoreConfiguration.ENTITY_SCAN_BASE_PACKAGE })
@ComponentScan({ GrapheneeCoreConfiguration.COMPONENT_SCAN_BASE_PACKAGE,
		GrapheneeI18nConfiguration.COMPONENT_SCAN_BASE_PACKAGE,
		GrapheneeSecurityConfiguration.COMPONENT_SCAN_BASE_PACKAGE,
		GrapheneeSmsConfiguration.COMPONENT_SCAN_BASE_PACKAGE,
		GrapheneeDocumentsConfiguration.COMPONENT_SCAN_BASE_PACKAGE })
public class GrapheneeCoreConfiguration {

	public static final String COMPONENT_SCAN_BASE_PACKAGE = "io.graphenee.core";
	public static final String ENTITY_SCAN_BASE_PACKAGE = "io.graphenee.core.model.entity";
	public static final String JPA_REPOSITORIES_BASE_PACKAGE = "io.graphenee.core.model.jpa.repository";

	@Autowired
	GxDataService dataService;

	@EventListener(ApplicationReadyEvent.class)
	@Order(100)
	public void initialize() {
		GxNamespace namespace = dataService.systemNamespace();
		GxSecurityGroup adminGroup = dataService.findOrCreateSecurityGroup("Admin", namespace);
		GxSecurityPolicy adminPolicy = dataService.findOrCreateSecurityPolicy("Admin Policy", namespace);
		GxSecurityPolicyDocument document = adminPolicy.defaultDocument();
		if (document == null) {
			document = new GxSecurityPolicyDocument();
			document.setIsDefault(true);
			document.setDocumentJson("grant all");
			document.setTag(TRCalendarUtil.yyyyMMddHHmmssFormatter.format(new Timestamp(0)));
			adminPolicy.addSecurityPolicyDocument(document);
			dataService.save(adminPolicy);
		}
		if (!adminGroup.getSecurityPolicies().contains(adminPolicy)) {
			adminGroup.getSecurityPolicies().add(adminPolicy);
			dataService.save(adminGroup);
		}
		GxUserAccount admin = dataService.findUserAccountByUsernameAndNamespace("admin", namespace);
		if (admin == null) {
			admin = new GxUserAccount();
			admin.setUsername("admin");
			admin.setPassword("change_on_install");
			admin.setIsActive(true);
			admin.setIsProtected(true);
			admin.setNamespace(namespace);
			dataService.save(admin);
		}
		if (!admin.getSecurityGroups().contains(adminGroup)) {
			admin.getSecurityGroups().add(adminGroup);
			dataService.save(admin);
		}
	}

}
