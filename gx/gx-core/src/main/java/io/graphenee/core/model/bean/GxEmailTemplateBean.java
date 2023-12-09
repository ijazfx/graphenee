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
package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.entity.GxNamespace;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GxEmailTemplateBean implements Serializable {

	private static final long serialVersionUID = 6239781437082703091L;

	private Integer oid;

	private String bccList;
	private String body, smsBody;
	private String ccList;
	private Boolean isActive = true;
	private Boolean isProtected = false;
	private String subject;
	private String templateName;
	private String templateCode;
	private BeanFault<Integer, GxNamespace> namespaceBeanFault;
	private String senderEmailAddress;

	public String getNamespace() {
		if (namespaceBeanFault != null)
			return namespaceBeanFault.getBean().getNamespace();
		return null;
	}

}