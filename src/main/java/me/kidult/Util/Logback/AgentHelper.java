package me.kidult.Util.Logback;

import eu.bitwalker.useragentutils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class AgentHelper {

  public static String getUserAgentString(HttpServletRequest request) {
    return request.getHeader("User-Agent");
  }

  public static String getUserAgentString() {
    return getUserAgentString(HttpHelper.getCurrentRequest());
  }

  public static UserAgent getUserAgent(HttpServletRequest request) {
    try {
      String userAgentString = getUserAgentString(request);
      return UserAgent.parseUserAgentString(userAgentString);
    } catch (Exception e) {

    }
    return null;
  }

  public static UserAgent getUserAgent() {
    return getUserAgent(HttpHelper.getCurrentRequest());
  }

  public static OperatingSystem getUserOperatingSystem(HttpServletRequest request) {
    UserAgent userAgent = getUserAgent(request);
    return userAgent == null ? OperatingSystem.UNKNOWN : userAgent.getOperatingSystem();
  }

  public static OperatingSystem getUserOperatingSystem() {
    return getUserOperatingSystem(HttpHelper.getCurrentRequest());
  }

  public static Browser getBrowser(HttpServletRequest request) {
    UserAgent userAgent = getUserAgent(request);
    return userAgent == null ? Browser.UNKNOWN : userAgent.getBrowser();
  }

  public static Browser getBrowser() {
    return getBrowser(HttpHelper.getCurrentRequest());
  }

  public static Version getBrowserVersion(HttpServletRequest request) {
    UserAgent userAgent = getUserAgent(request);
    if (userAgent == null) {
      return new Version("0", "0", "0");
    } else {
      Version version = userAgent.getBrowserVersion();
      if (version == null) {
        return new Version("0", "0", "0");
      } else {
        return version;
      }
    }
  }

  public static Version getBrowserVersion() {
    return getBrowserVersion(HttpHelper.getCurrentRequest());
  }

  public static BrowserType getBrowserType(HttpServletRequest request) {
    Browser browser = getBrowser(request);
    return browser == null ? BrowserType.UNKNOWN : browser.getBrowserType();
  }

  public static BrowserType getBrowserType() {
    return getBrowserType(HttpHelper.getCurrentRequest());
  }

  public static RenderingEngine getRenderingEngine(HttpServletRequest request) {
    Browser browser = getBrowser(request);
    return browser == null ? RenderingEngine.OTHER : browser.getRenderingEngine();
  }

  public static RenderingEngine getRenderingEngine() {
    return getRenderingEngine(HttpHelper.getCurrentRequest());
  }

  public static DeviceType getDeviceType(HttpServletRequest request) {
    OperatingSystem operatingSystem = getUserOperatingSystem(request);
    return operatingSystem == null ? DeviceType.UNKNOWN : operatingSystem.getDeviceType();
  }

  public static DeviceType getDeviceType() {
    return getDeviceType(HttpHelper.getCurrentRequest());
  }

  public static Manufacturer getManufacturer(HttpServletRequest request) {
    OperatingSystem operatingSystem = getUserOperatingSystem(request);
    return operatingSystem == null ? Manufacturer.OTHER : operatingSystem.getManufacturer();
  }

  public static Manufacturer getManufacturer() {
    return getManufacturer(HttpHelper.getCurrentRequest());
  }

  public static Map<String, String> getAgentDetail(HttpServletRequest request) {
    Map<String, String> agentDetail = new HashMap<>();
    agentDetail.put("browser", getBrowser(request).toString());
    agentDetail.put("browserType", getBrowserType(request).toString());
    agentDetail.put("browserVersion", getBrowserVersion(request).toString());
    agentDetail.put("renderingEngine", getRenderingEngine(request).toString());
    agentDetail.put("operatingSystem", getUserOperatingSystem(request).toString());
    agentDetail.put("deviceType", getDeviceType(request).toString());
    agentDetail.put("manufacturer", getManufacturer(request).toString());

    return agentDetail;
  }

}
