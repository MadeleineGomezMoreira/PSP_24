package org.example.security_project_encripting.methods;

import com.google.common.primitives.Bytes;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.example.security_project_encripting.model.AppError;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
public class AESEncryption {

    //con esto cifro el informe
    //el secret debe ser la clave pública del administrador

    //A type of symmetric key encryption
    //mode: GCM (Galois/Counter Mode)
    //padding: noPadding
    //key length: 256 bits
    //iv length: 12 bytes
    //salt length: 16 bytes
    //gcm authentication tag length: 128 bits
    //iteration count: 65536

    public Either<AppError, String> encrypt(String strToEncrypt, String secret) {
        Either<AppError, String> operationResult;
        try {

            byte[] iv = new byte[12];
            byte[] salt = new byte[16];
            //we generate a secure random byte array value for the initial value + salt
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);
            sr.nextBytes(salt);

            //we set up the GCM encryption mode
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

            //we create an instance of the SecretKeyFactory class, which lets us generate
            //secret keys that can be used in symmetric encryption algorithms
            //PBKDF2 (Password-Based Key Derivation Function 2) --> derives a key from a password/passphrase
            //HmacSHA256 --> hash function used by PBKDF2
            SecretKeyFactory skFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            //PBEKeySpec --> password-based encryption key specification
            //its constructor takes the following parameters:
            //password/passphrase --> as an array of characters ('secret')
            //salt --> array of bytes generated above
            //65536 --> iteration count (for hashing)
            //256 --> the length of the derived key in bits
            KeySpec specs = new PBEKeySpec(secret.toCharArray(), salt, 65536, 256);

            //we generate the key with the specifications above
            SecretKey sk = skFactory.generateSecret(specs);

            //SecretKeySpec --> it unwraps the byte array that contains the key material and also specifies the algorithm associated with the key (AES)
            SecretKeySpec secretKey = new SecretKeySpec(sk.getEncoded(), "AES");

            //we instantiate the Cipher object indicating:
            //the cipher algorithm --> AES
            //the operation mode for symmetric key cryptographic block ciphers --> CGM
            //the padding settings --> no padding
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");

            //we initialize the cipher in encryption mode with the secret key and gcm parameter specifications (for encryption)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

            //the iv and salt arrays are concatenated along with the input string array
            //then these are encoded into a Base64 url-safe string
            String encrypted = Base64.getUrlEncoder().encodeToString(Bytes.concat(iv, salt,
                    //the input String is converted into an array of bytes
                    //which is then encrypted using Cipher
                    cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))
            ));

            operationResult = Either.right(encrypted);

        } catch (Exception e) {
            log.error("Error while encrypting: " + e);
            operationResult = Either.left(new AppError("Error while encrypting: " + e));
        }
        return operationResult;
    }

    public Either<AppError, String> decrypt(String strToDecrypt, String secret) {
        Either<AppError, String> operationResult;

        try {
            //we decode the concatenated byte array that includes the iv, salt and input string
            byte[] decoded = Base64.getUrlDecoder().decode(strToDecrypt);

            //we get the iv byte array (from 0 --> 12)
            byte[] iv = Arrays.copyOf(decoded, 12);

            //we get the salt byte array (from 12 --> 28)
            byte[] salt = Arrays.copyOfRange(decoded, 12, 28);

            //we recreate the GCM parameter specs
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

            //we recreate an instance of the SecretKeyFactory
            SecretKeyFactory skFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            //we recreate the specifications for password-based encryption key derivation
            KeySpec specs = new PBEKeySpec(secret.toCharArray(), salt, 65536, 256);

            //we generate the key with the specifications above
            SecretKey sk = skFactory.generateSecret(specs);
            SecretKeySpec secretKey = new SecretKeySpec(sk.getEncoded(), "AES");

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

}
