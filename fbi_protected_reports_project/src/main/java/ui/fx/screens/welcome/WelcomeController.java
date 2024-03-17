package ui.fx.screens.welcome;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ui.fx.screens.common.BaseScreenController;
import ui.fx.screens.common.ScreenConstants;

public class WelcomeController extends BaseScreenController {

    @FXML
    private Text userWTextArea;


    @Override
    public void loadedPrincipal() {
        userWTextArea.setText("USER" + ScreenConstants.EXCLAMATION_MARK);
    }

}
