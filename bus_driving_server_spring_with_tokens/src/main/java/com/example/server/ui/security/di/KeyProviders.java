package com.example.server.ui.security.di;

import com.example.server.common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class KeyProviders {

    @Value("${keystore.password}")
    private String keystorePass;

    @Value("${keystore.path}")
    private String keystorePath;

    @Value("${keystore.alias}")
    private String keyStoreAlias;

    @Bean(name = "getPublicKey")
    public PublicKey getPublicKey(KeyStore keys) throws KeyStoreException {
        X509Certificate certificate = (X509Certificate) keys.getCertificate(keyStoreAlias);
        return certificate.getPublicKey();
    }

    @Bean(name = "getPrivateKey")
    public PrivateKey getPrivateKey(KeyStore keys) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(keystorePass.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keys.getEntry(keyStoreAlias, passwordProtection);
        return privateKeyEntry.getPrivateKey();
    }

    //this method cannot be private cause if not Spring cannot manage it as a Bean
    @Bean
    public KeyStore getKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ksLoad = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
        ksLoad.load(new FileInputStream(keystorePath), keystorePass.toCharArray());
        return ksLoad;
    }
}
