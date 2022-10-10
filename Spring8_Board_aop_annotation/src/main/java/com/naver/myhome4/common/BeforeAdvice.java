package com.naver.myhome4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
/*
 ȣ��Ǵ� ����Ͻ� �޼����� ������ JoinPoint �������̽��� �� �� �ֽ��ϴ�

  *JoinPoint �������̽��� �޼���
 1. Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�
 2. Object getTarget() : ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 3. getClass().getName() : Ŭ���� �̸��� ���մϴ�.
 4. proceeding.getSignature().getName() : ȣ��Ǵ� �޼��� �̸��� ���մϴ�
 */
import org.springframework.stereotype.Service;

//�������� ó���� ������ BeforeAdvice Ŭ������ beforeLog()�޼���� �����մϴ�
//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��մϴ�
//��� ��ü�� �޼ҵ带 �����ϴ� ���߿� �ͼ����� �߻��ߴ����� ���ο� ������� �޼��� ���� �� ���� ����� �����մϴ�. 


//@Service
//@Aspect 
public class BeforeAdvice {
	
	/*
	 	@Pointcut�� �����մϴ�. 
	 	�ϳ��� Advice Ŭ���� �ȿ� �������� ����Ʈ ���� ������ �� �ֽ��ϴ�. �������� ����Ʈ ����
	 	�ĺ��ϱ� ���� ���� �޼��带 �̿��մϴ�. 
	 	�� �޼���� ��ü�� ����ִ� �޼���� �ܼ��� ����Ʈ ���� �ĺ��ϱ� ���� �̸����θ� ���˴ϴ�.
	 
	*/

	
	@Pointcut("execution(* com.naver.myhome4..*Impl.get*(..))")
	public void getPointcut() {	}
	
	/*
	 @Before : �����Ͻ� �޼��� ���� ���� �����մϴ�.
	 @Before("getPointcut()") : getPointcut() ���� �޼���� ������ �޼��尡 ���� ����
	 							Advice�� �޼��� beforeLog()�� ȣ��˴ϴ�.
	 								
	*/
	@Before("getPointcut()") 
	public void beforeLog(JoinPoint proceeding) {

		/*
		 * ��� ���� [BeforeAdvice] : ����Ͻ� ���� ���� �� �����Դϴ� [BeforeAdvice] :
		 * com.naver.myhome4.service.BoardServiceImpl�� getBoardList ȣ���մϴ�
		 */
		System.out.println("=============================================");
		System.out.println("[BeforeAdvice] : ����Ͻ� ���� ���� �� �����Դϴ�.");
		System.out.println("[BeforeAdvice] : " + proceeding.getTarget().getClass().getName() + "��"
				+ proceeding.getSignature().getName() + "ȣ���մϴ�.");
		System.out.println("=============================================");
	}
}