package com.naver.myhome4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
/*
 호출되는 비즈니스 메서드의 정보를 JoinPoint 인터페이스로 알 수 있습니다

  *JoinPoint 인터페이스의 메서드
 1. Signature getSignature() : 호출되는 메서드에 대한 정보를 구합니다
 2. Object getTarget() : 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
 3. getClass().getName() : 클래스 이름을 구합니다.
 4. proceeding.getSignature().getName() : 호출되는 메서드 이름을 구합니다
 */
import org.springframework.stereotype.Service;

//공통으로 처리할 로직을 BeforeAdvice 클래스에 beforeLog()메서드로 구현합니다
//Advice : 횡단 관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성합니다
//대상 객체의 메소드를 실행하는 도중에 익셉션이 발생했는지의 여부에 상관없이 메서드 실행 후 공통 기능을 실행합니다. 


//@Service
//@Aspect 
public class BeforeAdvice {
	
	/*
	 	@Pointcut을 설정합니다. 
	 	하나의 Advice 클래스 안에 여러개의 포인트 컷을 선언할 수 있습니다. 여러개의 포인트 컷을
	 	식별하기 위해 참조 메서드를 이용합니다. 
	 	이 메서드는 몸체가 비어있는 메서드로 단순히 포인트 컷을 식별하기 위한 이름으로만 사용됩니다.
	 
	*/

	
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {	}
	
	/*
	 @Before : 비지니스 메서드 실행 전에 동작합니다.
	 @Before("getPointcut()") : getPointcut() 참조 메서드로 지정한 메서드가 실행 전에
	 							Advice의 메서드 beforeLog()가 호출됩니다.
	 								
	*/
	@Before("getPointcut()") 
	public void beforeLog(JoinPoint proceeding) {

		/*
		 * 출력 내용 [BeforeAdvice] : 비즈니스 로직 수행 전 동작입니다 [BeforeAdvice] :
		 * com.naver.myhome4.service.BoardServiceImpl의 getBoardList 호출합니다
		 */
		System.out.println("=============================================");
		System.out.println("[BeforeAdvice] : 비즈니스 로직 수행 전 동작입니다.");
		System.out.println("[BeforeAdvice] : " + proceeding.getTarget().getClass().getName() + "의"
				+ proceeding.getSignature().getName() + "호출합니다.");
		System.out.println("=============================================");
	}
}