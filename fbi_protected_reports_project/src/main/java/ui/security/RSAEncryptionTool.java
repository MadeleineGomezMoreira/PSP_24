package ui.security;

import domain.error.RsaEncryptionException;
import lombok.extern.log4j.Log4j2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

//@Singleton
@Log4j2
public class RSAEncryptionTool {

    //we usually use RSA to encrypt keys that are later used asymmetrically

    //name = username
    //strToCipher = key used to encrypt report

    public String encrypt(PublicKey publicKey, String strToEncrypt) {
        try {
            //we instantiate the Cipher object and set it up for RSA encryption/decryption
            //we are using padding here to make it more secure
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            //3 --> Cipher mode --> ENCRYPT
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //we encrypt the input String ('strToEncrypt')
            String ciphered;

            byte[] bufferCiphered = null;
            byte[] bufferCode64 = null;
            byte[] buffer = strToEncrypt.getBytes(StandardCharsets.UTF_8);

            bufferCiphered = cipher.doFinal(buffer);
            bufferCode64 = Base64.getUrlEncoder().encode(bufferCiphered);

            ciphered = new String(bufferCode64, StandardCharsets.UTF_8);

            return ciphered;

        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException |
                 InvalidKeyException | IllegalBlockSizeException ex) {
            log.error("Error while encrypting: " + ex);
            throw new RsaEncryptionException("Error while encrypting: " + ex);
        }
    }

    public String decrypt(PrivateKey privateKey, String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            //3 --> Cipher mode --> DECRYPT
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //decode the Base64-encoded String ('strToDecrypt') to get the encrypted byte array
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(strToDecrypt);

            //we decrypt the encrypted byte array
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            //finally we convert the decrypted byte array to a String
            String deciphered = new String(decryptedBytes, StandardCharsets.UTF_8);
            return deciphered;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException ex) {
            log.error("Error while decrypting: " + ex);
            throw new RsaEncryptionException("Error while decrypting: " + ex);
        }
    }
}