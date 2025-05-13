package ir.ac.kntu.UI;

import ir.ac.kntu.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.*;

public class CreateAccountUI extends Application {
    private List<String> errorList = new ArrayList<>();
    private String selectedRole = "Customer";
    private VBox form;

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef);");

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40));
        formContainer.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label title = new Label("CREATE ACCOUNT");
        title.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        title.setPadding(new Insets(0, 0, 20, 0));

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton customerTab = createRoleTab("Customer", roleGroup);
        ToggleButton sellerTab = createRoleTab("Seller", roleGroup);
        customerTab.setSelected(true);

        HBox tabs = new HBox(15, customerTab, sellerTab);
        tabs.setAlignment(Pos.CENTER);
        tabs.setPadding(new Insets(0, 0, 20, 0));

        TextField name = createFormField("Name");
        TextField surname = createFormField("Surname");
        TextField phoneNumber = createFormField("Phone Number");
        TextField email = createFormField("Email");
        TextField username = createFormField("Username");
        PasswordField password = createPasswordField("Password");
        TextField shopName = createFormField("Shop Name");
        shopName.setVisible(false);

        Button createAccountBtn = new Button("CREATE ACCOUNT");
        createAccountBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;");
        createAccountBtn.setMaxWidth(350);
        createAccountBtn.setOnMouseEntered(e -> createAccountBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;"));
        createAccountBtn.setOnMouseExited(e -> createAccountBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;"));

        form = new VBox(15, title, tabs, name, surname, phoneNumber, email,
                username, password, shopName, createAccountBtn);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);

        formContainer.getChildren().add(form);

        StackPane imagePane = new StackPane();
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setStyle("-fx-background-color: #ecf0f1;");

        ImageView logoView = new ImageView(new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg"));
        logoView.setFitWidth(500);
        logoView.setPreserveRatio(true);
        logoView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        imagePane.getChildren().add(logoView);

        root.getChildren().addAll(formContainer, imagePane);
        HBox.setHgrow(formContainer, Priority.ALWAYS);
        HBox.setHgrow(imagePane, Priority.ALWAYS);

        createAccountBtn.setOnAction(e -> {
            String nameText = name.getText();
            String surnameText = surname.getText();
            String phoneNumberText = phoneNumber.getText();
            String emailText = email.getText();
            String usernameText = username.getText();
            String passwordText = password.getText();
            String shopNameText = shopName.getText();

            if (nameText.isEmpty() || surnameText.isEmpty() || phoneNumberText.isEmpty() ||
                    emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty() ||
                    (selectedRole.equals("Seller") && (shopNameText.isEmpty()))) {
                showFloatingMessage("Please fill in all required fields.", true, primaryStage, null);
                return;
            }

            if (CreateAccountPage.receiveAccountInfo(phoneNumberText, emailText, selectedRole)) {
                showFloatingMessage("Account creation failed.\nEmail or phone number already in use.", true, primaryStage, null);
                return;
            }

            if (selectedRole.equals("Customer")) {
                Person person = new Customer(nameText, surnameText, phoneNumberText, emailText,
                        usernameText, passwordText);
                errorList = person.getErrorList();
            } else {
                Person person = new Seller(nameText, surnameText, phoneNumberText, emailText,
                        usernameText, passwordText, shopNameText);
                errorList = person.getErrorList();
            }

            if (!errorList.isEmpty()) {
                showFloatingMessage(String.join("\n", errorList), true, primaryStage, null);
                return;
            }

            showFloatingMessage("Account created successfully as " + selectedRole, false, primaryStage, () -> {
                new LoginPageUI().start(new Stage());
                ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
            });
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vendilo - Create Account");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    private TextField createFormField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; " +
                "-fx-padding: 12; -fx-font-size: 14px;");
        field.setMaxWidth(350);
        return field;
    }

    private PasswordField createPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; " +
                "-fx-padding: 12; -fx-font-size: 14px;");
        field.setMaxWidth(350);
        return field;
    }

    private ToggleButton createRoleTab(String role, ToggleGroup group) {
        ToggleButton tab = new ToggleButton(role);
        tab.setToggleGroup(group);
        tab.setPrefWidth(120);
        tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");

        tab.setOnAction(e -> {
            selectedRole = role;
            updateTabStyle(group);
            toggleSellerFields(role);
        });

        tab.setOnMouseEntered(e -> {
            if (!tab.isSelected()) {
                tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #dfe6e9; -fx-text-fill: #7f8c8d;");
            }
        });

        tab.setOnMouseExited(e -> {
            if (!tab.isSelected()) {
                tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");
            }
        });

        return tab;
    }

    private void toggleSellerFields(String role) {
        for (Node node : form.getChildren()) {
            if (node instanceof TextField) {
                TextField field = (TextField) node;
                if (field.getPromptText().equals("Agency Code") || field.getPromptText().equals("Shop Name")) {
                    field.setVisible(role.equals("Seller"));
                    field.setManaged(role.equals("Seller"));
                }
            }
        }
    }

    private void updateTabStyle(ToggleGroup group) {
        for (Toggle toggle : group.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8; " +
                        "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 15; " +
                        "-fx-text-fill: white;");
            } else {
                btn.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");
            }
        }
    }

    private void showFloatingMessage(String message, boolean isError, Stage ownerStage, Runnable afterMessage) {
        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-background-color: " + (isError ? "rgba(231,76,60,0.9);" : "rgba(46,204,113,0.9);") +
                        " -fx-text-fill: white; -fx-padding: 12 20; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-font-size: 14px;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        StackPane content = new StackPane(messageLabel);
        content.setStyle("-fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");
        popup.getContent().add(content);

        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.show(ownerStage,
                ownerStage.getX() + ownerStage.getWidth()/2 - 150,
                ownerStage.getY() + 50);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            popup.hide();
            if (afterMessage != null) {
                afterMessage.run();
            }
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}