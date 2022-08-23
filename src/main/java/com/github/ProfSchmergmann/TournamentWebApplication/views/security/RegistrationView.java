package com.github.ProfSchmergmann.TournamentWebApplication.views.security;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.user.User;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.user.User.Role;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.user.UserService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.GymView;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Register | Tournament")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

  private final UserService userService;

  public RegistrationView(@Autowired UserService userService) {
    this.userService = userService;
    var form = new RegistrationForm();
    this.setHorizontalComponentAlignment(Alignment.CENTER, form);
    this.add(form);
    var binder = new RegistrationFormBinder(form, userService);
    binder.addBindingAndValidation();
  }

  public static class RegistrationForm extends FormLayout {

    private final TextField email;
    private final Span errorMessageField;
    private final TextField firstName;
    private final TextField lastName;
    private final PasswordField passwordConfirm;
    private final PasswordField passwordField;
    private final Button submitButton;
    private final H3 title;

    public RegistrationForm() {
      this.title = new H3("Signup form");
      this.firstName = new TextField("First name");
      this.lastName = new TextField("Last name");
      this.email = new TextField();
      this.email.setLabel("Email address");
      this.email.getElement().setAttribute("name", "email");
      this.email.setErrorMessage("Please enter a valid email address");
      this.email.setClearButtonVisible(true);
      this.passwordField = new PasswordField("Password");
      this.passwordField.setHelperText(
          "A password must be at least 8 characters. It has to have at least one letter and one digit.");
      this.passwordField.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
      this.passwordField.setErrorMessage("Not a valid password");
      this.passwordConfirm = new PasswordField("Confirm password");

      this.setRequiredIndicatorVisible(this.firstName,
          this.lastName,
          this.email,
          this.passwordField,
          this.passwordConfirm);

      this.errorMessageField = new Span();

      this.submitButton = new Button("Join the community");
      this.submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

      var verticalLayout = new VerticalLayout(this.title,
          this.firstName,
          this.lastName,
          this.email,
          this.passwordField,
          this.passwordConfirm,
          this.errorMessageField,
          this.submitButton);
      verticalLayout.setPadding(true);

      this.add(verticalLayout);

      // Max width of the Form
      this.setMaxWidth("500px");
    }

    public Span getErrorMessageField() {
      return this.errorMessageField;
    }

    public PasswordField getPasswordConfirmField() {
      return this.passwordConfirm;
    }

    public PasswordField getPasswordField() {
      return this.passwordField;
    }

    public Button getSubmitButton() {
      return this.submitButton;
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
      Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

  }

  public static class RegistrationFormBinder {

    private final RegistrationForm registrationForm;
    private final UserService userService;
    /**
     * Flag for disabling first run for password validation
     */
    private boolean enablePasswordValidation;

    public RegistrationFormBinder(RegistrationForm registrationForm,
        UserService userService) {
      this.registrationForm = registrationForm;
      this.userService = userService;
    }

    /**
     * Method to add the data binding and validation logics to the registration form
     */
    public void addBindingAndValidation() {
      var binder = new BeanValidationBinder<>(User.class);
      binder.bindInstanceFields(this.registrationForm);
      binder.forField(this.registrationForm.getPasswordField())
          .withValidator(this::passwordValidator).bind("password");

      this.registrationForm.getPasswordConfirmField().addValueChangeListener(e -> {
        this.enablePasswordValidation = true;
        binder.validate();
      });

      binder.setStatusLabel(this.registrationForm.getErrorMessageField());

      this.registrationForm.getSubmitButton().addClickListener(event -> {
        try {
          var userBean = new User();

          binder.writeBean(userBean);
          userBean.setUserName(userBean.getEmail());
          userBean.setRole(Role.USER);
          var signedUpUser = this.userService.signUpUser(userBean);
          this.showSuccess(signedUpUser);
        } catch (ValidationException exception) {
          // validation errors are already visible for each field,
          // and bean-level errors are shown in the status label.
          // We could show additional messages here if we want, do logging, etc.
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }

    private ValidationResult passwordValidator(String pass1, ValueContext ctx) {

      if (pass1 == null || pass1.length() < 8) {
        return ValidationResult.error("Password should be at least 8 characters long");
      }

      if (!this.enablePasswordValidation) {
        this.enablePasswordValidation = true;
        return ValidationResult.ok();
      }

      var pass2 = this.registrationForm.getPasswordConfirmField().getValue();

      if (pass1.equals(pass2)) {
        return ValidationResult.ok();
      }

      return ValidationResult.error("Passwords do not match");
    }

    /**
     * We call this method when form submission has succeeded
     */
    private void showSuccess(User user) {
      Notification notification =
          Notification.show("Data saved, welcome " + user.getFirstName());
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

      UI.getCurrent().navigate(GymView.class);
    }
  }
}