package me.kidult.DAO.Token;

import me.kidult.DTO.Token.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Component
public class TokenDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public String getSecretKeyByAccessKey(
      String accessKey
  ) {
    List<String> secretKeyList = jdbcTemplate.query(
        "SELECT secretKey FROM token WHERE accessKey = ?",
        new Object[]{accessKey},
        (rs, rowNo) -> {
          return rs.getString("secretKey");
        }
    );
    if (secretKeyList.size() > 0) return secretKeyList.get(0);
    else return null;
  }
}
