package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.flow.security.GxUserAccountProfileForm;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.vaadin.flow.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.component.GxNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CssImport("./styles/app.css")
public class MainLayout extends GxAbstractAppLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAbstractFlowSetup flowSetup;

	@Autowired
	GxUserAccountRepository userAccountRepository;

	@Autowired
	GxUserAccountProfileForm form;

	@Override
	protected GxAbstractFlowSetup flowSetup() {
		return flowSetup;
	}

	@Override
	protected <T extends GxAuthenticatedUser> GxAbstractEntityForm<T> getProfileForm(T user) {
		if (user instanceof GxUserAccount) {
			return (GxAbstractEntityForm<T>) form;
		}
		return null;
	}

	@Override
	protected void saveProfile(GxAuthenticatedUser user) {
		if (user instanceof GxUserAccount) {
			try {
				userAccountRepository.save((GxUserAccount) user);
				GxNotification.success("Changes will be available once you login again.");
			} catch (Exception ex) {
				log.error("Error while saving profile: {}", ex.getMessage());
				GxNotification.error("Error while saving profile: " + ex.getMessage());
			}
		}
	}
}
