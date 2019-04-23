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
package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxAccessLog;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxAccessLogRepository extends GxJpaRepository<GxAccessLog, Integer> {

	List<GxAccessLog> findAllByGxAccessKeyOid(Integer oidAccessKey);

	@Query("select g2 from GxAccessLog g2 where g2.gxAccessKey.oid=:oidAccessKey and g2.isSuccess=:isSuccess and (g2.accessType=:accessTypeCheckIn or g2.accessType=:accessTypeCheckOut) and g2.accessTime = "
			+ "(select  max(g1.accessTime) from GxAccessLog g1 where g1.gxAccessKey.oid=:oidAccessKey and g1.isSuccess=:isSuccess and (g1.accessType=:accessTypeCheckIn or g1.accessType=:accessTypeCheckOut) and g1.accessTime BETWEEN :fromDate and :toDate)")
	GxAccessLog findTodayLastLogByAccessKey(@Param("oidAccessKey") Integer oidAccessKey, @Param("isSuccess") Boolean isSuccess,
			@Param("accessTypeCheckIn") Integer accessTypeCheckIn, @Param("accessTypeCheckOut") Integer accessTypeCheckOut, @Param("fromDate") Timestamp fromDate,
			@Param("toDate") Timestamp toDate);
}
