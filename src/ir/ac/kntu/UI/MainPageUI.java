package ir.ac.kntu.UI;

import ir.ac.kntu.Customer;
import ir.ac.kntu.Person;
import javafx.application.Application;
import javafx.geometry.Bounds;
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
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainPageUI extends Application {

    private VBox accountPopupContent;
    private VBox categoryPopupContent;
    private Customer currentPerson;

    public MainPageUI(Person currentPerson) {
        this.currentPerson = (Customer) currentPerson;
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");

        HBox header = createHeader();
        categoryPopupContent = categoryPopup();
        accountPopupContent = accountPopup();

        root.getChildren().addAll(header, categoryPopupContent, accountPopupContent);
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        VBox mainContent = mainContent();
        AnchorPane.setTopAnchor(mainContent, 80.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        root.getChildren().add(mainContent);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vendilo - Online Shopping");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public HBox createHeader() {
        HBox header = new HBox(20);
        header.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);

        Image logoImage = new Image(getClass().getResource("/image/logo.jpg").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        Image userImage = new Image(getClass().getResource("/image/user.jpg").toExternalForm());
        ImageView userView = new ImageView(userImage);
        userView.setFitHeight(36);
        userView.setFitWidth(36);
        userView.setPreserveRatio(true);
        userView.setStyle("-fx-background-radius: 50%; -fx-border-radius: 50%;");

        Label title = new Label("VENDILO");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        title.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_BOLD, 28));

        Button categoryButton = new Button("Product Categories ▼");
        categoryButton.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50; -fx-font-weight: bold; " +
                "-fx-background-color: transparent; -fx-padding: 8 15; -fx-border-radius: 5;");
        categoryButton.setOnMouseEntered(e -> categoryButton.setStyle("-fx-text-fill: #3498db; -fx-font-size: 16px; -fx-font-weight: bold;"));
        categoryButton.setOnMouseExited(e -> categoryButton.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-font-weight: bold;"));
        categoryButton.setOnAction(e -> toggleCategoryPopup(categoryButton));

        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setStyle("-fx-background-color: #f1f3f5; -fx-background-radius: 20; -fx-border-radius: 20; " +
                "-fx-padding: 8 15 8 35; -fx-font-size: 14px;");
        searchField.setPrefWidth(300);

        ImageView searchIcon = new ImageView(new Image(getClass().getResource("/image/search.jpg").toExternalForm()));
        searchIcon.setFitHeight(25);
        searchIcon.setFitWidth(25);
        StackPane searchIconWrapper = new StackPane(searchIcon);
        searchIconWrapper.setPadding(new Insets(0, 0, 0, 10));
        searchIconWrapper.setAlignment(Pos.CENTER_LEFT);

        StackPane searchContainer = new StackPane();
        searchContainer.getChildren().addAll(searchField, searchIconWrapper);
        StackPane.setAlignment(searchIconWrapper, Pos.CENTER_LEFT);

        Button accountButton = new Button(currentPerson.getName() + " " + currentPerson.getSurname());
        accountButton.setStyle("-fx-font-size:14px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        accountButton.setGraphic(userView);
        accountButton.setContentDisplay(ContentDisplay.RIGHT);
        accountButton.setGraphicTextGap(10);
        accountButton.setStyle("-fx-background-color: #f1f3f5; -fx-background-radius: 20; -fx-padding: 5 15 5 5;");
        accountButton.setOnAction(e -> toggleAccountPopup(accountButton));

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(categoryButton, leftSpacer, searchContainer, rightSpacer, accountButton);
        return header;
    }

    public VBox categoryPopup() {
        VBox popupContent = new VBox(5);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        popupContent.setPrefSize(220, 180);
        popupContent.setVisible(false);

        String[] categories = {"Digital Product", "Book"};
        for (String category : categories) {
            Label item = new Label(category);
            item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #2c3e50;");
            item.setMaxWidth(Double.MAX_VALUE);
            item.setOnMouseEntered(e -> item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #3498db; -fx-background-color: #f8f9fa;"));
            item.setOnMouseExited(e -> item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #2c3e50;"));
            popupContent.getChildren().add(item);
        }
        return popupContent;
    }

    public VBox accountPopup() {
        VBox popupContent = new VBox(5);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        popupContent.setPrefSize(200, 160);
        popupContent.setVisible(false);

        Label userInfo = new Label(currentPerson.getName() + "\n" + currentPerson.getSurname());
        userInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-padding: 0 0 10 0;");
        userInfo.setMaxWidth(Double.MAX_VALUE);

        Button infoButton = popupButton("Profile", "#3498db");
        Button ordersButton = popupButton("My Orders", "#2ecc71");
        Button cartButton = popupButton("Cart", "#2ec851");
        Button wishlistButton = popupButton("Wishlist", "#9b59b6");
        Button logOutButton = popupButton("Log Out", "#e74c3c");

        popupContent.getChildren().addAll(infoButton, ordersButton, cartButton, wishlistButton, logOutButton);
        return popupContent;
    }

    public VBox mainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Welcome to Vendilo, " + currentPerson.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        Label featuredLabel = new Label("Featured Products");
        featuredLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

        HBox productPlaceholders = new HBox(15);
        productPlaceholders.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            VBox productCard = productCard();
            productPlaceholders.getChildren().add(productCard);
        }

        mainContent.getChildren().addAll(welcomeLabel, featuredLabel, productPlaceholders);
        return mainContent;
    }

    public Button popupButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-color: " + color + "; -fx-padding: 8 15; -fx-background-radius: 5; " +
                "-fx-cursor: hand; -fx-alignment: center-left;");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-color: derive(" + color + ", -10%); " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-color: " + color + "; " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        return button;
    }

    public void toggleCategoryPopup(Button categoryButton) {
        Bounds bounds = categoryButton.localToScene(categoryButton.getBoundsInLocal());
        categoryPopupContent.setLayoutX(bounds.getMinX());
        categoryPopupContent.setLayoutY(bounds.getMaxY() + 5);
        categoryPopupContent.setVisible(!categoryPopupContent.isVisible());

        if (accountPopupContent.isVisible()) {
            accountPopupContent.setVisible(false);
        }
    }

    public void toggleAccountPopup(Button accountButton) {
        Bounds bounds = accountButton.localToScene(accountButton.getBoundsInLocal());
        accountPopupContent.setLayoutX(bounds.getMinX() - accountPopupContent.getPrefWidth() + accountButton.getWidth());
        accountPopupContent.setLayoutY(bounds.getMaxY() + 5);
        accountPopupContent.setVisible(!accountPopupContent.isVisible());

        if (categoryPopupContent.isVisible()) {
            categoryPopupContent.setVisible(false);
        }
    }

    public VBox productCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        card.setPrefSize(200, 250);
        card.setAlignment(Pos.TOP_CENTER);

        Rectangle imagePlaceholder = new Rectangle(150, 150);
        imagePlaceholder.setFill(Color.LIGHTGRAY);
        imagePlaceholder.setArcWidth(10);
        imagePlaceholder.setArcHeight(10);

        Label name = new Label("Product Name");
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label price = new Label("$99.99");
        price.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");

        Button addToCart = new Button("Add to Cart");
        addToCart.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 5; -fx-padding: 5 15;");

        card.getChildren().addAll(imagePlaceholder, name, price, addToCart);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}