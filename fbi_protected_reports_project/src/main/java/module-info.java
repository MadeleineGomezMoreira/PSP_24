module fbi_protected_reports_project {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires lombok;
    requires org.apache.logging.log4j;
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.util;
    requires jakarta.inject;
    requires jakarta.cdi;
    requires io.vavr;
    requires io.reactivex.rxjava3;
    requires org.pdfsam.rxjavafx;
    requires java.logging;

    exports ui.fx.main to javafx.graphics;
    exports ui.security;
    exports ui.fx.screens.common;
    exports ui.fx.screens.principal;
    exports ui.fx.screens.login;
    exports ui.fx.screens.welcome;

    opens ui.security;
    opens ui.fx.screens.principal;
    opens ui.fx.screens.login;
    opens ui.fx.screens.welcome;
    opens ui.fx.screens.common;
    opens ui.fx.main;


}