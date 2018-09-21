package com.ktds.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoParamAop {
	
	private Logger logger = LoggerFactory.getLogger(DaoParamAop.class);
	
	
	
	//around 영역
	public Object getParam(ProceedingJoinPoint pjp) {
		

		//어떤 클래스의 어떤 메소드를 실행할 것인지 확인한다.
		String classAndMethod = pjp.getSignature().toShortString();
		
		//before 영역 
		//parameter 가져오기
		Object[] paramArray = pjp.getArgs();
		
		for (Object param : paramArray) {
			
			logger.debug(classAndMethod + " = " + param.toString());
			
			
		}
		
		//기존의 메소드 실행
		Object result = null;
		try {
			
			//before 영역
			//결과가 무엇인지
			logger.debug("Before");
			result = pjp.proceed();
			
			//after-returning
			logger.debug("after-returning");
			logger.debug(classAndMethod + " = " + "Result: " + result.toString());
			
		} catch (Throwable e) {
			//after-throwing 영역
			logger.debug("afterThrowing");
			logger.debug(classAndMethod + " = " + "Exception: " + e.getCause().toString() + ", " + e.getMessage());

			throw new RuntimeException(e.getMessage(), e);
			
		} /*finally {
			//after 영역
			logger.debug("after");
		}*/
		
		//after-returning 영역
		//logger.debug("afterReturning");
		return result;
	}

}
