package com.naver.myhome4.task;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.naver.myhome4.domain.MailVO;

@Component
public class SendMail {

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Value("${sendfile}")
	private String sendfile;

	public void sendMail(MailVO vo) {
		MimeMessagePreparator mp = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				// 두 번쨰 인자 true는 멀티파트 메시지를 사용하겠다는 미입니다.
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				helper.setFrom(vo.getFrom());
				helper.setTo(vo.getTo());
				helper.setSubject(vo.getSubject());

				// 1. 문자로만 전송하는 경우
				// helper.setText(vo.getContent(), true);
				// 두번쨰 인자는 html을 사용하겠다는 뜻입니다.

				// 2. 이미지를 내장해서 보내는 경우, cid:Home이 하단 helper.addInline("Home", file); 으로 들어갑니다.
				String content = "<img src='cid:Home'>" + vo.getContent();
				helper.setText(content, true);

				FileSystemResource file = new FileSystemResource(new File(sendfile));

				helper.addInline("Home", file);

				// 3. 파일을 첨부해서 보내는 경우
				FileSystemResource file1 = new FileSystemResource(new File(sendfile));

				// 첫번째 인자 : 첨부될 파일의 이름입니다.
				// 두번쨰 인자 : 첨부파일
				helper.addAttachment("mari.jpg", file1);

			}

		};
		mailSender.send(mp); // 메일 전송합니다.
		System.out.println("메일 전송했습니다.");
	}
}
