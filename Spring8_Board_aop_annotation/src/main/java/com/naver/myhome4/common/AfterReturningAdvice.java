package com.naver.myhome4.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

//JoinPoint 인터페이스의 메서드
//Signature getSignature() : 호완되는 메서드에 대한 정보를 구합니다.
//Object getTarget() : 클라이언트가 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
//Object[] getArgs() : 클라이언트가 메서드를 호출할 떄 넘겨준 인자 목록을 Object 배열로 리턴합니다.

//Advice : 횡단 관심에 해당하는 공통기능을 의미하며 독립된 클래스의 메서드로 작성됩니다.
//AfterReturningAdvice : 타겟 메소드가 성공적으로 결과값을 반환 후에 어드바이스 기능을 수행


//@Service
//@Aspect 
public class AfterReturningAdvice{
	
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {}
	
	
		@AfterReturning(pointcut="getPointcut()", returning="obj")
		public void afterReturningLog(Object obj) {
			System.out.println("=============================================");
			System.out.println("[AfterReturningAdvice] OBJ : " + obj.toString());
			System.out.println("=============================================");
			
		}
	}