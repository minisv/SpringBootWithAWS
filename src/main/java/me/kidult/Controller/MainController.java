package me.kidult.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by minisv on 2017. 3. 8..
 */
@Controller
public class MainController {

  private static final Logger logger = LoggerFactory.getLogger(MainController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    logger.info("log");
    return "index";
  }

  @RequestMapping(value = "/swagger", method = RequestMethod.GET)
  public String swagger() {
    return "swagger-ui/index";
  }

}
