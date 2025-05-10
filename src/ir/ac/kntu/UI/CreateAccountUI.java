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

import java.util.List;

public class CreateAccountUI extends Application {
    private List<String> errorList;
    private String selectedRole = "Customer";
    private VBox form;

    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("VENDILO");
        title.setFont(Font.font("Cinzel", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#b63227"));

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton customerTab = createRoleTab("Customer", roleGroup);
        ToggleButton sellerTab = createRoleTab("Seller", roleGroup);
        customerTab.setSelected(true);

        HBox tabs = new HBox(customerTab, sellerTab);
        tabs.setAlignment(Pos.CENTER);
        tabs.setSpacing(10);
        tabs.setPadding(new Insets(10));

        TextField name = new TextField(); name.setPromptText("Name");
        TextField surname = new TextField(); surname.setPromptText("Surname");
        TextField phoneNumber = new TextField(); phoneNumber.setPromptText("Phone Number");
        TextField email = new TextField(); email.setPromptText("Email");
        TextField username = new TextField(); username.setPromptText("Username");
        PasswordField password = new PasswordField(); password.setPromptText("Password");
        TextField province = new TextField(); province.setPromptText("Province");
        TextField address = new TextField(); address.setPromptText("Address");

        TextField agencyCode = new TextField(); agencyCode.setPromptText("Agency Code"); agencyCode.setVisible(false);
        TextField shopName = new TextField(); shopName.setPromptText("Shop Name"); shopName.setVisible(false);

        Button createAccountBtn = new Button("Create Account");
        createAccountBtn.setStyle("-fx-background-color: #d70041; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        createAccountBtn.setOnAction(e -> {
            String nameText = name.getText();
            String surnameText = surname.getText();
            String phoneNumberText = phoneNumber.getText();
            String emailText = email.getText();
            String usernameText = username.getText();
            String passwordText = password.getText();
            String provinceText = province.getText();
            String addressText = address.getText();
            String agencyCodeText = agencyCode.getText();
            String shopNameText = shopName.getText();

            if (nameText.isEmpty() || surnameText.isEmpty() || phoneNumberText.isEmpty() || emailText.isEmpty() ||
                    usernameText.isEmpty() || passwordText.isEmpty() ||
                    provinceText.isEmpty() || addressText.isEmpty() ||
                    (selectedRole.equals("Seller") && (agencyCodeText.isEmpty() || shopNameText.isEmpty()))) {
                showFloatingMessage("Please fill in all fields.", true, primaryStage, null);
                return;
            }

            if (CreateAccountPage.receiveAccountInfo(phoneNumberText, emailText, selectedRole)) {
                showFloatingMessage("Account creation failed,\nemail or phone number is already in use", true, primaryStage, null);
                return;
            }

            if (selectedRole.equals("Customer")) {
                Customer person = new Customer(nameText, surnameText, phoneNumberText, emailText, usernameText,
                        passwordText, provinceText, addressText);
                errorList = person.getErrorList();
            } else {
                Seller person = new Seller(nameText, surnameText, phoneNumberText, emailText, usernameText,
                        passwordText, agencyCodeText, shopNameText, provinceText, addressText);
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

        form = new VBox(15, title, tabs, name, surname, phoneNumber, email, username, password,
                province, address, agencyCode, shopName, createAccountBtn);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(60, 40, 60, 60));
        form.setMaxWidth(400);

        StackPane leftPane = new StackPane(form);
        leftPane.setStyle("-fx-background-color: #f5e6cc;");

        ImageView logoView = new ImageView(new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg"));
        logoView.setFitWidth(400);
        logoView.setPreserveRatio(true);
        StackPane rightPane = new StackPane(logoView);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setStyle("-fx-background-color: #f8ebdc;");
        rightPane.setPadding(new Insets(20));

        HBox root = new HBox(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 960, 600);
        primaryStage.setTitle("Create Account");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ToggleButton createRoleTab(String role, ToggleGroup group) {
        ToggleButton tab = new ToggleButton(role);
        tab.setToggleGroup(group);
        tab.setPrefWidth(120);
        tab.setStyle("-fx-background-radius: 10; -fx-font-weight: bold;");

        tab.setOnAction(e -> {
            selectedRole = role;
            updateTabStyle(group);
            toggleSellerFields(role);
        });

        return tab;
    }

    private void toggleSellerFields(String role) {
        for (Node node : form.getChildren()) {
            if (node instanceof TextField) {
                TextField field = (TextField) node;
                if (field.getPromptText().equals("Agency Code") || field.getPromptText().equals("Shop Name")) {
                    field.setVisible(role.equals("Seller"));
                }
            }
        }
    }

    private void updateTabStyle(ToggleGroup group) {
        for (Toggle toggle : group.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: #f1d9b5; -fx-background-radius: 10; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: #f5e6cc; -fx-background-radius: 10; -fx-font-weight: normal;");
            }
        }
    }

    private void showFloatingMessage(String message, boolean isError, Stage ownerStage, Runnable afterMessage) {
        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-background-color: " + (isError ? "rgba(255,0,0,0.85);" : "rgba(0,128,0,0.85);") +
                        " -fx-text-fill: white; -fx-padding: 10; -fx-font-weight: bold; -fx-background-radius: 5;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        StackPane content = new StackPane(messageLabel);
        content.setStyle("-fx-background-radius: 5;");
        popup.getContent().add(content);

        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.show(ownerStage, ownerStage.getX() + 20, ownerStage.getY() + 20);

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
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