module fbi_protected_reports_project {

    requires org.apache.logging.log4j;
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.util;
    requires com.google.common;
    requires javafx.graphics;
    requires javafx.controls;
    requires spring.jdbc;
    requires javafx.fxml;
    requires MaterialFX;
    requires lombok;
    requires spring.tx;
    requires jakarta.inject;
    requires jakarta.cdi;
    requires io.vavr;
    requires io.reactivex.rxjava3;
    requires org.pdfsam.rxjavafx;
    requires java.logging;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires jakarta.persistence;

    exports ui.fx.main to javafx.graphics;
    exports ui.security;
    exports ui.fx.screens.common;
    exports ui.fx.screens.principal;
    exports ui.fx.screens.login;
    exports ui.fx.screens.welcome;
    exports domain.error;
    exports domain.model;
    exports data.model;

    opens ui.security;
    opens ui.fx.screens.principal;
    opens ui.fx.screens.login;
    opens ui.fx.screens.welcome;
    opens ui.fx.screens.common;
    opens ui.fx.main;
    opens domain.error;
    opens domain.model;
    opens data.model;


}