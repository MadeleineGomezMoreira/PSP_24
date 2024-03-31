package org.example.security_project_encripting.methods;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.example.security_project_encripting.domain.model.error.InvalidPrivateKeyException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;

public class SaveUserKeys {

    //LOAD KEYSTORE
    private KeyStore loadKeyStore(
            String fileName,
            char[] password
            //admin's password
    ) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore kyst = KeyStore.getInstance("PKCS12");
        kyst.load(new FileInputStream(fileName), password);
        return kyst;

    }

    //SAVE USER PRIVATE KEY + CERT TO KEYSTORE
    public void saveUserToKeyStore(String keyStoreFileName, String userPass, String userName, String serverPass) throws Exception {
        String aliasPublic = "public";
        String aliasPrivate = "private";

        String userSalt = generateRandomSalt();
        String adminSalt = generateRandomSalt();

        //TODO: save this salt into the DB

        char[] adminPassHashed = hashPassword(serverPass, adminSalt);
        char[] userPassHashed = hashPassword(userPass, userSalt);

        String adminPassHashedString = new String(adminPassHashed);
        String userPassHashedString = new String(userPassHashed);

        //TODO: save the hashed pw into the DB

        KeyStore keyStore = loadKeyStore(keyStoreFileName, adminPassHashed);

        KeyPair keyPair = generateRandomKeyPair();

        //We generate the certificate
        Certificate certificate = generateCertificate(keyPair, userName, adminSalt, serverPass);

        //we generate the private key
        PrivateKey privateKey = keyPair.getPrivate();

        //we store both into the keyStore
        keyStore.setCertificateEntry(aliasPublic, certificate);
        keyStore.setKeyEntry(aliasPrivate, privateKey, userPassHashed, new Certificate[]{certificate});

    }

    //GET SERVER PRIVATE KEY
    //The user's public key certificate must be done utilizing the server's private key
    private PrivateKey getServerPrivateKey(String keystoreFileName, char[] adminPassword, String adminPublicKeyAlias) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        // Load the keystore
        KeyStore keyStore = loadKeyStore(keystoreFileName, adminPassword);

        // Get the server's private key
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(adminPublicKeyAlias, adminPassword);
        if (privateKey == null) {
            //we throw a dedicated exception
            throw new InvalidPrivateKeyException("Server's private key not found in the keystore.");
        }

        return privateKey;
    }

    //GENERATE KEYPAIR
    private KeyPair generateRandomKeyPair() throws NoSuchAlgorithmException {

        //we create a new instance of the KeyPairGenerator using RSA algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        //we initialize our generator with a key-size of 2048 bits (larger size == safer) and a SecureRandom object to make it random
        keyPairGenerator.initialize(2048, new SecureRandom());

        //we finally generate both keys
        return keyPairGenerator.generateKeyPair();
    }

    //GENERATE CERTIFICATE
    private X509Certificate generateCertificate(KeyPair keyPair, String userName, String adminSalt, String adminPass) throws NoSuchAlgorithmException, InvalidKeySpecException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, OperatorCreationException {

        String keyStoreFileName = "keystore.pfx";
        char[] adminPassword = hashPassword(adminPass, adminSalt);
        String adminPublicKeyAlias = "Made";


        //we extract each key from the keyPair
        PrivateKey privateKey = getServerPrivateKey(keyStoreFileName, adminPassword, adminPublicKeyAlias);
        PublicKey publicKey = keyPair.getPublic();

        //WE SET THE KEY PARAMETERS

        //we create a name for the certificate owner and issuer
        X500Name owner = new X500Name("CN=" + userName);
        X500Name issuer = new X500Name("CN=Project");

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

    //GENERATE KEYSTORE PASSWORD
    private char[] hashPassword(String password, String saltString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // First we must define the PBKDF2 parameters
        int iterations = 10000;
        int length = 256; // This is length in bits
        byte[] salt = stringSaltToByteArray(saltString);

        // Now we hash the password using PBKDF2 with HMAC SHA-256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySettings = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        byte[] hashBytes = factory.generateSecret(keySettings).getEncoded();

        // We then convert the byte array into a char array using Base64 encoding
        return Base64.getEncoder().encodeToString(hashBytes).toCharArray();
    }

    private byte[] stringSaltToByteArray(String saltString) {
        // Decode the salt string back to its original byte array form
        byte[] salt = Base64.getDecoder().decode(saltString);
        return salt;
    }

    private String generateRandomSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
