package com.naver.myhome4.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naver.myhome4.domain.Comment;
import com.naver.myhome4.service.CommentService;

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	/*
	 * �ۼ� ������ ���Կ� ��Ʈ�ѷ��� ���� ���񽺸� ��ġ�� �ٿ��� ��ġ�� �׸��� ������ �ܰ迡�� xml ! �������� �ִ°� ���ڳ��� �ű�� ����
	 * 
	 * ��Ʈ�ѷ� ȭ�� �ϳ� ����ΰ� ������� ���� ����ΰ� �ٿ� ����ΰ� mapper�����̶�� �ϴ� ������ �ۼ�����(xml) �̷��� �װ��� ��
	 * �ѵμž� �ؿ�
	 * 
	 * ��͸� �ͼ������� �Ǿ���,,,
	 * 
	 * ��Ʈ�ѷ�-����-�ٿ�-xml
	 * 
	 * 
	 * 
	 * 
	 */

	/*
	 * @ResponseBody �޼��忡 @ResponseBody Annotation�� �Ǿ������� return�Ǵ� ���� view�� ���ؼ� ��µǴ�
	 * ���� �ƴ϶� HTTP ResponseBody�� ���� �������� �˴ϴ�. ��)HTTP ���� ���������� ���� HTTP/1.1 Message
	 * Header 200OK => Stat Line Content-Type:text/html => Message Header Connection
	 * : close Server : Apache Tomcat ... Last-Modified : Mon,...
	 * 
	 * Message Body <html> <head><title>Hello JSP</title></head> <body>Hello JSP!
	 * </body> </html> =>
	 * 
	 * ���� ����� HTML�� �ƴ� JSON���� ��ȯ�Ͽ� Message Body�� �����Ϸ��� ���������������ϴ� ��ȯ��(converter)��
	 * ����ؾ��մϴ�. �� ��ȯ�⸦ �̿��ؼ� �ڹ� ��ü�� �پ��� Ÿ������ ��ȯ�Ͽ� HTTP Response Body�� ������ �� �ֽ��ϴ�. ������
	 * ���� ���Ͽ� <mvc:annotation-driven>�� �����ϸ� ��ȯ�Ⱑ �����˴ϴ�.
	 * 
	 * ���߿��� �ڹ� ��ü�� JSON ���� �ٵ�� ��ȯ�� ���� MappingJackson2HttpMessageConverter�� ����մϴ�.
	 * 
	 * ResponseBody�� �̿��ؼ� �� �޼����� ���� ����� JSON���� ��ȯ�Ǿ� HTTP Response BODY�� �����˴ϴ�.
	 */

	@ResponseBody
	@PostMapping(value = "CommentList.bo")
	public List<Comment> CommentList(@RequestParam("board_num") int board_num,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page) {
		List<Comment> list = commentService.getCommentList(board_num, page);
		return list;
	}

	@PostMapping(value = "CommentAdd.bo")
	public void CommentAdd(Comment co, HttpServletResponse response) throws Exception {
		int ok = commentService.commentsInsert(co); // ��Ʈ�� �ϰ� commentsInsert Ŭ���ϸ� impl�� �Ѿ����ϴµ�,,,? ��
		response.getWriter().print(ok);

	}

	@PostMapping(value = "CommentDelete.bo")
	public void CommentDelete(int num, HttpServletResponse response) throws Exception {
		// int num => Integer.parseInt(request.getParameter("num"));
		int result = commentService.commentsDelete(num);
		response.getWriter().print(result);
	}

	@PostMapping(value = "CommentUpdate.bo")
	public void CommentUpdate(Comment co, HttpServletResponse response) throws Exception {
		int ok = commentService.commentsUpdate(co);
		response.getWriter().print(ok);
	}

}
