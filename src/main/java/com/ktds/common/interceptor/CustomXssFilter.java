package com.ktds.common.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.nhncorp.lucy.security.xss.XssFilter;

public class CustomXssFilter implements Filter {

	public CustomXssFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Map<String, String[]> requestParams = request.getParameterMap();

		XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml");

		// parameter 츨력
		requestParams.entrySet().stream().forEach(entry -> {
			entry.getValue()[0] = filter.doFilter(entry.getValue()[0]);

			System.out.println(entry.getKey());
			System.out.println(entry.getValue()[0]);
			System.out.println("---------------------------");

		});

		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
