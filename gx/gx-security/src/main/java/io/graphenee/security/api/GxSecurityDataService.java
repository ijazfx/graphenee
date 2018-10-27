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

import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxResource;
import io.graphenee.security.exception.GxPermissionException;

/**
 * This is the core security interface offered by Graphenee. You authenticate,
 * authorize and assert security permissions based on users, groups and
 * policies.
 * 
 * @author ijazfx
 */

public interface GxSecurityDataService {
	void checkIn(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException;

	void checkOut(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException;

	void access(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException;

	boolean canAccessResource(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp);
}
