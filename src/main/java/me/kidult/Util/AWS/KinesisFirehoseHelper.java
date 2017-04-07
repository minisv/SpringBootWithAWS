package me.kidult.Util.AWS;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;
import me.kidult.Config.AWS.AWSConfig;
import me.kidult.Util.StringHelper;
import org.apache.http.NoHttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class KinesisFirehoseHelper {
  private static final Logger logger = LoggerFactory.getLogger(KinesisFirehoseHelper.class);

  private AmazonKinesisFirehoseClient amazonKinesisFirehoseClient;

  private static KinesisFirehoseHelper instance;

  private AWSConfig awsConfig;

  private KinesisFirehoseHelper() {}

  public static synchronized KinesisFirehoseHelper getInstance() {
    if (instance == null) {
      instance = new KinesisFirehoseHelper();
    }
    return instance;
  }

  public void setAWSConfig(AWSConfig awsConfig) {
    this.awsConfig = awsConfig;
    try {
      if (awsConfig.getAccessKey() != null) {
        amazonKinesisFirehoseClient = new AmazonKinesisFirehoseClient(
            new AWSCredentials() {
              @Override
              public String getAWSAccessKeyId() {
                return awsConfig.getAccessKey();
              }

              @Override
              public String getAWSSecretKey() {
                return awsConfig.getSecretKey();
              }
            }
        );
      } else {
        amazonKinesisFirehoseClient = new AmazonKinesisFirehoseClient();
      }
    } catch (NullPointerException e) {
    } finally {
      if (amazonKinesisFirehoseClient == null) {
        amazonKinesisFirehoseClient = new AmazonKinesisFirehoseClient();
      }
    }
  }

  public void putItem(String data) throws NoHttpResponseException {
    PutRecordRequest putRecordRequest = new PutRecordRequest();
    putRecordRequest.setDeliveryStreamName(awsConfig.getKinesisFireHoseDeliveryStreamName());

    Record record = new Record();
    record.setData(StringHelper.stringToByteBuffer(data));
    putRecordRequest.setRecord(record);

    amazonKinesisFirehoseClient.putRecord(putRecordRequest);
  }

}
