package ui.security;

import domain.model.PasswordItem;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordManager {

    public char[] hashPassword(String password, String saltString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //first we must define the PBKDF2 parameters
        int iterations = 10000;
        int length = 256; // This is length in bits
        byte[] salt = stringSaltToByteArray(saltString);

        //now we hash the password using PBKDF2 with HMAC SHA-256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySettings = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        byte[] hashBytes = factory.generateSecret(keySettings).getEncoded();

        //we then convert the byte array into a char array using Base64 encoding
        return Base64.getEncoder().encodeToString(hashBytes).toCharArray();
    }

    public PasswordItem hashPasswordRandomSalt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //first we must define the PBKDF2 parameters
        int iterations = 10000;
        int length = 256; // This is length in bits
        String saltString = generateRandomSalt();
        byte[] salt = stringSaltToByteArray(saltString);

        //now we hash the password using PBKDF2 with HMAC SHA-256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySettings = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        byte[] hashBytes = factory.generateSecret(keySettings).getEncoded();

        String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
        char[] passCharArray = hashedPassword.toCharArray();

        return new PasswordItem(passCharArray, hashedPassword, saltString);
    }

    private byte[] stringSaltToByteArray(String saltString) {
        //decode the salt string back to its original byte array form
        return Base64.getDecoder().decode(saltString);
    }

    private String generateRandomSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
