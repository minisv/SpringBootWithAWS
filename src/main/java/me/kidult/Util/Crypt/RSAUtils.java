package me.kidult.Util.Crypt;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import me.kidult.Exception.LibraryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RSAUtils {
  private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);

  Key publicKey;
  Key privateKey;

  /**
   * RSA 2048 은 80자 까지 암호화 하여 전송하도록 한다
   */
  public RSAUtils() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);

      KeyPair keyPair = keyPairGenerator.genKeyPair();
      publicKey = keyPair.getPublic();
      privateKey = keyPair.getPrivate();
    } catch (Throwable e) {
      throw new LibraryException("RSAUtils : RSAUtils -> " + e.getMessage());
    }
  }

  public static String encrypt(Key publicKey, String text) {
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] data = cipher.doFinal(text.getBytes());
      return Base64.getUrlEncoder().encodeToString(data);
    } catch (Throwable e) {
      logger.info("publicKey : " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
      logger.info("text : " + text);
      throw new LibraryException("RSAUtils : encrypt + " + e.getMessage());
    }
  }

  public static String decrypt(Key privateKey, String text) {
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      byte[] encryptedData = Base64.getUrlDecoder().decode(text);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] data = cipher.doFinal(encryptedData);
      return new String(data, "UTF-8");
    } catch (Throwable e) {
      logger.info("privateKey : " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
      logger.info("text : " + text);
      throw new LibraryException("RSAUtils : decrypt + " + e.getMessage());
    }
  }

}
