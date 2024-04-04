package ui.security;

import domain.error.CertificateNotFoundException;
import domain.error.PrivateKeyNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

//@Singleton
public class KeyRetrieverTool {

    //RETRIEVE PUBLIC KEY
    public PublicKey retrievePublicKey(String keyStoreFileName, String username, char[] keyStorePassword) throws Exception {

        String userAlias = username + ".public";

        KeyStore keyStore = loadKeyStore(keyStoreFileName, keyStorePassword);
        Certificate userCertificate = keyStore.getCertificate(userAlias);
        if (userCertificate == null) {
            throw new CertificateNotFoundException("Certificate not found for the user: " + username);
        }
        return userCertificate.getPublicKey();
    }

    //RETRIEVE PRIVATE KEY
    public PrivateKey retrievePrivateKey(String keyStoreFileName, String username, char[] keyStorePassword, char[] pkUserPassword) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {

        String userAlias = username + ".private";

        KeyStore keyStore = loadKeyStore(keyStoreFileName, keyStorePassword);
        Key key = keyStore.getKey(userAlias, pkUserPassword);
        if (!(key instanceof PrivateKey)) {
            throw new PrivateKeyNotFoundException("Private key was not found for the user: " + username);
        }
        return (PrivateKey) key;
    }

    //LOAD KEYSTORE
    private KeyStore loadKeyStore(
            String fileName,
            char[] password
            //admin's password
    ) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(fileName), password);
        return keyStore;

    }
}
