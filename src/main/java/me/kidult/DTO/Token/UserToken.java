package me.kidult.DTO.Token;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserToken {

  String accessKey;
  String secretKey;

}
