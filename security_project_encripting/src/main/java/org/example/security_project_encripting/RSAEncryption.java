package org.example.security_project_encripting;

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
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
public class RSAEncryption {

    //name = "nombre"
    //strToCipher = "123456"

    public Either<AppError, String> encrypt(String name, String strToCipher) {
        Either<AppError, String> operationResult;

        try {

            //we instantiate the Cipher object and set it up for RSA encryption/decryption
            Cipher cipher = Cipher.getInstance("RSA");

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


            //3-a --> Cipher mode --> ENCRYPT

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //we encrypt the input String ('strToCipher')
            String unciphered = strToCipher;
            String ciphered;

            byte[] bufferCiphered = null;
            byte[] bufferCode64 = null;
            byte[] buffer = unciphered.getBytes("UTF-8");

            bufferCiphered = cipher.doFinal(buffer);
            bufferCode64 = Base64.getUrlEncoder().encode(bufferCiphered);

            ciphered = new String(bufferCode64, StandardCharsets.UTF_8);

            operationResult = Either.right(ciphered);


        } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IOException |
                 InvalidKeyException | InvalidKeySpecException | IllegalBlockSizeException ex) {
            log.error("Error while ciphering: " + ex);
            operationResult = Either.left(new AppError("Error while ciphering: " + ex));
        }
        return operationResult;
    }

}
