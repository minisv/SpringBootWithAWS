package me.kidult.Util.Crypt;

import me.kidult.Exception.LibraryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class AESUtils {

  private String keySpec;
  private SecretKeySpec secretKeySpec;

  public AESUtils(String keySpec, int position) {
    try {
      this.keySpec = keySpec.substring(position, position + 16);
      byte[] keyArray = new byte[16];
      byte[] key = keySpec.getBytes("UTF-8");

      int length = key.length;
      if (length > keyArray.length) {
        length = keyArray.length;
      }
      System.arraycopy(key, 0, keyArray, 0, length);
      this.secretKeySpec = new SecretKeySpec(keyArray, "AES");
    } catch (UnsupportedEncodingException e) {
      throw new LibraryException("AESUtils : AESUtils -> " + e.getMessage());
    }
  }

  public String encrypt(String text) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(keySpec.getBytes()));
      return Base64.getUrlEncoder().encodeToString(cipher.doFinal(text.getBytes()));
    } catch (
        NullPointerException |
        NoSuchAlgorithmException |
        NoSuchPaddingException |
        InvalidKeyException |
        InvalidAlgorithmParameterException |
        IllegalBlockSizeException |
        BadPaddingException e
        ) {
      throw new LibraryException("AESUtils : encrypt -> " + e.getMessage());
    }
  }

  public String decrypt(String text) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(keySpec.getBytes()));
      return new String(cipher.doFinal(Base64.getUrlDecoder().decode(text.getBytes())), "UTF-8");
    } catch (
        NullPointerException |
        NoSuchAlgorithmException |
        NoSuchPaddingException |
        InvalidKeyException |
        InvalidAlgorithmParameterException |
        IllegalBlockSizeException |
        BadPaddingException |
        UnsupportedEncodingException e
        ) {
      throw new LibraryException("AESUtils : decrypt -> " + e.getMessage());
    }
  }

}
