package me.kidult.Config.AWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by minisv on 2017. 4. 7..
 */
@Component
@Configuration
public class AWSContextConfig implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AWSContextConfig.class);

  @Autowired
  private AWSConfig awsConfig;

  @Override
  public void afterPropertiesSet() throws Exception {

  }
}
