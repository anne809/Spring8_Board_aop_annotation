package com.naver.myhome4.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myhome4.domain.MailVO;
import com.naver.myhome4.domain.Member;
import com.naver.myhome4.service.MemberService;
/*
	@Component�� �̿��ؼ� ������ �����̳ʰ� �ش� Ŭ���� ��ü�� �����ϵ��� ������ �� ������
	��� Ŭ������ @Component�� �Ҵ��ϸ� � Ŭ������ � ������ �����ϴ��� �ľ��ϱ�
	��ƽ��ϴ�. ������ �����ӿ�ũ������ �̷� Ŭ�������� �з��ϱ� ���ؼ� @Component�� ����Ͽ� ������ ���� �� ���� �ֳ����̼��� �����մϴ�.
	
	1. @Controller - ������� ��û�� �����ϴ� Controller Ŭ����
	2. @Respository - ������ ���̽� ������ ó���ϴ� DAO Ŭ����
    3. @Service - �����Ͻ� ������ ó���ϴ� Service Ŭ����




	 @�帧
		MemberController
			->MemberService->
				->MemberServiceImpl->
					->MemberDAO
*/
import com.naver.myhome4.task.SendMail;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberservice; // MemberService�� �̵��ؼ� ����

	@Autowired // home4.task ->SendMail
	private SendMail sendMail;

	@Autowired // ��й�ȣ ��ȣȭ
	private PasswordEncoder passwordEncoder;

	/*
	 * @CookieValue(value="saveid", required=false) Cookie readCookie �̸��� saveid��
	 * ��Ű�� Cookie Ÿ������ ���� �޽��ϴ�. ������ �̸��� ��Ű�� �������� ���� ���� �ֱ� ������ required=false��
	 * �����ؾ��մϴ�. ��, id ����ϱ⸦ �������� ���� ���� �ֱ� ������ required=false�� �����ؾ��մϴ�. required=true
	 * ���¿��� ������ �̸��� ���� ��Ű�� �������� ������ ������ MVC�� �ͼ����� �߻���ŵ�ϴ�.
	 * 
	 */

	// �α��� �� �̵�
	@RequestMapping(value = "/login.net", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mv, @CookieValue(value = "saveid", required = false) Cookie readCookie)
			throws Exception {

		if (readCookie != null) {
			mv.addObject("saveid", readCookie.getValue());

			System.out.println("cookie time = " + readCookie.getMaxAge());
		}

		mv.setViewName("member/loginForm");
		return mv;
	}

	// ���������� �̵��ϴ°�
	// ȸ������ �� �̵�
	@RequestMapping(value = "/join.net", method = RequestMethod.GET)
	public String join() {
		return "member/joinForm"; // WEB-INF/views/member/joinForm.jsp
	}

	// ȸ������ó��
	@RequestMapping(value = "/joinProcess.net", method = RequestMethod.POST)
	public void joinProcess(Member member, HttpServletResponse response) throws Exception {

		// Member member ������ �޾ƿԾ�� �����ϰ� command��ü��.. ����
		// �츰 ���̻� new MemberDAO�� ���� �ʽ��ϴ�.
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// ��й�ȣ ��ȣȭ �߰�
		String encPassword = passwordEncoder.encode(member.getPassword());
		System.out.println(encPassword);
		member.setPassword(encPassword);// �̰ɷ� �����Ұſ���

		int result = memberservice.insert(member); // �׸��� ���⼭ Ȯ���Ұſ���
		out.println("<script>");

		// ������ �� ���
		if (result == 1) {

			MailVO vo = new MailVO();
			vo.setTo(member.getEmail()); // ���� �����ϴ�.
			vo.setContent(member.getId() + "�� ȸ�������� ���ϵ帳�ϴ�.");
			sendMail.sendMail(vo);

			out.println("alert('ȸ�������� �����մϴ�.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���.');");
			// out.println("location.href='join.net';"); ���ΰ�ħ�Ǿ� ������ �Է��� �����Ͱ� ��Ÿ���� �ʽ��ϴ�.
			out.println("history.back()");// ��й�ȣ�� ������ �ٸ� �����ʹ� �����Ǿ� �ֽ��ϴ�.
		}
		out.println("</script>");
		out.close();

	}

	// ȸ������������ ���̵� �˻� (joinForm.jsp�� ��� �������� idcheck.net)
	@RequestMapping(value = "/idcheck.net", method = RequestMethod.GET)
	public void idcheck(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		int result = memberservice.isId(id); // -> MemberServiceImpl �� �̵�
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	// �α��� ó��
	@RequestMapping(value = "/loginProcess.net", method = RequestMethod.POST)
	public String loginProcess(@RequestParam("id") String id, // �α��ΰ� �Ѿ�°�
			@RequestParam("password") String password,
			@RequestParam(value = "remember", defaultValue = "") String remember, // üũ�ϴ°ſ���. �⺻�� ��Ʈ���̴ϱ� ������ �־������ϴ�.
			HttpServletResponse response, HttpSession session) throws Exception {

		int result = memberservice.isId(id, password); // �ΰ� �Ѱ��ݴϴ�.
		System.out.println("����� " + result);

		if (result == 1) { // ����� ���� ������ ������ ó���Ѱ̴ϴ�. ������� üũ�� ���� �ƴ� ��� �ΰ����� �����Ұ̴ϴ�.

			// �α��� ����
			session.setAttribute("id", id);
			Cookie savecookie = new Cookie("saveid", id);
			if (!remember.equals("")) {
				savecookie.setMaxAge(60 * 60);
				System.out.println("��Ű���� : 60*60"); // ������ ����Դϴ�.

			} else {
				System.out.println("��Ű���� : 0");
				savecookie.setMaxAge(0); // ���� �ȵ� ����Դϴ�.
			}
			response.addCookie(savecookie); // ��� ���� ����ϴ�.
			return "redirect:BoardList.bo"; // �׸��� �����մϴ�. -> BoardController�� �̵�~

		} else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if (result == -1)
				message = "���̵� �������� �ʽ��ϴ�.";

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + message + "');");
			out.println("location.href='login.net';");
			out.println("</script>");
			out.close();
			return null;

		}
	}

	// �α׾ƿ�

	@RequestMapping(value = "/logout.net", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:login.net";

	}

	// ȸ���� ��������

	@RequestMapping(value = "/member_info.net", method = RequestMethod.GET)
	public ModelAndView member_info(@RequestParam("id") String id, ModelAndView mv) throws Exception {
		Member m = memberservice.member_info(id);
		mv.setViewName("member/member_info");
		mv.addObject("memberinfo", m);
		return mv;
	}

	// ������
	@RequestMapping(value = "/member_update.net", method = RequestMethod.GET)
	public ModelAndView member_update(HttpSession session, ModelAndView mv) throws Exception {
		String id = (String) session.getAttribute("id");
		Member m = memberservice.member_info(id);
		mv.setViewName("member/updateForm");
		mv.addObject("memberinfo", m);
		return mv;
	}

	// ����ó��
	@RequestMapping(value = "/updateProcess.net", method = RequestMethod.POST)
	public void updateProcess(Member member, HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = memberservice.update(member);
		out.println("<script>");

		// ������ �� ���
		if (result == 1) {
			out.println("alert('�����Ǿ����ϴ�.')");
			out.println("location.href='BoardList.bo';");
		} else {
			out.println("alert('ȸ�� ���� ������ �����߽��ϴ�.');");
			out.println("history.back()"); // ��й�ȣ�� ������ �ٸ� �����ʹ� ���� �Ǿ� �ֽ��ϴ�
		}
		out.println("</script>");
		out.close();

	}

	// �˻� ����Ʈ
	// @GetMapping(value = "/member_list.net")
	@RequestMapping(value = "/member_list.net")

	public ModelAndView memberList(

			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "3", required = false) int limit, ModelAndView mv,
			@RequestParam(value = "search_field", defaultValue = "-1") int index,
			@RequestParam(value = "search_word", defaultValue = "") String search_word) throws Exception {

		List<Member> list = null;
		int listcount = 0;

		list = memberservice.getSearchList(index, search_word, page, limit);
		listcount = memberservice.getSearchListCount(index, search_word); // �� ����Ʈ ���� �޾ƿ�

		// �� ������ ��
		int maxpage = (listcount + limit - 1) / limit;

		// ���� �������� ������ ���� ������ ��
		int startpage = ((page - 1) / 10) * 10 + 1;

		// ���� �������� ������ ������ ������ ��(10, 20, 30 ��...)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		mv.setViewName("member/member_list");
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("memberlist", list);
		mv.addObject("limit", limit);
		mv.addObject("search_field", index);
		mv.addObject("search_word", search_word);
		return mv;
	}

	@RequestMapping(value = "/member_delete.net", method = RequestMethod.GET)
	public String member_delete(String id) throws Exception {
		memberservice.delete(id);
		return "redirect:member_list.net";
	}

}
