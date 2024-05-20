package com.example.server.ui.security.di;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class KeyStoreGenerator {

    public static void main(String[] args) {
        KeyStoreGenerator keyStoreManager = new KeyStoreGenerator();
        keyStoreManager.initializeKeyStore();
    }

    public void initializeKeyStore() {
        //we add the bouncyCastleProvider to our java security settings
        Security.addProvider(new BouncyCastleProvider());

        //T0DO: put these into constants somewhere
        String adminPasswordString = "admin";
        String fileName = "keystore.pfx";
        String aliasPublic = "admin.public";
        String aliasPrivate = "admin.private";

        try {
            KeyPair keyPair = generateRandomKeyPair();

            X509Certificate certificate = generateCertificate(keyPair);
            char[] password = adminPasswordString.toCharArray();
            PrivateKey privateKey = keyPair.getPrivate();

            createKeyStoreFileWithEntry(fileName, aliasPublic, aliasPrivate, privateKey, certificate, password);

            //now I can load my keyStore and use it
            KeyStore keyStore = loadKeyStore(fileName, password);

        } catch (Exception e) {
            System.out.println("There was an error: " + e);
        }
    }

    private void createKeyStoreFileWithEntry(
            String fileName, //the name should have the '.pfx' file-extension, as this indicates it is a PKCS12 keystore
            String aliasPublic,
            String aliasPrivate,
            PrivateKey privateKey,
            Certificate certificate,
            char[] password
    ) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {

        //I create a new instance of KeyStore using the standard PKCS12 format
        KeyStore kyst = KeyStore.getInstance("PKCS12");

        //with this, I initialize the keystore with contents
        kyst.load(null, null);

        //I add a certificate entry to the keystore (aliasPublic serves as a unique identifier for this entry whenever I want to access it)
        kyst.setCertificateEntry(aliasPublic, certificate);

        //now I add a key entry (private key + password + associated certificate)
        //'aliasPrivate' serves as a unique identifier for this entry
        //the password protects the private key entry
        kyst.setKeyEntry(aliasPrivate, privateKey, password, new Certificate[]{certificate});

        //now we shall save our KeyStore to File format in our project using FileOutputStream
        FileOutputStream fos = new FileOutputStream(fileName);

        //we store the contents of the keystore (along with its entries) to the output stream
        //we also associate the KeyStore with a password that will protect it
        kyst.store(fos, password);

        //we finally close the output stream
        fos.close();

        //Done!
    }

    private KeyStore loadKeyStore(
            String fileName,
            char[] password
    ) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore kyst = KeyStore.getInstance("PKCS12");
        kyst.load(new FileInputStream(fileName), password);
        return kyst;

    }

    private KeyPair generateRandomKeyPair() throws NoSuchAlgorithmException {

        //we create a new instance of the KeyPairGenerator using RSA algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        //we initialize our generator with a key-size of 2048 bits (larger size == safer) and a SecureRandom object to make it random
        keyPairGenerator.initialize(2048, new SecureRandom());

        //we finally generate both keys
        return keyPairGenerator.generateKeyPair();
    }

    private X509Certificate generateCertificate(KeyPair keyPair) throws OperatorCreationException, CertificateException {

        //we extract each key from the keyPair
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        //WE SET THE KEY PARAMETERS

        //we create a name for the certificate owner and issuer
        X500Name owner = new X500Name("CN=Server");
        X500Name issuer = new X500Name("CN=Server");

        //we set the certificate's serial number
        BigInteger serialNumber = BigInteger.valueOf(1);

        //we set the certificate's expedition date and expirationDate
        Date expeditionDate = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + 1000000); //about 16.7 minutes

        //public key information
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKey.getEncoded()));

        //then we create a certificate using X509v3CertificateBuilder
        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuer,
                serialNumber,
                expeditionDate,
                expirationDate,
                owner,
                publicKeyInfo
        );

        //now the certificate must be signed with the private key, so we build the content signer using SHA256WithRSAEncryption algorithm
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(privateKey);

        //then we build the X509CertificateHolder with the content signer using the certificate builder
        X509CertificateHolder certHolder = certBuilder.build(signer);

        //we then convert the certificate holder into a certificate using JcaX509CertificateConverter
        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certHolder);

        //and finally we return the created certificate
        return certificate;
    }
}
