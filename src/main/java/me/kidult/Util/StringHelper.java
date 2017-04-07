package me.kidult.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class StringHelper {

  private static final Logger logger = LoggerFactory.getLogger(StringHelper.class);

  public static String parseString(String data, String name, String searchEnd) {
    if (data.lastIndexOf(name) != -1) {
      try {
        String frontSplite = data.substring(data.indexOf(name)+name.length());
        return frontSplite.substring(0, frontSplite.indexOf(searchEnd));
      } catch (StringIndexOutOfBoundsException e) {
        logger.error("StringUtils Error : " + name + " : " + data);
        return "";
      }
    }
    return "";
  }

  public static ByteBuffer stringToByteBuffer(String data) {
    return ByteBuffer.wrap(data.getBytes());
  }

}
