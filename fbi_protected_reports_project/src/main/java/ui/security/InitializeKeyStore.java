package ui.security;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.util.Date;

public class InitializeKeyStore {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        String fileName = "keystore.pfx";
        String aliasPublic = "public";
        String aliasPrivate = "private";
        String passwordString = "12345";

        try {
            KeyPair keyPair = generateRandomKeyPair();

            X509Certificate certificate = generateCertificate(keyPair);
            String hashedPassword = hashPassword(passwordString);
            char[] password = hashedPassword.toCharArray();
            PrivateKey privateKey = keyPair.getPrivate();

            createKeyStoreFileWithEntry(fileName, aliasPublic, aliasPrivate, privateKey, certificate, password);

            //now I can load my keyStore and use it
            KeyStore kyst = loadKeyStore(fileName, password);

        } catch (Exception e) {
            System.out.println("There was an error: " + e);
        }
    }

    private static void createKeyStoreFileWithEntry(
            String fileName,
            String aliasPublic,
            String aliasPrivate,
            PrivateKey privateKey,
            Certificate certificate,
            char[] password
    ) throws Exception {

        KeyStore kyst = KeyStore.getInstance("PKCS12");
        kyst.load(null, null);
        kyst.setCertificateEntry(aliasPublic, certificate);
        kyst.setKeyEntry(aliasPrivate, privateKey, password, new Certificate[]{certificate});

        FileOutputStream fos = new FileOutputStream(fileName);
        kyst.store(fos, password);
        fos.close();

        //Done!
    }

    private static KeyStore loadKeyStore(
            String fileName,
            char[] password
    ) throws Exception {

        KeyStore kyst = KeyStore.getInstance("PKCS12");
        kyst.load(new FileInputStream(fileName), password);
        return kyst;

    }

    private static KeyPair generateRandomKeyPair() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();

    }

    private static X509Certificate generateCertificate(KeyPair keyPair) throws Exception {

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        X500Name owner = new X500Name("CN=Made");
        X500Name issuer = new X500Name("CN=Project");
        BigInteger serialNumber = BigInteger.valueOf(1);
        Date expeditionDate = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + 1000000); //about 16.7 minutes

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKey.getEncoded()));

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuer,
                serialNumber,
                expeditionDate,
                expirationDate,
                owner,
                publicKeyInfo
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(privateKey);
        X509CertificateHolder certHolder = certBuilder.build(signer);
        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certHolder);

        return certificate;
    }

    //KEYSTORE PASSWORD

    private static String hashPassword(String password) throws Exception {

        int iterations = 10000;
        int length = 256;
        byte[] salt = generateRandomSalt();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySettings = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        byte[] hash = factory.generateSecret(keySettings).getEncoded();

        return bytesToHexadecimalString(hash);
    }

    private static byte[] generateRandomSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String bytesToHexadecimalString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

}
