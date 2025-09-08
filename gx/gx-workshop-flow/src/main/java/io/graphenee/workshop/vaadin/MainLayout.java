package io.graphenee.workshop.vaadin;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.flow.GxUserAccountDashboardUser;
import io.graphenee.core.flow.security.GxUserAccountProfileForm;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.GxNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;

import io.graphenee.vaadin.flow.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;

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
	protected GxAbstractEntityForm<?> getProfileForm(GxAuthenticatedUser user) {
		form.setEntity(user);
		return form;
	}

	@Override
	protected void saveProfile(Object user) {
		try {
			GxUserAccountDashboardUser u = (GxUserAccountDashboardUser) user;
			userAccountRepository.save(u.getUser());
			GxNotification.success("Changes will be available once you login again.");
		} catch (Exception ex) {
			log.error("Error while saving profile: {}", ex.getMessage());
			GxNotification.error("Error while saving profile: " + ex.getMessage());
		}

	}
}
