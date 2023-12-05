/*******************************************************************************
 * Copyright (c) 2016, 2022 Farrukh Ijaz
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
package io.graphenee.core.model.api;

import java.util.List;

import org.springframework.data.domain.Sort;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxAuditLog;

public interface GxAuditLogDataService {

	List<GxAuditLog> fetch(int pageNumber, int pageSize, GxAuditLog se, Sort sort);

	int count(GxAuditLog se);

	GxAuditLog log(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String detail, String auditEntity, Integer oidAuditEntity);

	GxAuditLog log(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String detail);

	GxAuditLog log(String username, String remoteAddress, String auditEvent, String detail, String auditEntity, Integer oidAuditEntity);

	GxAuditLog log(String username, String remoteAddress, String auditEvent, String detail);

}
