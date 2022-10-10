package com.naver.myhome4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/*
 Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
 Advice Ŭ������ ������ ���� ���Ͽ��� <bean> ���� ����ϰų� @Service annotation�� ����մϴ�.
 @Aspect�� ������ Ŭ�������� Pointcut�� Advice�� �����ϴ� ������ �־���մϴ�.
*/
//@Service //�ٸ��ʿ��� ������̶� �ּ�ó����
//@Aspect 
//�������� ó���� ������ AfterAdviceŬ������ afterLog() �޼���� �����մϴ�.
public class AfterAdvice {
      
		/*
		 @Pointcut�� �����մϴ�. �ϳ��� Advice Ŭ���� �ȿ� �������� ����Ʈ ���� ������ �� �ֽ��ϴ�. �������� ����Ʈ ����
		 	�ĺ��ϱ� ���� ���� �޼��带 �̿��մϴ�. �� �޼���� ��ü�� ����ִ� �޼���� �ܼ��� ����Ʈ ���� �ĺ��ϱ� ���� �̸����θ� ���˴ϴ�.
		 
		*/
	//Pointcut
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {
		
	}
	/*
	 * @After : ����Ͻ� �޼��� ���� �Ŀ� �����մϴ�.
	 * 
	 * @After("getPointcut()") : getPointcut() ���� �޼���� ������ �޼��尡 ���� �Ŀ� Advice�� �޼��� afterLog()�� ȣ��˴ϴ�.
	 * 
	 * 
	 * Advice*/
	 @After("getPointcut()")
	  public void afterLog(JoinPoint proceeding) {
      System.out.println("=============================================");
      System.out.println("[AfterAdvice] : ����Ͻ� ���� ���� �� �����Դϴ�.");
      System.out.println("[AfterAdvice] : " + proceeding.getTarget().getClass().getName() + "��"
            + proceeding.getSignature().getName() + " ȣ���մϴ�.");
      System.out.println("=============================================");
   }
}