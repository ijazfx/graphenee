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
package io.graphenee.security.api;

import java.sql.Timestamp;
import java.util.List;

import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.security.exception.GxPermissionException;

/**
 * This is the core security interface offered by Graphenee. You authenticate,
 * authorize and assert security permissions based on users, groups and
 * policies.
 * 
 * @author ijazfx
 */
public interface GxSecurityDataService {

	List<GxResourceBean> findResources(GxNamespaceBean gxNamespaceBean, String accessKey) throws GxPermissionException;

	void checkIn(GxNamespaceBean gxNamespaceBean, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException;

	void checkOut(GxNamespaceBean gxNamespaceBean, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException;

	void access(GxNamespaceBean gxNamespaceBean, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException;

	boolean canAccessResource(GxNamespaceBean gxNamespaceBean, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException;

}
