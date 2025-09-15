package io.graphenee.core.flow.security;

import java.io.IOException;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import com.vaadin.flow.server.streams.InputStreamDownloadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.GxDialog;
import lombok.extern.slf4j.Slf4j;

@SpringComponent
@Scope("prototype")
@Slf4j
public class GxUserAccountProfileForm extends GxAbstractEntityForm<GxAuthenticatedUser> {
    private static final long serialVersionUID = 1L;

    public GxUserAccountProfileForm() {
        super(GxAuthenticatedUser.class);
    }

    private TextField username;
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    Upload imageUploader;

    Avatar pictureAvatar;

    Button tapToEdit;

    @Override
    protected void decorateForm(HasComponents entityForm) {

        username = new TextField("Username");
        username.setReadOnly(true);

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");

        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory((metadata, data) -> {
            try {
                // Display image in Avatar
                DownloadHandler dh = new InputStreamDownloadHandler(new InputStreamDownloadCallback() {

                    @Override
                    public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
                        return new DownloadResponse(new java.io.ByteArrayInputStream(data), metadata.fileName(),
                                metadata.contentType(), metadata.contentLength());
                    }

                });

                pictureAvatar.setImageHandler(dh);

                getEntity().setProfilePhoto(data);

            } catch (Exception e) {
                log.error("Error while image upload: {}", e.getMessage());
            }
        });

        imageUploader = new Upload(inMemoryHandler);
        imageUploader.getElement().getStyle().set("display", "none");

        pictureAvatar = new Avatar("Picture");
        pictureAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        pictureAvatar.setName("Picture");
        pictureAvatar.getStyle().set("width", "130px"); // Set custom width
        pictureAvatar.getStyle().set("height", "130px");
        pictureAvatar.getStyle().set("font-size", "20px"); // For initials
        pictureAvatar.getStyle().set("cursor", "pointer");
        pictureAvatar.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "50%")
                .set("box-shadow", "0 2px 6px rgba(0, 0, 0, 0.2)");
        pictureAvatar.getElement().addEventListener("click",
                e -> {
                    imageUploader.getElement().executeJs("this.shadowRoot.querySelector('input[type=file]').click();");
                });

        tapToEdit = new Button("Tap to edit");
        tapToEdit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        tapToEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
        tapToEdit.addClickListener(l -> {
            imageUploader.getElement().executeJs("this.shadowRoot.querySelector('input[type=file]').click();");
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(pictureAvatar);
        horizontalLayout.setAlignItems(Alignment.CENTER); // Vertical alignment
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Horizontal alignment
        horizontalLayout.setWidthFull();

        entityForm.add(horizontalLayout, imageUploader);

        setColspan(imageUploader, 2);
        setColspan(horizontalLayout, 2);

        Span gap = new Span();
        gap.getElement().getStyle().set("height", "1rem");

        entityForm.add(gap, tapToEdit, firstName, lastName, username, email);
        expand(firstName, lastName, username, email, tapToEdit, gap);

    }

    @Override
    protected void decorateDialog(GxDialog dialog) {
        super.decorateDialog(dialog);
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);
        dialog.setTop("3rem");
        dialog.setLeft("calc(100% - 20rem)");
    }

    @Override
    protected void bindFields(Binder<GxAuthenticatedUser> dataBinder) {
        dataBinder.forMemberField(username).asRequired();
        dataBinder.forMemberField(email).withValidator(new EmailValidator("Must be a valid email address"));
    }

    @Override
    protected void preBinding(GxAuthenticatedUser entity) {
        if (entity.getProfilePhoto() != null) {
            DownloadHandler dh = new InputStreamDownloadHandler(new InputStreamDownloadCallback() {

                @Override
                public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
                    return new DownloadResponse(new java.io.ByteArrayInputStream(entity.getProfilePhoto()),
                            "Profile Picture", null,
                            entity.getProfilePhoto().length);
                }

            });
            pictureAvatar.setImageHandler(dh);
        } else {
            pictureAvatar.setImageHandler(null);
            pictureAvatar.setName(entity.getFirstNameLastName());
        }
    }

    @Override
    protected String formTitle() {
        return "Profile Settings";
    }

    @Override
    protected String dialogHeight() {
        return "80%";
    }

    @Override
    protected String dialogWidth() {
        return "20rem";
    }

    @Override
    protected void customizeDismissButton(Button dismissButton) {
        dismissButton.setVisible(false);
    }

}
