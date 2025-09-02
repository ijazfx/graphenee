package io.graphenee.workshop.vaadin;

import com.vaadin.flow.component.avatar.Avatar;
import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.flow.security.GxUserAccountProfileForm;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.GxNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;

import io.graphenee.vaadin.flow.GxAbstractAppLayout;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;

import java.util.Optional;

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
	protected void customizeAvatar(Avatar avatar) {
		super.customizeAvatar(avatar);

		avatar.getElement().addEventListener("click",
				e -> {
					GxAuthenticatedUser authenticatedUser = flowSetup().loggedInUser();
					Integer userId = authenticatedUser.getOid();
					Optional<GxUserAccount> user = userAccountRepository.findById(userId);
					if (user.isPresent()) {
						form.setEntity(user.get());
						form.setDelegate(new GxAbstractEntityForm.EntityFormDelegate<GxUserAccount>() {
							@Override
							public void onSave(GxUserAccount entity) {
								try {
									userAccountRepository.save(entity);
									GxNotification.success("Changes will be available once you login again.");
								} catch (Exception ex) {
									log.error("Error while saving profile: {}", ex.getMessage());
									GxNotification.error("Error while saving profile: " + ex.getMessage());
								}

							}
						});
						form.showInDialog(user.get());
					} else {
						form.setEntity(null);
					}
				});
	}
}
