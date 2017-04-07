package me.kidult.Util.ElasticSearch;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElasticSearchObject {

  private static final Logger logger = LoggerFactory.getLogger(ElasticSearchObject.class);


  String index;
  String type;
  ElasticSearchMessageFormat source;
  String id;
  Long version;
  Long ttl;
  Boolean create;
  String timestamp;

  GeoPoint geoPoint;

  String timezone;
  String country;
  String city;
  String postalCode;
  String subdivision;
  String continent;
  double[] location = {180L,90L};

  public ElasticSearchObject(String index, String type, ElasticSearchMessageFormat source) {
    this.index = index;
    this.type = type;
    this.id = UUID.randomUUID().toString();
    this.source = source;
    this.version = null;
    this.create = null;
    this.timestamp = printServerTimeFormat();
    if (this.source.getXForwardedFor() != null) {
      setGeoField(source.getXForwardedFor());
    }
  }

  private void setGeoField(String source) {
    try {
      if (source == null) {
        return;
      }

      InetAddress ipAddress = InetAddress.getByName(source);
      CityResponse response = GeoLite2CityHelper.getInstance().getCityResponse(ipAddress);

      if (response != null) {
        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();
        Location location = response.getLocation();
        String postalCode = response.getPostal().getCode();
        String timezone = response.getLocation().getTimeZone();

        this.geoPoint = new GeoPoint(
            location.getLongitude(), location.getLatitude()
        );
        this.timezone = timezone;
        this.country = country.getName();
        this.city = city.getName();
        this.postalCode = postalCode;
        this.subdivision = subdivision.getName();
        this.continent = response.getContinent().getName();
        this.location[0] = location.getLongitude();
        this.location[1] = location.getLatitude();

      }
    } catch (IOException | GeoIp2Exception e) {

    }
  }

  private String printServerTimeFormat() {
    long time = new Date().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    return simpleDateFormat.format(time);
  }

  @Override
  public String toString() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }

}
