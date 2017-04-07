package me.kidult.Filter;

import me.kidult.Config.ElasticSearch.ElasticSearchConfig;
import me.kidult.Util.AWS.KinesisFirehoseHelper;
import me.kidult.Util.ElasticSearch.ElasticSearchMessageFormat;
import me.kidult.Util.ElasticSearch.ElasticSearchObject;
import me.kidult.Util.JsonHelper;
import org.apache.http.NoHttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by minisv on 2017. 4. 7..
 */
public class ElasticSearchFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(ElasticSearchFilter.class);

  @Autowired
  private ElasticSearchConfig elasticSearchConfig;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
        this,
        filterConfig.getServletContext()
    );
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain filterChain
  ) throws IOException, ServletException {
    try {
      filterChain.doFilter(request, response);

      ElasticSearchObject elasticSearchObject = new ElasticSearchObject(
          elasticSearchConfig.getIndex(),
          "kinesis",
          new ElasticSearchMessageFormat(request, response)
      );

      String logString = JsonHelper.toJson(elasticSearchObject).replace("timestamp", "@timestamp");
      KinesisFirehoseHelper.getInstance().putItem(logString);

    } catch (
        NullPointerException |
        NoHttpResponseException e
      ) {
    } finally {
    }
  }

  @Override
  public void destroy() {}

}
