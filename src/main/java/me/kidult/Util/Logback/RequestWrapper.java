package me.kidult.Util.Logback;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class RequestWrapper {
  private HttpServletRequest request;

  private RequestWrapper(HttpServletRequest request) {
    this.request = request;
  }

  public static RequestWrapper of(HttpServletRequest request) {
    return new RequestWrapper(request);
  }

  public static RequestWrapper of(ServletRequest request) {
    return of((HttpServletRequest) request);
  }

  public Map<String, String> headerMap() {
    Map<String, String> convertedHeaderMap = new HashMap<>();

    Enumeration<String> headerMap = request.getHeaderNames();

    while (headerMap.hasMoreElements()) {
      String key      = headerMap.nextElement();
      String value    = request.getHeader(key);

      convertedHeaderMap.put(key, value);
    }
    return convertedHeaderMap;
  }

  public Map<String, String> parameterMap() {
    Map<String, String> convertedParameterMap = new HashMap<>();
    Map<String, String[]> parameterMap = request.getParameterMap();

    for(String key : parameterMap.keySet()) {
      String[] values = parameterMap.get(key);
      StringJoiner valueString = new StringJoiner(",");

      for(String value : values) {
        valueString.add(value);
      }
      convertedParameterMap.put(key, valueString.toString());
    }
    return convertedParameterMap;
  }

  public String getRequestUri() {
    return request.getRequestURI();
  }
}
