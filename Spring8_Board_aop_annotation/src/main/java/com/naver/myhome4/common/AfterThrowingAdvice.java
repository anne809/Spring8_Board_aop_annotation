package com.naver.myhome4.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/*
 * JoinPoint �������̽��� �޼���
 * Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
 * Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 * Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���ڸ���� Object �迭�� �����մϴ�.
 * 
 * Advice : Ⱦ�ܰ��ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
 * After Throwing (���� �߻������� ����)
 * - Ÿ�� �޼ҵ尡 ������ ���ܸ� ������ �Ǹ� �����̽� ����� ����
 * BoardServiceImpl.java���� getBoardList() �ȿ� double i=1/0; �߰��մϴ�. 
 * */
@Service
@Aspect 
public class AfterThrowingAdvice {
	
	
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {}
	
	@AfterThrowing(pointcut="getPointcut()", throwing="exp")
	public void afterThrowingLog(Throwable exp) {
		System.out.println("=============================================");
		System.out.println("[AfterThrowing] : ����Ͻ� ���� ������ ������ " + "�߻��ϸ� �����Դϴ�." );
		System.out.println("ex : " +exp.toString());
		System.out.println("=============================================");
	}
	

}
