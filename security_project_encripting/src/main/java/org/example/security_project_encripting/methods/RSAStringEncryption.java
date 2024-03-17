package org.example.security_project_encripting.methods;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.example.security_project_encripting.model.AppError;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
public class RSAStringEncryption {

    //en rsa se suelen encriptar claves que luego se usan de manera simétrica

    //name = "nombre"
    //strToCipher = "123456"

    public Either<AppError, String> encrypt(String name, String strToEncrypt) {
        Either<AppError, String> operationResult;

        try {

            //we instantiate the Cipher object and set it up for RSA encryption/decryption
            //we are using padding here to make it more secure
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            //we create an instance of KeyFactory
            KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA");

            //to retrieve the public key from the file ('name.public'):

            //1 --> we read binary data
            //we place it in a byte array (with a 5000 byte length)
            byte[] bufferPub = new byte[5000];

            //we initialize a file which is assumed to contain the public key
            FileInputStream publicKeyFile = new FileInputStream(name + ".public");

            //this will read primitive data types from the input stream above (unused)
            DataInputStream data = new DataInputStream(publicKeyFile);

            //we read the data from the input stream and place it into the 5000 bytes array
            int charsPub = publicKeyFile.read(bufferPub, 0, 5000);
            //we close the input stream (to free system resources)
            publicKeyFile.close();

            //we instantiate another byte array with a length equal to the file ('charsPub')
            //this array will store the contents of the public key
            byte[] bufferPub2 = new byte[charsPub];

            //we copy the contents of the original byte array ('bufferPub') into the new byte array ('bufferPub2')
            //it copies 'charsPub' number of bytes
            //from the beginning of the first array ('bufferPub') to the beginning of the second ('bufferPub2')
            System.arraycopy(bufferPub, 0, bufferPub2, 0, charsPub);


            //2 --> retrieve public key from codified data (X509 format)

            //we create an instance of a class that will build a public key from its encoded X509 format
            //its constructor takes a byte array which contains the encoded public key ('bufferPub2')
            X509EncodedKeySpec publicKeySpecs = new X509EncodedKeySpec(bufferPub2);

            //we reconstruct the public key
            PublicKey publicKey = keyFactoryRSA.generatePublic(publicKeySpecs);

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

            operationResult = Either.right(ciphered);

        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IOException |
                 InvalidKeyException | InvalidKeySpecException | IllegalBlockSizeException ex) {
            log.error("Error while encrypting: " + ex);
            operationResult = Either.left(new AppError("Error while encrypting: " + ex));
        }
        return operationResult;
    }

    public Either<AppError, String> decrypt(String name, String strToDecrypt) {
        Either<AppError, String> operationResult;

        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA");

            //to retrieve the private key from the file ('name.private'):

            //1 --> we read binary data
            //we place it in a byte array (with a 5000 byte length)
            byte[] bufferPriv = new byte[5000];

            //we initialize a file which is assumed to contain the private key
            FileInputStream privateKeyFile = new FileInputStream(name + ".private");

            //we read the data from the input stream and place it into the 5000 bytes array
            int charsPriv = privateKeyFile.read(bufferPriv, 0, 5000);
            privateKeyFile.close();

            //we instantiate another byte array with a length equal to the file ('charsPriv')
            //this array will store the contents of the private key
            byte[] bufferPriv2 = new byte[charsPriv];

            //we copy the contents of the original byte array ('bufferPriv') into the new byte array ('bufferPriv2')
            //it copies 'charsPriv' number of bytes
            //from the beginning of the first array ('bufferPriv') to the beginning of the second ('bufferPriv2')
            System.arraycopy(bufferPriv, 0, bufferPriv2, 0, charsPriv);

            //2 --> retrieve private key from codified data (X509 format)

            //we create an instance of a class that will build a public key from its encoded X509 format
            //its constructor takes a byte array which contains the encoded private key ('bufferPriv2')
            X509EncodedKeySpec privateKeySpecs = new X509EncodedKeySpec(bufferPriv2);

            //we reconstruct the private key
            PrivateKey privateKey = keyFactoryRSA.generatePrivate(privateKeySpecs);

            //3 --> Cipher mode --> DECRYPT
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //decode the Base64-encoded String ('strToDecrypt') to get the encrypted byte array
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(strToDecrypt);

            //we decrypt the encrypted byte array
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            //finally we convert the decrypted byte array to a String
            String deciphered = new String(decryptedBytes, StandardCharsets.UTF_8);

            operationResult = Either.right(deciphered);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException |
                 IOException ex) {
            log.error("Error while decrypting: " + ex);
            operationResult = Either.left(new AppError("Error while decrypting: " + ex));
        }
        return operationResult;
    }
}