package io.graphenee.core.flow.security;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.component.GxDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.vaadin.flow.GxAbstractEntityForm;

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

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");

        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory((metadata, data) -> {
            try {
                // Display image in Avatar
                StreamResource resource = new StreamResource(metadata.fileName(), () -> new java.io.ByteArrayInputStream(data));
                pictureAvatar.setImageResource(resource);

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
        pictureAvatar.getStyle().set("width", "130px");  // Set custom width
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
        gap.getElement().getStyle().set("height", "10px");

        entityForm.add(gap, tapToEdit, group("group0", firstName, lastName), username, email);
        setColspan(username, 2);
        setColspan(email, 2);
        setColspan(tapToEdit, 2);
        setColspan(gap, 2);
        expand("group0");

    }

    @Override
    protected void decorateDialog(GxDialog dialog) {
        super.decorateDialog(dialog);
        dialog.setTop("5%");
        dialog.setLeft("70%");
    }

    @Override
    protected void bindFields(Binder<GxAuthenticatedUser> dataBinder) {
        dataBinder.forMemberField(username).asRequired();
        dataBinder.forMemberField(email).withValidator(new EmailValidator("Must be a valid email address"));
    }

    @Override
    protected void preBinding(GxAuthenticatedUser entity) {
        if (entity.getProfilePhoto() != null) {
            StreamResource resource = new StreamResource("Profile Picture", () -> new java.io.ByteArrayInputStream(entity.getProfilePhoto()));
            pictureAvatar.setImageResource(resource);
        } else {
            pictureAvatar.setImageResource(null);
            pictureAvatar.setName(entity.getFirstNameLastName());
        }
    }

    @Override
    protected String formTitle() {
        return "Profile Settings";
    }

    @Override
    protected String dialogHeight() {
        return "60%";
    }

    @Override
    protected String dialogWidth() {
        return "30%";
    }
}
