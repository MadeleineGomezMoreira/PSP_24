package ui.security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignatureManager {

    //generate a digital signature for the report content
    public String signReport(String reportContent, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(reportContent.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    //verify the digital signature of the report content
    public boolean verifySignature(String reportContent, String signatureString, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(reportContent.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
        return signature.verify(signatureBytes);
    }

}
