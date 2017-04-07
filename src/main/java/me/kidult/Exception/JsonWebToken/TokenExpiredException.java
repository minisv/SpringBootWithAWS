package me.kidult.Exception.JsonWebToken;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class TokenExpiredException extends RuntimeException {

  public TokenExpiredException(String message) {
    super(message);
  }

}
