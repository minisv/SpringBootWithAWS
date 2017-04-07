package me.kidult.Util.ElasticSearch;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class GeoLite2CityHelper {

  private static GeoLite2CityHelper instance;

  private DatabaseReader databaseReader;

  private GeoLite2CityHelper() throws IOException {
    databaseReader = new DatabaseReader
        .Builder(getClass().getResourceAsStream("/GeoLite2-City.mmdb")) //resources 에 파일 필요
        .fileMode(Reader.FileMode.MEMORY)
        .build();
  }

  public static synchronized GeoLite2CityHelper getInstance() throws IOException {
    if (instance == null) {
      instance = new GeoLite2CityHelper();
    }
    return instance;
  }

  public CityResponse getCityResponse(InetAddress ipAddress) throws IOException, GeoIp2Exception {
    if (ipAddress != null) {
      return databaseReader.city(ipAddress);
    } else {
      return null;
    }
  }

}
