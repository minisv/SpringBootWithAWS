package me.kidult.Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class JsonHelper {

  private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

  private final ObjectMapper objectMapper;

  private JsonHelper() {
    objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, true);
    objectMapper.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true);
    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
  }

  public static JsonHelper getInstance() {
    return new JsonHelper();
  }

  private static ObjectMapper getObjectMapper() {
    return getInstance().objectMapper;
  }

  public static String toJson(Object object) {
    try {
      return getObjectMapper().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException("JsonUtils : toJson -> " + e);
    }
  }

  public static <T> T fromJson(String jsonString, Class<T> clazz) {
    try {
      return getObjectMapper().readValue(jsonString, clazz);
    } catch (Exception e) {
      throw new RuntimeException("JsonUtils : fromJson -> " + e);
    }
  }

  public static <T extends Collection> T fromJson(String jsonString, CollectionType collectionType) {
    try {
      return getObjectMapper().readValue(jsonString, collectionType);
    } catch (Exception e) {
      throw new RuntimeException("JsonUtils : fromJson (collection) -> " + e);
    }
  }

  public static String toPrettyJson(String json) {
    Object object = JsonHelper.fromJson(json, Object.class);
    try {
      return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }

}
