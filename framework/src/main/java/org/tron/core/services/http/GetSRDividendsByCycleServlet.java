package org.tron.core.services.http;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.core.Wallet;
import org.tron.protos.Protocol.Account;

@Component
@Slf4j(topic = "API")
public class GetSRDividendsByCycleServlet extends RateLimiterServlet {

  @Autowired
  private Wallet wallet;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      byte[] address = Util.getAddress(request);
      long startCycle = Long.parseLong(request.getParameter("startCycle"));
      long endCycle = Long.parseLong(request.getParameter("endCycle"));
      fillResponse(address, startCycle, endCycle, response);
    } catch (Exception e) {
      Util.processError(e, response);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      PostParams params = PostParams.getPostParams(request);
      Account.Builder build = Account.newBuilder();
      JsonFormat.merge(params.getParams(), build, params.isVisible());
      JSONObject jsonObject = JSONObject.parseObject(params.getParams());
      long startCycle = jsonObject.getLong("startCycle");
      long endCycle = jsonObject.getLong("endCycle");
      byte[] address = build.getAddress().toByteArray();
      fillResponse(address, startCycle, endCycle, response);
    } catch (Exception e) {
      Util.processError(e, response);
    }
  }

  private void fillResponse(byte[] address, long start, long end, HttpServletResponse response)
      throws IOException {
    if (start <= end && address != null) {
      HashMap<String, Long> value = wallet.queryRewardByCycle(address, start, end);
      response.getWriter().println(Util.printMapToJSON(value));
    } else {
      response.getWriter().println("{}");
    }
  }
}
