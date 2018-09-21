package com.ktds.common.exceptions.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.ktds.common.exceptions.PolicyViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	public String noHandlerFoundExceptionHandler() {
		return "error/404";
	}

	
/*	@ExceptionHandler(PolicyViolationException.class)
	public String policyViolationExceptionHandler(PolicyViolationException e)
				throws UnsupportedEncodingException{
		
		System.out.println("에리가 발생함");
		
		return "redirect:" 
				+ e.getRedirectUri() 
				+ "?message=" 
				+ URLEncoder.encode(e.getMessage(), "UTF-8");
	}*/
	
	@ExceptionHandler(RuntimeException.class)
	public String runtimeException(RuntimeException e)
			throws UnsupportedEncodingException {

		e.printStackTrace();
		
		if ( e instanceof PolicyViolationException ) {
			PolicyViolationException pve = (PolicyViolationException) e;
			
			System.out.println("에리가 발생함");
			
			return "redirect:" 
					+ pve.getRedirectUri() 
					+ "?message=" 
					+ URLEncoder.encode(e.getMessage(), "UTF-8");
		}
		
		//에러 출력하기
		return "error/500";
	}
	
	
	
	
}














