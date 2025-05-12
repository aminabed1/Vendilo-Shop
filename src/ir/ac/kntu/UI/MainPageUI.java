package ir.ac.kntu.UI;

import ir.ac.kntu.Customer;
import ir.ac.kntu.Person;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainPageUI extends Application {

    private boolean isPopupPinned = false;
    private VBox accountPopupContent;
    private boolean isAccountPopupVisible = false;
    private Customer currentCustomer;

    public MainPageUI(Person currentCustomer) {
        this.currentCustomer = (Customer) currentCustomer;
    }

    @Override
    public void start(Stage primaryStage) {

        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #f5e6cc;");

        HBox header = new HBox(20);
        header.setStyle("-fx-background-color: #f1dbc1; -fx-padding: 10;");
        header.setAlignment(Pos.CENTER_LEFT);

        Image logoImage = new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(30);
        logoView.setPreserveRatio(true);

        Image userImage = new Image("file:F:/advancedProgramming/untitled/iamges/user.jpg");
        ImageView userView = new ImageView(userImage);
        userView.setFitHeight(33);
        userView.setPreserveRatio(true);

        Label title = new Label("VENDILO");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #b63227;");

        Label categoryButton = new Label("Product Category ▾");
        categoryButton.setStyle("-fx-font-size: 20px; -fx-text-fill: #b63227; -fx-font-weight: bold;");

        Label account = new Label("");
        account.setStyle("-fx-font-size:12px; -fx-text-fill: #b63227; -fx-font-weight: bold;");
        account.setGraphic(userView);
        account.setContentDisplay(ContentDisplay.LEFT);

        Region spacer = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer2.setMinWidth(10);
        header.getChildren().addAll(categoryButton, spacer2, userView, account, spacer, title, logoView);

        VBox popupContent = new VBox(10);
        popupContent.setStyle("-fx-background-color:#f5e6cc; -fx-border-color: lightgray; -fx-padding: 10;");
        popupContent.setPrefSize(200, 150);
        popupContent.setVisible(false);

        for (int i = 0; i < 5; i++) {
            Label item = new Label("Category " + (i + 1));
            popupContent.getChildren().add(item);
        }

        accountPopupContent = new VBox(10);
        accountPopupContent.setStyle("-fx-background-color:#f5e6cc; -fx-border-color: lightgray; -fx-padding: 10;");
        accountPopupContent.setPrefSize(200, 150);
        accountPopupContent.setVisible(false);

        Button accountButton = new Button(currentCustomer.getName() + " " + currentCustomer.getSurname());
        accountButton.setStyle("-fx-font-size: 12; -fx-background-color: #d89566; -fx-padding: 8;");

        Button editInfoButton = new Button("Edit Information");
        editInfoButton.setStyle("-fx-font-size: 12; -fx-background-color: #d89566; -fx-padding: 8;");

        Button odredButton = new Button("Orders Details");
        odredButton.setStyle("-fx-font-size: 12; -fx-background-color: #d89566; -fx-padding: 8;");

        Button logOutButton = new Button("Log Out");
        logOutButton.setStyle("-fx-font-size: 12; -fx-background-color: #d89566; -fx-padding: 8;");

        accountPopupContent.getChildren().addAll(accountButton, editInfoButton, odredButton, logOutButton);


        root.getChildren().addAll(header, popupContent, accountPopupContent);
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        categoryButton.setOnMouseClicked(e -> {
            isPopupPinned = !isPopupPinned;
            if (isPopupPinned) {
                Bounds bounds = categoryButton.localToScene(categoryButton.getBoundsInLocal());
                double x = bounds.getMinX();
                popupContent.setLayoutX(x);
                popupContent.setLayoutY(header.getHeight() + 5);
                popupContent.setVisible(true);
            } else {
                popupContent.setVisible(false);
            }
        });

        categoryButton.setOnMouseEntered(e -> {
            if (!isPopupPinned) {
                Bounds bounds = categoryButton.localToScene(categoryButton.getBoundsInLocal());
                double x = bounds.getMinX();
                popupContent.setLayoutX(x);
                popupContent.setLayoutY(header.getHeight() + 5);
                popupContent.setVisible(true);
            }
        });

        categoryButton.setOnMouseExited(e -> {
            if (!isPopupPinned) {
                popupContent.setVisible(false);
            }
        });

        account.setOnMouseClicked(e -> {
            if (isAccountPopupVisible) {
                accountPopupContent.setVisible(false);
            } else {
                accountPopupContent.setLayoutX(account.getLayoutX());
                accountPopupContent.setLayoutY(account.getLayoutY() + 30);
                accountPopupContent.setVisible(true);
            }
            isAccountPopupVisible = !isAccountPopupVisible;
        });

        account.setOnMouseEntered(e -> {
            if (!isAccountPopupVisible) {
                accountPopupContent.setLayoutX(account.getLayoutX());
                accountPopupContent.setLayoutY(account.getLayoutY() + 30);
                accountPopupContent.setVisible(true);
            }
        });

        account.setOnMouseExited(e -> {
            if (!isAccountPopupVisible) {
                accountPopupContent.setVisible(false);
            }
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Vendilo Main Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}