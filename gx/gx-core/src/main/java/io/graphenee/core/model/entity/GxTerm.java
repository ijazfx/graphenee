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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_term")
public class GxTerm extends GxMappedSuperclass {

	private Boolean isActive = true;
	private Boolean isProtected = false;
	private String termKey;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

	@OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GxTermTranslation> translations = new ArrayList<>();

	public GxTermTranslation addToTranslations(GxTermTranslation translation) {
		if (!translations.contains(translation)) {
			translations.add(translation);
			translation.setTerm(this);
		}
		return translation;
	}

	public GxTermTranslation removeFromTranslations(GxTermTranslation translation) {
		if (translations.contains(translation)) {
			translations.remove(translation);
			translation.setTerm(null);
		}
		return translation;
	}

	public Map<GxSupportedLocale, GxTermTranslation> translationMap() {
		return translations.stream()
				.collect(Collectors.toMap(GxTermTranslation::getSupportedLocale, Function.identity(), (first, second) -> first));
	}

}