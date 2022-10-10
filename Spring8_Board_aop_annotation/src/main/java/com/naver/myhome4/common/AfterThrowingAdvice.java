package com.naver.myhome4.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/*
 * JoinPoint 인터페이스의 메서드
 * Signature getSignature() : 호출되는 메서드에 대한 정보를 구합니다.
 * Object getTarget() : 클라이언트가 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
 * Object[] getArgs() : 클라이언트가 메서드를 호출할 떄 넘겨준 인자목록을 Object 배열로 리턴합니다.
 * 
 * Advice : 횡단관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성됩니다.
 * After Throwing (예외 발생했을때 실행)
 * - 타겟 메소드가 수행중 예외를 던지게 되면 어드바이스 기능을 수행
 * BoardServiceImpl.java에서 getBoardList() 안에 double i=1/0; 추가합니다. 
 * */
@Service
@Aspect 
public class AfterThrowingAdvice {
	
	
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {}
	
	@AfterThrowing(pointcut="getPointcut()", throwing="exp")
	public void afterThrowingLog(Throwable exp) {
		System.out.println("=============================================");
		System.out.println("[AfterThrowing] : 비즈니스 로직 수행중 오류가 " + "발생하면 동작입니다." );
		System.out.println("ex : " +exp.toString());
		System.out.println("=============================================");
	}
	

}
