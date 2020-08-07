package org.tron.core.services.filter;

import com.beust.jcommander.internal.Sets;
import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tron.common.parameter.CommonParameter;

@Component
@Slf4j(topic = "API")
public class LiteFnQueryHttpFilter implements Filter {

  public static Set<String> filterPaths = Sets.newHashSet();

  static {
    // base path: /wallet
    filterPaths.add("/wallet/getblockbyid");
    filterPaths.add("/wallet/getblockbylatestnum");
    filterPaths.add("/wallet/getblockbylimitnext");
    filterPaths.add("/wallet/getblockbynum");
    filterPaths.add("/wallet/getmerkletreevoucherinfo");
    filterPaths.add("/wallet/gettransactionbyid");
    filterPaths.add("/wallet/gettransactioncountbyblocknum");
    filterPaths.add("/wallet/gettransactioninfobyid");
    filterPaths.add("/wallet/gettransactionreceiptbyid");
    filterPaths.add("/wallet/isspend");
    filterPaths.add("/wallet/scanandmarknotebyivk");
    filterPaths.add("/wallet/scannotebyivk");
    filterPaths.add("/wallet/scannotebyovk");
    filterPaths.add("/wallet/totaltransaction");

    // base path: /walletsolidity
    filterPaths.add("/walletsolidity/getblockbyid");
    filterPaths.add("/walletsolidity/getblockbylatestnum");
    filterPaths.add("/walletsolidity/getblockbylimitnext");
    filterPaths.add("/walletsolidity/getblockbynum");
    filterPaths.add("/walletsolidity/getmerkletreevoucherinfo");
    filterPaths.add("/walletsolidity/gettransactionbyid");
    filterPaths.add("/walletsolidity/gettransactioncountbyblocknum");
    filterPaths.add("/walletsolidity/gettransactioninfobyid");
    filterPaths.add("/walletsolidity/isspend");
    filterPaths.add("/walletsolidity/scanandmarknotebyivk");
    filterPaths.add("/walletsolidity/scannotebyivk");
    filterPaths.add("/walletsolidity/scannotebyovk");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    String requestPath = ((HttpServletRequest) servletRequest).getRequestURI();
    boolean shouldBeFiltered = false;
    if (CommonParameter.getInstance().isLiteFullNode
            && !CommonParameter.getInstance().openHistoryQueryWhenLiteFN) {
      if (filterPaths.contains(requestPath)) {
        shouldBeFiltered = true;
      }
    }
    if (shouldBeFiltered) {
      servletResponse.getWriter().write("this API is closed because this node is a lite fullnode");
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {

  }
}
