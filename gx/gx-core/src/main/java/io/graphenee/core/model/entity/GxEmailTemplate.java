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
package io.graphenee.core.model.entity;

import java.io.Serializable;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the gx_email_template database table.
 * 
 */
@Getter
@Setter

@Entity
@Table(name = "gx_email_template")
public class GxEmailTemplate extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bccList;
	private String body;
	private String smsBody;
	private String ccList;
	private Boolean isActive;
	private Boolean isProtected = false;
	private String subject;
	private String templateName;
	private String templateCode;
	private String senderEmailAddress;

	//bi-directional many-to-one association to GxNamespace
	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

}