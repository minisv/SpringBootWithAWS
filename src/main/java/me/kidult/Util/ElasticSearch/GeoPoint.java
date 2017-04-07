package me.kidult.Util.ElasticSearch;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoPoint {

  double lon;
  double lat;

  public GeoPoint(
      double lon,
      double lat
  ) {
    this.lon = lon;
    this.lat = lat;
  }

}
