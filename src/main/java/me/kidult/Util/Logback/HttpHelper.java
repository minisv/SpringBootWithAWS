package me.kidult.Util.Logback;

import eu.bitwalker.useragentutils.Browser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class HttpHelper {

  public static HttpServletRequest getCurrentRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
  }

  public static HttpServletResponse getCurrentResponse() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
  }

  public static String getJsonContentType(HttpServletRequest request) {
    Browser browser = AgentHelper.getBrowser(request);

    if (browser != null && browser == Browser.IE) {
      return "text/plain; charset=UTF-8";
    }
    return "application/json; charset=UTF-8";
  }

  public static Map<String, String> getCurrentUser() {
    Map<String, String> mockUser = new HashMap<>();
    mockUser.put("name", "system");
    mockUser.put("userGroup", "Normal");
    return mockUser;
  }

}
