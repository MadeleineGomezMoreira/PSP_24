package ui.fx.screens.principal;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import ui.fx.screens.common.BaseScreenController;
import ui.fx.screens.common.ScreenConstants;
import ui.fx.screens.common.ScreensThings;

import java.io.IOException;
import java.util.Optional;



@Log4j2
public class PrincipalController {

    @FXML
    private MenuBar principalMenu;

    @FXML
    public BorderPane root;

    Instance<Object> instance;
    private Stage primaryStage;
    private final Alert alert;
    @Getter
    @Setter
    private String username;


    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    //SCREEN LOADING/CHANGING

    public void initialize() {
//        principalMenu.setVisible(false);
//        loadScreen(ScreensThings.LOGIN_SCREEN);
        principalMenu.setVisible(true);
        loadScreen(ScreensThings.WELCOME_SCREEN);
    }

    private void loadScreen(ScreensThings screen) {
        screenChange(loadScreen(screen.getRoute()));
    }

    private Pane loadScreen(String route) {
        Pane screenPane = null;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            screenPane = fxmlLoader.load(getClass().getResourceAsStream(route));
            BaseScreenController screenController = fxmlLoader.getController();
            screenController.setPrincipalController(this);
            screenController.loadedPrincipal();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return screenPane;
    }

    private void screenChange(Pane newScreen) {
        root.setCenter(newScreen);
    }


    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    //MENU ACTIONS

    @FXML
    private void menuClick(ActionEvent actionEvent) {

        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case ScreenConstants.MENU_ITEM_LOGOUT -> logout();
            case ScreenConstants.MENU_ITEM_EXIT -> exit();
            case ScreenConstants.MENU_ITEM_CUSTOMER_LIST -> loadScreen(ScreensThings.CUSTOMER_LIST_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_ADD -> loadScreen(ScreensThings.CUSTOMER_ADD_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_UPDATE -> loadScreen(ScreensThings.CUSTOMER_UPDATE_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_DELETE -> loadScreen(ScreensThings.CUSTOMER_DELETE_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_LIST -> loadScreen(ScreensThings.ORDER_LIST_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_ADD -> loadScreen(ScreensThings.ORDER_ADD_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_UPDATE -> loadScreen(ScreensThings.ORDER_UPDATE_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_DELETE -> loadScreen(ScreensThings.ORDER_DELETE_SCREEN);
            default -> loadScreen(ScreensThings.WELCOME_SCREEN);
        }
    }

    public void exit() {
        primaryStage.close();
        Platform.exit();
    }

    //ALERTS

    public void showErrorAlert(String message) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }

    public void showInfoAlert(String message) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }


    public boolean showConfirmationAlert(String message) {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);

        ButtonType confirmButtonType = new ButtonType(ScreenConstants.OK, ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirmButtonType, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == confirmButtonType;
    }

    public void showWarningAlert(String message) {
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }

    //LOGIN & LOGOUT

    public void login() {
        principalMenu.setVisible(true);
        loadScreen(ScreensThings.WELCOME_SCREEN);
    }

    public void logout() {
        principalMenu.setVisible(false);
        loadScreen(ScreensThings.LOGIN_SCREEN);
    }
}
