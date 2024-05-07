package jakarta.di;

import common.Constants;
import common.config.ConfigSettings;
import domain.exception.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyProvider {

    @Produces
    @Singleton
    @Named(Constants.JWT)
    public SecretKey key(ConfigSettings config) {
        try {
            final MessageDigest digest =
                    MessageDigest.getInstance(Constants.SHA_512_ALGORITHM);

            String secretKey = config.getSecretKey();

            digest.update(secretKey.getBytes(StandardCharsets.UTF_8));
            final SecretKeySpec key2 = new SecretKeySpec(
                    digest.digest(), Constants.SK_OFFSET, Constants.SK_LEN, Constants.AES_ALGORITHM);

            return Keys.hmacShaKeyFor(key2.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidKeyException(Constants.INVALID_KEY);
        }
    }
}
