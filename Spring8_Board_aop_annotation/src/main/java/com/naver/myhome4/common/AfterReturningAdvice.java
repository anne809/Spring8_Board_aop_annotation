package com.naver.myhome4.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

//JoinPoint �������̽��� �޼���
//Signature getSignature() : ȣ�ϵǴ� �޼��忡 ���� ������ ���մϴ�.
//Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
//Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.

//Advice : Ⱦ�� ���ɿ� �ش��ϴ� �������� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
//AfterReturningAdvice : Ÿ�� �޼ҵ尡 ���������� ������� ��ȯ �Ŀ� �����̽� ����� ����


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