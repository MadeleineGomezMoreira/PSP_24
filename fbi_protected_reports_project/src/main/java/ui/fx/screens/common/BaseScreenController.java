package ui.fx.screens.common;


import ui.fx.screens.principal.PrincipalController;

public class BaseScreenController {

    private PrincipalController principalController;

    public PrincipalController getPrincipalController() {
        return principalController;
    }

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void loadedPrincipal() {

    }
}
