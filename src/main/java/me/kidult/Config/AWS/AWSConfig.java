package me.kidult.Config.AWS;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@ConfigurationProperties(prefix = "aws")
public class AWSConfig {

  String regions;
  String accessKey;
  String secretKey;
  String kinesisFireHoseDeliveryStreamName;

}
