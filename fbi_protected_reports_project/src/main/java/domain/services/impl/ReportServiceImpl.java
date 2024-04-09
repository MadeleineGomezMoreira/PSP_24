package domain.services.impl;

import data.dao.DaoAccessControl;
import data.dao.DaoReport;
import data.dao.DaoUser;
import data.model.AccessControlEntity;
import data.model.ReportEntity;
import data.model.UserEntity;
import domain.error.AppError;
import domain.model.EncryptedData;
import domain.services.ReportService;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import ui.security.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ReportServiceImpl implements ReportService {

    private final DaoReport daoReport;
    private final DaoUser daoUser;
    private final DaoAccessControl daoAccessControl;
    private final SignatureManager signatureManager;
    private final KeyRetrieverTool keyRetriever;
    private final RSAEncryptionTool keyEncryptor;
    private final AESEncryptingTool contentEncryptor;
    private final PasswordManager passwordManager;

    @Inject
    public ReportServiceImpl(DaoReport daoReport, DaoUser daoUser, DaoAccessControl daoAccessControl, SignatureManager signatureManager, KeyRetrieverTool keyRetriever, RSAEncryptionTool keyEncryptor, AESEncryptingTool contentEncryptor, PasswordManager passwordManager) {
        this.daoReport = daoReport;
        this.daoUser = daoUser;
        this.daoAccessControl = daoAccessControl;
        this.signatureManager = signatureManager;
        this.keyRetriever = keyRetriever;
        this.keyEncryptor = keyEncryptor;
        this.contentEncryptor = contentEncryptor;
        this.passwordManager = passwordManager;
    }

    //TODO: should we initialize the keyStore in the principal controller when it is initialized?
    //then we would pass the keyStore to all the methods that would use it

    @Override
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
            PrivateKey privateKey = keyRetriever.retrievePrivateKey("keystore.pfx", username, keyStorePassword, userPassword);

            //Then we need to decrypt the encrypted key
            ReportEntity report = getReport.get();
            String decryptedKey = keyEncryptor.decrypt(privateKey, encryptedKey);

            //Now we use that key to decrypt the report (aes)
            EncryptedData encryptedData = new EncryptedData(decryptedKey, report.getContent());
            String decryptedReport = contentEncryptor.decrypt(encryptedData);

            //We need to get the author's public key to verify the signature
            //we get the author's username from the report
            UserEntity userAuthor = daoUser.get(report.getLastModifiedById()).get();
            PublicKey userPublicKey = keyRetriever.retrievePublicKey("keystore.pfx", userAuthor.getUsername(), keyStorePassword);

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

    @Override
    public Either<AppError, Integer> saveReport(ReportEntity report) {
        try {
            //retrieve the user and admin by id
            Either<AppError, UserEntity> getUser = daoUser.get(report.getLastModifiedById());
            Either<AppError, UserEntity> getAdmin = daoUser.get(-1);

            UserEntity admin;
            UserEntity user;

            if (getUser.isLeft() || getAdmin.isLeft()) {
                return Either.left(new AppError("Error retrieving user or admin from database"));
            } else {
                admin = getAdmin.get();
                user = getUser.get();
            }

            //retrieve the keyStore's password
            char[] adminPassHashed = passwordManager.hashPassword(admin.getPassword(), admin.getSalt());
            //retrieve the user's password
            char[] userPassHashed = passwordManager.hashPassword(user.getPassword(), user.getSalt());

            //we retrieve each of the public keys
            PublicKey adminPk = keyRetriever.retrievePublicKey("keystore.pfx", "admin", adminPassHashed);
            PublicKey userPk = keyRetriever.retrievePublicKey("keystore.pfx", user.getUsername(), adminPassHashed);

            String reportContent = report.getContent();

            //we sign the report
            //for which we must obtain user private key
            PrivateKey userPrivateKey = keyRetriever.retrievePrivateKey("keystore.pfx", user.getUsername(), adminPassHashed, userPassHashed);
            String signedReport = signatureManager.signReport(reportContent, userPrivateKey);

            //then encrypt the report
            EncryptedData encryptReport = contentEncryptor.encrypt(signedReport);
            String encryptedReportContent = encryptReport.getEncryptedReport();

            //save encrypted report to db
            report.setContent(encryptedReportContent);
            Either<AppError, Integer> generatedReportId = daoReport.save(report);

            int reportId = 0;

            //get report generated id
            if (generatedReportId.isRight()) {
                reportId = generatedReportId.get();
            } else {
                return Either.left(generatedReportId.getLeft());
            }

            String symmetricKey = encryptReport.getAesKey();

            //encrypt symmetric keys with RSA for admin and user
            String encryptedKeyForAdmin = keyEncryptor.encrypt(adminPk, symmetricKey);
            String encryptedKeyForUser = keyEncryptor.encrypt(userPk, symmetricKey);

            //save access control
            AccessControlEntity acAdmin = new AccessControlEntity(
                    0,
                    reportId,
                    admin.getUserId(),
                    encryptedKeyForAdmin
            );
            AccessControlEntity acUser = new AccessControlEntity(
                    0,
                    reportId,
                    user.getUserId(),
                    encryptedKeyForUser
            );

            Either<AppError, Integer> saveAdminAc = daoAccessControl.save(acAdmin);
            Either<AppError, Integer> saveUserAc = daoAccessControl.save(acUser);

            if (saveAdminAc.isRight() && saveUserAc.isRight()) {
                return Either.right(1);
            } else {
                return Either.left(new AppError("Error while saving access control credentials"));
            }

        }catch (Exception e){
            return Either.left(new AppError(e.getMessage()));
        }
    }

}
