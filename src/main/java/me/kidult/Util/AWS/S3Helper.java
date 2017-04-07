package me.kidult.Util.AWS;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import me.kidult.Config.AWS.AWSConfig;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class S3Helper {
  private static final Logger logger = LoggerFactory.getLogger(S3Helper.class);

  private static final String SERVICE_DOMAIN_NAME = ".kidult.me";
  private static final String TEST_REGION_NAME = "ap-northeast-2";

  private static S3Helper instance;

  private AmazonS3Client amazonS3Client;

  private AWSConfig awsConfig;

  private S3Helper() {}

  public static synchronized S3Helper getInstance() {
    if (instance == null) {
      instance = new S3Helper();
    }

    return instance;
  }

  public void setAwsConfig(AWSConfig awsConfig) {
    this.awsConfig = awsConfig;
    ClientConfiguration clientConfiguration = new ClientConfiguration();
    clientConfiguration.setSignerOverride("AWSS3V4SignerType");
    try {
      if (awsConfig.getAccessKey() != null) {
        amazonS3Client = new AmazonS3Client(
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
        , clientConfiguration);
      } else {
        amazonS3Client = new AmazonS3Client(clientConfiguration);
      }
    } catch (NullPointerException e) {

    } finally {
      if (amazonS3Client == null) {
        amazonS3Client = new AmazonS3Client(clientConfiguration);
      }
      amazonS3Client.setRegion(Region.getRegion(Regions.fromName(awsConfig.getRegions())));
    }
  }

  /**
   * InputStream 으로 S3 에 파일을 업로드 해야 할 경우
   *
   * @param bucket
   * @param s3Path
   * @param fileName
   * @param fileContentType
   * @param inputStream
   * @param storageClass
   * @param cannedAccessControlList
   * @return
   */
  public PutObjectResult putObjectToS3ByInputStream(
      String bucket,
      String s3Path,
      String fileName,
      String fileContentType,
      InputStream inputStream,
      StorageClass storageClass,
      CannedAccessControlList cannedAccessControlList
  ) {
    String storagePath = s3Path + File.separator + fileName;

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(fileContentType);

    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucket,
        storagePath,
        inputStream,
        objectMetadata
    );

    putObjectRequest.setStorageClass(storageClass);
    putObjectRequest.setCannedAcl(cannedAccessControlList);

    return amazonS3Client.putObject(putObjectRequest);
  }

  /**
   * File 로 S3 에 파일을 업로드 해야 할 경우
   *
   * @param bucket
   * @param s3Path
   * @param fileName
   * @param file
   * @param storageClass
   * @param cannedAccessControlList
   * @return
   */
  public PutObjectResult putObjectToS3ByFile(
      String bucket,
      String s3Path,
      String fileName,
      File file,
      StorageClass storageClass,
      CannedAccessControlList cannedAccessControlList
  ) {
    String storagePath = s3Path + File.separator + fileName;

    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucket,
        storagePath,
        file
    );

    putObjectRequest.setStorageClass(storageClass);
    putObjectRequest.setCannedAcl(cannedAccessControlList);

    return amazonS3Client.putObject(putObjectRequest);
  }

  /**
   * File 로 S3 에 파일을 업로드 해야 할 경우
   * 단, ObjectMetadata 를 따로 업데이트 해야 할 경우
   *
   * @param bucket
   * @param s3Path
   * @param fileName
   * @param file
   * @param storageClass
   * @param cannedAccessControlList
   * @param objectMetadata
   * @return
   */
  public PutObjectResult putObjectToS3ByFileWithObjectMetadata(
      String bucket,
      String s3Path,
      String fileName,
      File file,
      StorageClass storageClass,
      CannedAccessControlList cannedAccessControlList,
      ObjectMetadata objectMetadata
  ) {
    String storagePath = s3Path + File.separator + fileName;

    PutObjectRequest putObjectRequest = new PutObjectRequest(
        bucket,
        storagePath,
        file
    );

    putObjectRequest.setMetadata(objectMetadata);
    putObjectRequest.setStorageClass(storageClass);
    putObjectRequest.setCannedAcl(cannedAccessControlList);

    return amazonS3Client.putObject(putObjectRequest);
  }

  /**
   * S3 에서 File 을 읽어 byteArray 로 데이터를 사용해야 할 경우
   *
   * @param bucket
   * @param fullPath
   * @return
   */
  public byte[] getByteArrayFromS3(
      String bucket,
      String fullPath
  ) {
    InputStream inputStream = null;
    try {
      String s3Path = fullPath;
      if (fullPath.indexOf(SERVICE_DOMAIN_NAME + File.separator) != -1) {
        s3Path = fullPath.substring(
            fullPath.indexOf(SERVICE_DOMAIN_NAME + File.separator)
            +
            (SERVICE_DOMAIN_NAME + File.separator).length()
        );
      }
      inputStream = amazonS3Client.getObject(new GetObjectRequest(bucket, s3Path)).getObjectContent();
      return IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("S3Helper : getByteArrayFromS3 -> " + e.getMessage());
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          throw new RuntimeException("S3Helper : getByteArrayFromS3 -> " + e.getMessage());
        }
      }
    }
  }

  public String getS3BucketName(
      String type
  ) {
    if (awsConfig.getRegions().equalsIgnoreCase(TEST_REGION_NAME)) {
      type += "-test";
    }
    return type;
  }

  public String getS3BucketURL(
      String type
  ) {
    String post = SERVICE_DOMAIN_NAME;
    if (awsConfig.getRegions().equalsIgnoreCase(TEST_REGION_NAME)) {
      post = "-test" + SERVICE_DOMAIN_NAME;
    }
    return "https://" + type + post;
  }
}
