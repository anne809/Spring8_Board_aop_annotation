package com.naver.myhome4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/*
 Advice : 횡단 관심에 해당하는 공통 기능의 의미하며 독립된 클래스의 메서드로 작성됩니다.
 Advice 클래스는 스프링 설정 파일에서 <bean> 으로 등록하거나 @Service annotation을 사용합니다.
 @Aspect가 설정된 클래스에는 Pointcut과 Advice를 결합하는 설정이 있어야합니다.
*/
//@Service //다른쪽에서 사용중이라 주석처리함
//@Aspect 
//공통으로 처리할 로직을 AfterAdvice클래스에 afterLog() 메서드로 구현합니다.
public class AfterAdvice {
      
		/*
		 @Pointcut을 설정합니다. 하나의 Advice 클래스 안에 여러개의 포인트 컷을 선언할 수 있습니다. 여러개의 포인트 컷을
		 	식별하기 위해 참조 메서드를 이용합니다. 이 메서드는 몸체가 비어있는 메서드로 단순히 포인트 컷을 식별하기 위한 이름으로만 사용됩니다.
		 
		*/
	//Pointcut
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {
		
	}
	/*
	 * @After : 비즈니스 메서드 실행 후에 동작합니다.
	 * 
	 * @After("getPointcut()") : getPointcut() 참조 메서드로 지정한 메서드가 실행 후에 Advice의 메서드 afterLog()가 호출됩니다.
	 * 
	 * 
	 * Advice*/
	 @After("getPointcut()")
	  public void afterLog(JoinPoint proceeding) {
      System.out.println("=============================================");
      System.out.println("[AfterAdvice] : 비즈니스 로직 수행 후 동작입니다.");
      System.out.println("[AfterAdvice] : " + proceeding.getTarget().getClass().getName() + "의"
            + proceeding.getSignature().getName() + " 호출합니다.");
      System.out.println("=============================================");
   }
}