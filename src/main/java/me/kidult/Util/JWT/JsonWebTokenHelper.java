package me.kidult.Util.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.kidult.Config.Crypt.CryptConfig;
import me.kidult.DAO.Token.TokenDAO;
import me.kidult.DTO.Token.UserToken;
import me.kidult.Exception.JsonWebToken.TokenExpiredException;
import me.kidult.Util.Crypt.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Component
public class JsonWebTokenHelper {

  private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenHelper.class);

  @Autowired
  private CryptConfig cryptConfig;

  @Autowired
  private TokenDAO tokenDAO;

  public String create(
      int userPk,
      UserToken userToken,
      long timeToLiveMillis
  ) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(cryptConfig.getJsonWebTokenSecret());
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder jwtBuilder = Jwts.builder()
        .setId(userToken.getAccessKey())
        .setIssuedAt(now)
        .setIssuer(String.valueOf(userPk))
        .setSubject(new AESUtils(userToken.getSecretKey(), 0).encrypt(userToken.getAccessKey()))
        .signWith(signatureAlgorithm, signingKey);

    if (timeToLiveMillis >= 0) {
      long expireMillis = nowMillis + timeToLiveMillis;
      Date expire = new Date(expireMillis);
      jwtBuilder.setExpiration(expire);
    }

    return jwtBuilder.compact();
  }

  public Claims verification(
      String jsonWebToken
  ) {
    try {
      Claims claims = getClaims(jsonWebToken);
      if (claims.getId().length() == 32) { // id 가 32자 일 경우, id 길이가 고정값 일때 사용 가능
        String secret = tokenDAO.getSecretKeyByAccessKey(claims.getId());
        if (claims.getId().equalsIgnoreCase(new AESUtils(secret, 0).decrypt(claims.getSubject()))) {
          return claims;
        } else {
          throw new RuntimeException("JsonWebTokenHelper : verification -> Not Valid Token : " + jsonWebToken);
        }
      } else {
        throw new TokenExpiredException("Token Expired");
      }
    } catch (RuntimeException e) {
      throw new TokenExpiredException("Token Expired");
    }
  }

  private Claims getClaims(
      String jsonWebToken
  ) {
    return Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(cryptConfig.getJsonWebTokenSecret()))
        .parseClaimsJws(jsonWebToken)
        .getBody();
  }

  public String getAccessKey(
      String jsonWebToken
  ) {
    return getClaims(jsonWebToken).getId();
  }

  public String getSecretKey(
      String jsonWebToken
  ) {
    Claims claims = getClaims(jsonWebToken);
    if (claims.getId().length() == 32) { // id 가 32자 일 경우, id 길이가 고정값 일때 사용 가능
      return tokenDAO.getSecretKeyByAccessKey(claims.getId());
    }
    return null;
  }

  public Integer getUserPk(
      String jsonWebToken
  ) {
    return Integer.parseInt(getClaims(jsonWebToken).getIssuer());
  }

}
