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
	 * 작성 순서를 볼게욥 컨트롤러에 가서 서비스를 거치고 다오를 거치고 그리고 마지막 단계에서 xml ! 쿼리문이 있는거 있자나여 거기로 가져
	 * 
	 * 컨트롤러 화면 하나 열어두고 순서대로 서비스 열어두고 다오 열어두고 mapper파일이라고 하는 쿼리문 작성파일(xml) 이렇게 네개를 꼭
	 * 켜두셔야 해용
	 * 
	 * 요것만 익숙해지면 되어유,,,
	 * 
	 * 컨트롤러-서비스-다오-xml
	 * 
	 * 
	 * 
	 * 
	 */

	/*
	 * @ResponseBody 메서드에 @ResponseBody Annotation이 되어있으면 return되는 값은 view를 통해서 출력되는
	 * 것이 아니라 HTTP ResponseBody에 직접 쓰여지게 됩니다. 예)HTTP 응답 프로토콜의 구조 HTTP/1.1 Message
	 * Header 200OK => Stat Line Content-Type:text/html => Message Header Connection
	 * : close Server : Apache Tomcat ... Last-Modified : Mon,...
	 * 
	 * Message Body <html> <head><title>Hello JSP</title></head> <body>Hello JSP!
	 * </body> </html> =>
	 * 
	 * 응답 결과를 HTML이 아닌 JSON으로 변환하여 Message Body에 저장하려면 스프링에서제공하는 변환기(converter)를
	 * 사용해야합니다. 이 변환기를 이용해서 자바 객체를 다양한 타입으로 변환하여 HTTP Response Body에 설정할 수 있습니다. 스프링
	 * 설정 파일에 <mvc:annotation-driven>을 설정하면 변환기가 생성됩니다.
	 * 
	 * 이중에서 자바 객체를 JSON 응답 바디로 변환할 때는 MappingJackson2HttpMessageConverter를 사용합니다.
	 * 
	 * ResponseBody를 이용해서 각 메서드의 실행 결과는 JSON으로 변환되어 HTTP Response BODY에 설정됩니다.
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
		int ok = commentService.commentsInsert(co); // 컨트롤 하고 commentsInsert 클릭하면 impl로 넘어어가야하는데,,,? 잉
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
