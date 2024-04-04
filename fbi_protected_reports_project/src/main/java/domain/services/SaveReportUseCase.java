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
import ui.security.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SaveReportUseCase {

    private final DaoReport daoReport;
    private final DaoAccessControl daoAccessControl;
    private final DaoUser daoUser;
    private final AESEncryptingTool contentEncryptor;
    private final KeyRetrieverTool keyRetriever;
    private final RSAEncryptionTool keyEncryptor;
    private final SignatureManager signatureManager;
    private final PasswordManager passwordManager;

    @Inject
    public SaveReportUseCase(DaoReport daoReport, DaoAccessControl daoAccessControl, DaoUser daoUser, AESEncryptingTool contentEncryptor, KeyRetrieverTool keyRetriever, RSAEncryptionTool keyEncryptor, SignatureManager signatureManager, PasswordManager passwordManager) {
        this.daoReport = daoReport;
        this.daoAccessControl = daoAccessControl;
        this.daoUser = daoUser;
        this.contentEncryptor = contentEncryptor;
        this.keyRetriever = keyRetriever;
        this.keyEncryptor = keyEncryptor;
        this.signatureManager = signatureManager;
        this.passwordManager = passwordManager;
    }

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
