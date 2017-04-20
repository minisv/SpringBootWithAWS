package me.kidult.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by minisv on 2017. 3. 8..
 */
@Controller
public class MainController {

  private static final Logger logger = LoggerFactory.getLogger(MainController.class);

  @RequestMapping("/")
  public String index() {
    logger.info("log");
    return "index";
  }

  @RequestMapping(value = "/swagger")
  public String swagger() {
    return "swagger-ui/index";
  }

}
