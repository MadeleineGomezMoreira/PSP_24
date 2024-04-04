package domain.services;

import data.dao.DaoAccessControl;
import data.dao.DaoReport;
import data.dao.DaoUser;
import data.model.AccessControlEntity;
import data.model.ReportEntity;
import data.model.UserEntity;
import domain.error.AppError;
import domain.model.EncryptedData;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import ui.security.AESEncryptingTool;
import ui.security.KeyRetrieverTool;
import ui.security.RSAEncryptionTool;
import ui.security.SignatureManager;

import java.security.PrivateKey;
import java.security.PublicKey;

public class AccessReportUseCase {

    private final DaoReport daoReport;
    private final DaoUser daoUser;
    private final DaoAccessControl daoAccessControl;
    private final SignatureManager signatureManager;
    private final KeyRetrieverTool keyRetrieverTool;
    private final RSAEncryptionTool rsaEncryptionTool;
    private final AESEncryptingTool aesEncryptingTool;

    @Inject
    public AccessReportUseCase(DaoReport daoReport, DaoUser daoUser, DaoAccessControl daoAccessControl, SignatureManager signatureManager, KeyRetrieverTool keyRetrieverTool, RSAEncryptionTool rsaEncryptionTool, AESEncryptingTool aesEncryptingTool) {
        this.daoReport = daoReport;
        this.daoUser = daoUser;
        this.daoAccessControl = daoAccessControl;
        this.signatureManager = signatureManager;
        this.keyRetrieverTool = keyRetrieverTool;
        this.rsaEncryptionTool = rsaEncryptionTool;
        this.aesEncryptingTool = aesEncryptingTool;
    }

    //TODO: should we initialize the keyStore in the principal controller when it is initialized?
    //then we would pass the keyStore to all the methods that would use it

    public Either<AppError, ReportEntity> accessReport(String userPass, String username, int reportId, int userId) {

        try {

            //To access a report, we first need to retrieve it from the db
            Either<AppError, ReportEntity> getReport = daoReport.get(new ReportEntity(reportId));

            //We then retrieve the access control item for the report and the user
            Either<AppError, AccessControlEntity> getAccessControl = daoAccessControl.get(new AccessControlEntity(reportId, userId));

            //We extract the key from the access control item
            AccessControlEntity accessControl = getAccessControl.get();
            String encryptedKey = accessControl.getEncryptedKey();

            //We then decrypt the key with the user's private key and password
            char[] keyStorePassword = "admin".toCharArray();
            char[] userPassword = userPass.toCharArray();
            PrivateKey privateKey = keyRetrieverTool.retrievePrivateKey("keystore.pfx", username, keyStorePassword, userPassword);

            //Then we need to decrypt the encrypted key
            ReportEntity report = getReport.get();
            String decryptedKey = rsaEncryptionTool.decrypt(privateKey, encryptedKey);

            //Now we use that key to decrypt the report (aes)
            EncryptedData encryptedData = new EncryptedData(decryptedKey, report.getContent());
            String decryptedReport = aesEncryptingTool.decrypt(encryptedData);

            //We need to get the author's public key to verify the signature
            //we get the author's username from the report
            UserEntity userAuthor = daoUser.get(report.getLastModifiedById()).get();
            PublicKey userPublicKey = keyRetrieverTool.retrievePublicKey("keystore.pfx", userAuthor.getUsername(), keyStorePassword);

            //We then verify the signature
            boolean verified = signatureManager.verifySignature(decryptedReport, userPublicKey);

            if (!verified) {
                return Either.left(new AppError("The signature of the report could not be verified."));
            }

            //If the signature is verified, we return the report, but first we separate it from the signature
            String[] parts = decryptedKey.split("\n", 2);
            String reportContent = parts[0];

            //now we return the report
            ReportEntity accessedReport = new ReportEntity(
                    report.getReportId(),
                    report.getTitle(),
                    reportContent,
                    report.getLastModifiedById(),
                    report.getLastModifiedDate()
            );

            return Either.right(accessedReport);
        } catch (Exception e) {
            return Either.left(new AppError(e.getMessage()));
        }
    }
}
