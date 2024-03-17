package ui.fx.main;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.fx.screens.principal.PrincipalController;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainFX {
    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes @StartupScene Stage stage) throws IOException {
        try {
            ResourceBundle r = ResourceBundle.getBundle("/i18n/appStrings");

            fxmlLoader.setResources(r);
            Parent fxmlParent = fxmlLoader.load(getClass().getResourceAsStream("/fxml/principal.fxml"));
            PrincipalController controller = fxmlLoader.getController();
            controller.setStage(stage);
            stage.setTitle(r.getString("app.title"));
            stage.setScene(new Scene(fxmlParent));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

}
