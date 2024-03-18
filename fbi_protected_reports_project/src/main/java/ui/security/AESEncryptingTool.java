package ui.security;

import com.google.common.primitives.Bytes;
import domain.error.AppError;
import domain.model.EncryptedData;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
public class AESEncryptingTool {

    //A type of symmetric key encryption
    //mode: GCM (Galois/Counter Mode)
    //padding: noPadding
    //key length: 256 bits
    //iv length: 12 bytes
    //salt length: 16 bytes
    //gcm authentication tag length: 128 bits
    //iteration count: 65536

    public Either<AppError, EncryptedData> encrypt(String strToEncrypt) {
        Either<AppError, EncryptedData> operationResult;
        try {
            // Generate a random symmetric key
            SecretKey sk = generateSymmetricKey();

            byte[] iv = new byte[12];
            byte[] salt = new byte[16];
            //we generate a secure random byte array value for the initial value + salt
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);
            sr.nextBytes(salt);

            //we set up the GCM encryption mode
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

            //we instantiate the Cipher object indicating:
            //the cipher algorithm --> AES
            //the operation mode for symmetric key cryptographic block ciphers --> CGM
            //the padding settings --> no padding
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");

            //we initialize the cipher in encryption mode with the secret key and gcm parameter specifications (for encryption)
            cipher.init(Cipher.ENCRYPT_MODE, sk, gcmParameterSpec);

            //the iv and salt arrays are concatenated along with the input string array
            //then these are encoded into a Base64 url-safe string
            String encrypted = Base64.getUrlEncoder().encodeToString(Bytes.concat(iv, salt,
                    //the input String is converted into an array of bytes
                    //which is then encrypted using Cipher
                    cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))
            ));

            //I convert the aes key from an array of bytes to String representation
            String encodedAesKey = Base64.getUrlEncoder().encodeToString(sk.getEncoded());

            EncryptedData encryptedData = new EncryptedData();
            encryptedData.setAesKey(encodedAesKey);
            encryptedData.setEncryptedReport(encrypted);

            operationResult = Either.right(encryptedData);

        } catch (Exception e) {
            log.error("Error while encrypting: " + e);
            operationResult = Either.left(new AppError("Error while encrypting: " + e));
        }
        return operationResult;
    }

    public Either<AppError, String> decrypt(EncryptedData encryptedData) {
        Either<AppError, String> operationResult;

        try {
            //we decode the concatenated byte array that includes the iv, salt and input string
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedData.getEncryptedReport());

            //we get the iv byte array (from 0 --> 12)
            byte[] iv = Arrays.copyOf(decoded, 12);

            //we get the salt byte array (from 12 --> 28)
            byte[] salt = Arrays.copyOfRange(decoded, 12, 28);

            //get the encoded AES key from the EncryptedData object
            byte[] decodedAesKey = Base64.getUrlDecoder().decode(encryptedData.getAesKey());


            //we recreate the GCM parameter specs
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

            //we create a SecretKeySpec using the encoded AES key
            SecretKeySpec secretKey = new SecretKeySpec(decodedAesKey, "AES");

            //we recreate a Cipher object and initialize it in decrypt mode this time
            Cipher cipher = Cipher.getInstance("AES/GCM/noPADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

            //we decrypt the last decoded part (which corresponds with the initial input String) --> (from 28 --> end of array)
            String decrypted = new String(cipher.doFinal(Arrays.copyOfRange(decoded, 28, decoded.length)), StandardCharsets.UTF_8);

            operationResult = Either.right(decrypted);

        } catch (Exception e) {
            log.error("Error while decrypting: " + e);
            operationResult = Either.left(new AppError("Error while decrypting: " + e));
        }
        return operationResult;
    }

    private SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        //here I generate a random symmetric key using AES algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); //the key size is 256 bits
        return keyGenerator.generateKey();
    }

}
