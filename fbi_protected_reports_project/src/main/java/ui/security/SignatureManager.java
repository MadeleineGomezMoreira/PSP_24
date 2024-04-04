package ui.security;

import domain.error.ReportSigningException;
import domain.error.ReportSigningVerificationException;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

//@Singleton
public class SignatureManager {

    //I will generate a digital signature for the report content and combine it with the report
    public String signReport(String reportContent, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(reportContent.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = signature.sign();

            // Combine the original report content and the signature
            String signedReport = reportContent + "\n" + Base64.getEncoder().encodeToString(signatureBytes);
            return signedReport;
        } catch (Exception e) {
            throw new ReportSigningException(e.getMessage());
        }
    }

    //here I'll verify the digital signature of the signed report
    public Boolean verifySignature(String signedReport, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            //I will split the signed report into original content and signature
            String[] parts = signedReport.split("\n", 2);
            String reportContent = parts[0];
            String signatureString = parts[1];

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(reportContent.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            throw new ReportSigningVerificationException(e.getMessage());
        }
    }
}
