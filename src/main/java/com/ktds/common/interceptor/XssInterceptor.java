package com.ktds.common.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nhncorp.lucy.security.xss.XssFilter;

public class XssInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 * 모든 parameter를 가져와 XSS Filtering 수행
		 */
		
		Map<String, String[]> requestParams = request.getParameterMap();
		
		XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml");
				
				
		//parameter 츨력
		requestParams.entrySet().stream().forEach(entry -> {
			entry.getValue()[0] = filter.doFilter(entry.getValue()[0]);
			
			System.out.println(entry.getKey());
			System.out.println(entry.getValue()[0]);
			System.out.println("---------------------------");
			
			
		});
		
		
		return true;
	}
}
