package me.kidult.Util.ElasticSearch;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import me.kidult.Util.Logback.AgentHelper;
import me.kidult.Util.Logback.RequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElasticSearchMessageFormat {

  String requestURI;
  String parameter;

  String accessKey;

  // CloudFront Information
  String xAmzCfId;
  String referer;
  String host;
  String acceptLanguage;
  String acceptEncoding;
  String connection;
  String cookie;
  String ga;
  String accept;
  String via;
  String xForwardedProto;
  String xForwardedPort;
  String xForwardedFor;
  String xForwardedServer;
  String xForwardedHost;
  String xRequestedWith;
  String cloudfrontViewerCountry;
  String cloudfrontIsSmarttvViewer;
  String cloudfrontIsDesktopViewer;
  String cloudftontIsTabletViewer;
  String cloudfrontForwardedProto;
  String cloudfrontIsMobileViewer;
  String userAgent;

  // AgentDetail
  String deviceType;
  String browser;
  String browserVersion;
  String renderingEngine;
  String operationSystem;
  String browserType;
  String manufacturer;

  int responseCode;
  String exception;

  public ElasticSearchMessageFormat(ServletRequest servletRequest, ServletResponse servletResponse) {
    try {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;

      RequestWrapper requestWrapper = RequestWrapper.of(request);

      this.requestURI = requestWrapper.getRequestUri();
      this.parameter = requestWrapper.parameterMap().toString();

      if (this.parameter.length() > 2048) {
        this.parameter = this.parameter.substring(2048);
      }

      if (this.parameter.equalsIgnoreCase("{}")) {
        this.parameter = "";
      }

      this.responseCode = response.getStatus();

      String token = request.getHeader("Authorization");
      if (token.length() > 0) {
        this.accessKey = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary("JWTSecretSystem"))
            .parseClaimsJws(token)
            .getBody().getId();
      }

      // CloudFront Information
      this.xAmzCfId = requestWrapper.headerMap().get("x-amz-cf-id");
      this.referer = requestWrapper.headerMap().get("referer");
      this.host = requestWrapper.headerMap().get("host");
      this.acceptLanguage = requestWrapper.headerMap().get("accept-language");
      this.acceptEncoding = requestWrapper.headerMap().get("accept-encoding");
      this.connection = requestWrapper.headerMap().get("connection");
      this.cookie = requestWrapper.headerMap().get("cookie");
      this.ga = requestWrapper.headerMap().get("_ga");
      this.accept = requestWrapper.headerMap().get("accept");
      this.via = requestWrapper.headerMap().get("via");
      this.xForwardedProto = requestWrapper.headerMap().get("x-forwarded-proto");
      this.xForwardedPort = requestWrapper.headerMap().get("x-forwarded-port");
      this.xForwardedFor = requestWrapper.headerMap().get("x-forwarded-for");
      this.xForwardedServer = requestWrapper.headerMap().get("x-forwarded-server");
      this.xForwardedHost = requestWrapper.headerMap().get("x-forwarded-host");
      this.xRequestedWith = requestWrapper.headerMap().get("x-requested-with");
      this.cloudfrontViewerCountry = requestWrapper.headerMap().get("cloudfront-viewer-country");
      this.cloudfrontIsSmarttvViewer = requestWrapper.headerMap().get("cloudfront-is-smarttv-viewer");
      this.cloudfrontIsDesktopViewer = requestWrapper.headerMap().get("cloudfront-is-desktop-viewer");
      this.cloudftontIsTabletViewer = requestWrapper.headerMap().get("cloudfront-is-tablet-viewer");
      this.cloudfrontForwardedProto = requestWrapper.headerMap().get("cloudfront-forwarded-proto");
      this.cloudfrontIsMobileViewer = requestWrapper.headerMap().get("cloudfront-is-mobile-viewer");
      this.userAgent = requestWrapper.headerMap().get("user-agent");

      // AgentDetail
      this.deviceType = AgentHelper.getAgentDetail(request).get("deviceType");
      this.browser = AgentHelper.getAgentDetail(request).get("browser");
      this.browserVersion = AgentHelper.getAgentDetail(request).get("browserVersion");
      this.renderingEngine = AgentHelper.getAgentDetail(request).get("renderingEngine");
      this.operationSystem = AgentHelper.getAgentDetail(request).get("operationSystem");
      this.browserType = AgentHelper.getAgentDetail(request).get("browserType");
      this.manufacturer = AgentHelper.getAgentDetail(request).get("manufacturer");

    } catch (NullPointerException | ExpiredJwtException | MalformedJwtException e) {

    }
  }

}
