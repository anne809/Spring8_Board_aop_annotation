package com.naver.myhome4.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myhome4.domain.Board;
import com.naver.myhome4.service.BoardService;
import com.naver.myhome4.service.CommentService;

/*


*/
//우리 예전에 만들었던 BoardListAction 있잖아요? 그거 복붙하시면 되요. 기본 로직은 구하는건 거의 비슷해요...

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	
	
	//추가
		//savefolder.properties
		//속성=값
		//의 형식으로 작성하면 됩니다.
		//savefoldername=D:\\workspace-sts-3.9.8.RELEASE\\Spring4_MVC_BOARD2\\src\\main\\webapp\\resources\\upload
		//값을 가져오기 위해서는 속성(property)를 이용합니다.
	@Value("${savefoldername}")
	private String saveFolder;

	
		
	
	//글쓰기		
	@GetMapping(value = "/BoardWrite.bo")
	//@RequestMapping(value="/BoardWrite.bo", method=RequestMethod.GET)	이게 버전업되면서 위의 GetMapping과 value값으로 씁니다.
	public String board_write() {
		return "board/qna_board_write";
	}
	
	

	
	
	// 게시판 저장 board_write_ok
	
	@PostMapping(value = "/Board_write_ok.bo")  
	// @RequestMapping(value = "/board_write_ok.nhn", method = RequestMethod.POST)버전업되면서 같이 쓰게 되었습니다.
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);//저장 메서드 호출
		return "redirect:/BoardList.bo";
	}
	
	
	
	
	
	
	//글목록 보기
	
	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(
		      @RequestParam(value="page", defaultValue="1", required=false) 
		      int page, ModelAndView mv) {
		   
		   int limit = 10; //한 화면에 출력할 레코드 갯수
		   
		   int listcount = boardService.getListCount(); //총 리스트 수를 받아옴
		   
		   //총 페이지 수
		   int maxpage = (listcount + limit - 1) / limit; 
		   
		   //현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21 등...)
		   int startpage = ((page - 1) / 10 ) * 10 + 1; 
		   
		   //현재 페이지에 보여줄 마지막 페이지 수 (10, 20, 30 등...)
		   int endpage = startpage + 10 - 1; 
		   
		   if(endpage > maxpage)
		      endpage = maxpage; 
		   
		   List<Board> boardlist = boardService.getBoardList(page,limit); //리스트를 받아옴
		   
		   mv.setViewName("board/qna_board_list");
		   mv.addObject("page", page); 
		   mv.addObject("maxpage", maxpage); 
		   mv.addObject("startpage", startpage); 
		   mv.addObject("endpage", endpage); 
		   mv.addObject("listcount", listcount); 
		   mv.addObject("boardlist", boardlist); 
		   mv.addObject("limit", limit);  
		   
		   return mv; 
		}


	
	/* 파일 업로드 */
	
	
	//자료 업로드 하는건 postmapping을 써야합니다.
	//MultipartFile객체 가져와서 꺼냅니다. 파일 업로드한 경우에는 무언가를 처리해줄꺼에요. 여기서 결론은 무언가를 처리해주고 쭉 보시면
	//마지막에 setBOARD_FILE 에 넣어줄거거든요.

	@PostMapping ("/BoardAddAction.bo")
    //@RequestMapping(value="/BoardAddAction.bo",method=RequestMethod.POST)
    public String bbs_write_ok(Board board, HttpServletRequest request)
          throws Exception{
       MultipartFile uploadfile = board.getUploadfile(); //자료형이  MultipartFile로 가져온겁니다.
       
       
     //오리지널은 이름 그대로 넣을꺼에요. 올린 이름 그대로. 실제로 여러분들이 저장하는 곳 위치에서는 이름을 바꿀거에요.
       if(!uploadfile.isEmpty()) {							 //파일을 선택했다면
          String fileName = uploadfile.getOriginalFilename();//원래 파일명, 이곳에 집어넣습니다.
          board.setBOARD_ORIGINAL(fileName); //원래 파일명 저장
          
          
        //새로운 폴더 이름 : 오늘 년-월-일 , 저장할땐 이름이 겹치는 경우가 있기때문에.. 첫번째는 오늘날짜에 대한 연월일을 구합니다. 
          // 새로운 폴더 이름 : 오늘 - 년 - 월 - 일 
       Calendar c = Calendar.getInstance();
       int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
       int month = c.get(Calendar.MONTH) +  1; // 오늘 월 구합니다.
       int date = c.get(Calendar.DATE); // 오늘 일 구합니다.
       
       
       //제거
       //1. 이클립스 관리 2. 특정폴더 - 이 안에서만 사용된 지역변수라서 갈색이었어요. 하지만 이젠 전역변수로 변합니다.
       //String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
       
       String homedir = saveFolder + year + "-" + month + "-" + date;
       System.out.println(homedir);
       File path1 = new File(homedir);
       if (!(path1.exists())) {
          path1.mkdir(); //새로운 폴더를 생성
       }
       
       //난수를 구합니다.
       Random r = new Random();
       int random = r.nextInt(100000000);
       
       /**** 확장자 구하기 시작 ****/
       int index = fileName.lastIndexOf(".");
       // 문자열에서 특정 문자열의 위치 값 (index)를 반환한다.
       // indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
       // lastIndextOf는 마지막ㄷ으로 발견되는 문자열의 index를 반환합니다.
       // (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
       System.out.println("index = " + index);
       
       String fileExtension = fileName.substring(index + 1);
       System.out.println("fileExtension = " + fileExtension);
       /**** 확장자 구하기 끝****/
       
       
       
       //새로운 파일명
       String refileName = "bbs" + year + month + date + random + "." + fileExtension;
       System.out.println("refileName = " + refileName);
       
       //오라클 디비에 저장될 파일 명
       String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
       System.out.println("fileDBName = " + fileDBName);
       
       // tranferTo 업로드한 파일을 매개변수 경로에저장하는것! 매우 중요합니다.
       //transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
       uploadfile.transferTo(new File(saveFolder + fileDBName));
       
       
       // bbs.연월일. 난수발생. fileExtension(확장자 구한것)= 생성된 새로운 파일명
       //바뀐 파일명 저장
       board.setBOARD_FILE(fileDBName);
    }
       
       boardService.insertBoard(board); //저장 메서드 호출
       
       return "redirect:BoardList.bo";
          
    }
	
	

	
	
	//BoardDetailAction.bo?num=9 요청시 파라미터 num의 값을 int num에 저장합니다.
	//에러페이지 나눠서 처리하는거에용..
	@GetMapping("/BoardDetailAction.bo")
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board == null) {
			System.out.println("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURI()); 
			mv.addObject("message", "상세보기 실패입니다."); 
		} else {
			System.out.println("상세보기 성공");
			int count = commentService.getListCount(num);
		
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count); 
			mv.addObject("boarddata", board); 
		}
		return mv;
		
	}
	
	
	
	//ajax주기! 
	 @ResponseBody
	 @RequestMapping(value="/BoardListAjax.bo")
	 public Map<String,Object> boardListAjax(
	       @RequestParam(value="page", defaultValue="1", required=false) int page,
	       @RequestParam(value="limit", defaultValue="10", required=false) int limit
	       )
	 {
	    int listcount = boardService.getListCount(); //총 리스트 수를 받아옴
	    
	    //총 페이지 수
	    int maxpage = (listcount + limit - 1 ) / limit; 
	    
	    //현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등 ...)
	    int startpage = ((page - 1) / 10) * 10 + 1 ; 
	    
	    //현재 페이지에 보여줄 마지막 페이지 수 (10, 20, 30 등...)
	    int endpage = startpage + 10 - 1; 
	    
	    if(endpage > maxpage)
	       endpage = maxpage; 
	    
	    List<Board> boardlist = boardService.getBoardList(page, limit); //리스트를 받아옴
	    
	    Map<String, Object> map = new HashMap<String, Object>(); 
	    map.put("page",page); 
	    map.put("maxpage",maxpage); 
	    map.put("startpage",startpage);
	    map.put("endpage",endpage);
	    map.put("listcount",listcount);
	    map.put("boardlist",boardlist);
	    map.put("limit",limit);
	    return map; 
	 }
	 
	 
	
	
	//수정버튼 눌렀을때 정보 가지고 가야합니다. 
	@GetMapping("/BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board boarddata = boardService.getDetail(num);
		//글 내용 불러오기 실패한 경우입니다.
		if(boarddata == null) {
			System.out.println("(수정)상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURI());
			mv.addObject("message", "(수정)상세보기 실패입니다.");
			return mv;
			
		}
		System.out.println("(수정)상세보기 성공");
		
		//수정 폼 페이지로 이동할 떄 원문 글내용을 보여주기때문에 boarddata 객체를
		//ModelAndView 객체에 저장합니다.
		mv.addObject("boarddata",boarddata);
		
		//글 수정 폼페이지로 이동하기위해 경로를 설정합니다.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}
	
	
	
	
	//수정 액션
	
	 @PostMapping("BoardModifyAction.bo")
     public ModelAndView BoardModifyAction(Board board, String before_file,
           String check, ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception{
		 
        boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
        
        //비밀번호가 다른 경우
        if(usercheck == false) {
           response.setContentType("text/html;charset=utf-8");
           PrintWriter out = response.getWriter()   ;
           out.println("<script>");
           out.println("alert('비밀번호가 다릅니다.')");
           out.println("history.back();");
           out.println("</script>");
           out.close();
           return null;
        }
        
        MultipartFile uploadfile = board.getUploadfile();
       // String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
        
        System.out.println("check = " + check);
        if(check != null && !check.contentEquals("")) { //기존 파일 그대로 
          board.setBOARD_ORIGINAL(check);
          //<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_fILE}">
          //위 문장 때문에 board.setBOARD_FILE()은 자동 저장 됩니다.
          
        } else {
        	
           if(uploadfile !=null && !uploadfile.isEmpty()) {//파일 변경한 경우
             System.out.println("파일 변경 되었습니다.");
             
              String fileName=uploadfile.getOriginalFilename(); //원래 파일 명
              board.setBOARD_ORIGINAL(fileName);
              
              String fileDBName = fileDBName(fileName, saveFolder);
              
              //transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
              uploadfile.transferTo(new File(saveFolder + fileDBName));
              
              //바뀐 파일명으로 저장
              board.setBOARD_FILE(fileDBName); 
              
              
           } else { //uploadfile.isEmpty() 인 경우 - 파일 선택하지 않은 경우
        	   
              System.out.println("선택 파일 없습니다.");
              
              
              // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
              // 위 태그에 값이 있다면 ""로 값을 변경합니다.
              
              board.setBOARD_FILE(""); //""로 초기화 합니다.
              board.setBOARD_ORIGINAL(""); //""로 초기화 합니다.
           }
        }
        
        
        
        //DAO에서 수정 메서드 호출하여 수정합니다.
        int result = boardService.boardModify(board);
        
        //수정에 실패한 경우
        if(result == 0) {
           System.out.println("게시판 수정 실패");
           mv.setViewName("error/error");
           mv.addObject("url",request.getRequestURI());
           mv.addObject("message", "게시판 수정 실패");
        } else { //수정 성공의 경우
           System.out.println("게시판 수정 완료");
           
           //추가된부분
           //수정전에 파일이 있고 새로운 파일을 선택한 경우는 삭제할 목록을 테이블에 추가합니다.
           if(!before_file.equals("") && !before_file.equals(board.getBOARD_FILE())) {
        	   boardService.insert_deleteFile(before_file);
           }
           
           
           
           
           String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
           
           //수정한 글 내용을 보여주기 위해 글 내용 보기 보기 페이지로 이동하기 위해 경로를 설저합니다
           mv.setViewName(url);
        }
        return mv;
        
     }

  
	 //db에 들어갈 부분 가공
     private String fileDBName(String fileName, String saveFolder) {
           Calendar c = Calendar.getInstance();
           int year = c.get(Calendar.YEAR);
           int month = c.get(Calendar.MONTH);
           int date = c.get(Calendar.DATE);
           
           String homedir = saveFolder + year + "-" + month + "-" + date;
           System.out.println(homedir);
           File path1 = new File(homedir);
           if(!(path1.exists())) {
              path1.mkdir(); //새로운 폴더를 생성
           }
           
           //난수를 구합니다.
           Random r = new Random();
           int random = r.nextInt(100000000);
           
           //확장자 구하기 시작
           int index = fileName.lastIndexOf(".");
           //문자열에서 특정 문자열의 위치 값(index)를 반환한다.
           //indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
           //lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
           //(파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
           System.out.println("index = " + index);
           
           String fileExtension = fileName.substring(index + 1);
           System.out.println("fileExtension = " + fileExtension);
           //확장자 구하기 끝
           
           //새로운 파일명
           String refileName = "bbs" + year + month + date + random + "." + fileExtension;
           System.out.println("refileName = " + refileName);
           
           //오라클 디비에 저장될 파일 명
           String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
           System.out.println("fileDBName = " + fileDBName);
           return fileDBName;
        }

	
	
 	//답변달기
     @GetMapping("BoardReplyView.bo")
     public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
        Board board = boardService.getDetail(num);
        if (board == null) {
           mv.setViewName("error/error");
           mv.addObject("url", request.getRequestURL());
           mv.addObject("message", "게시판 답변글 가져오기 실패");
        }else {
           mv.addObject("boarddata", board);
           mv.setViewName("board/qna_board_reply");
        }
        return mv;
     }
     
     @PostMapping("BoardReplyAction.bo")
     public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) {
        int result = boardService.boardReply(board);
        if(result == 0) {
           mv.setViewName("error/error");
           mv.addObject("url", request.getRequestURL());
           mv.addObject("message", "게시판 답변 처리 실패");
        }else {
           mv.setViewName("redirect:BoardList.bo");
        
        }
        return mv;
     }
     
     @GetMapping("BoardFileDown.bo")
     public void BoardFileDown(String filename, HttpServletRequest request, String original,
           HttpServletResponse response) throws Exception {

        String savePath = "resources/upload";

        // 서블릿의 실행 환경 정보를 담고 있는 객체를 리턴합니다.
        ServletContext context = request.getSession().getServletContext();
        String sDownloadPath = context.getRealPath(savePath);

        // String sFilePath = sDownloadPath + "\\" + fileName;
        // "\" 추가하기 위해 "\\" 사용합니다.
        String sFilePath = sDownloadPath + "/" + filename;
        System.out.println(sFilePath);

        byte b[] = new byte[4096];

        // sFilePath에 있는 파일 MimeType을 구해옵니다
        String sMimeType = context.getMimeType(sFilePath);
        System.out.println("sMimeType>>>" + sMimeType);

        if (sMimeType == null)
           sMimeType = "application/octet-stream";

        response.setContentType(sMimeType);

        // - 이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.
        String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
        System.out.println(sEncoding);

        /*
         * Content-Dispostion: attachment : 브라우저는 해당 Content를 처리하지않고, 다운로드 하게 됩니다.
         * 
         */
        response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
        //프로젝트 properties - Project facets에서 자바버전 1.8로 수정

        try (
              // 웹 브라우저로의 출력 스트림 생성합니다.
              BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
              // sFilePath로 지정한 파일에 대한 입력 스트림을 생성합니다.
              BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) {
           int numRead;
           // read(b, 0, b.length) : 바이트 배열 b의 0번부터 b.length
           // 크기만큼 읽어옵니다.
           while ((numRead = in.read(b, 0, b.length)) != -1) { // 읽을 데이터가 존재하는 경우
              // 바이트 배열 b의 0번 부터 numRead크기 만큼 브라우저로 출력
              out2.write(b, 0, numRead);
           }
        } catch (Exception e) {
           e.printStackTrace();
        }
     }
     
     
     
     @PostMapping("BoardDeleteAction.bo")
     public ModelAndView BoardDeleteAction(String BOARD_PASS, int num,
           ModelAndView mv,
            HttpServletResponse response, HttpServletRequest request
           )throws Exception{
        //글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
        //입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다
        boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
        
        //비밀번호 일치하지 않는 경우
        if(usercheck == false) {
           response.setContentType("text/html;charset=utf-8"); 
           PrintWriter out = response.getWriter(); 
           out.println("<script>"); 
           out.println("alert('비밀번호가 다릅니다.');");
           out.println("history.back();"); 
           out.println("</script>"); 
           out.close();
           return null; 
        }
        
         //비밀번호가 일치하는 경우 삭제 처리합니다
        int result = boardService.boardDelete(num);
        
        //삭제 처리 실패한 경우
         if(result == 0) {
            System.out.println("게시판 삭제 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL()); 
            mv.addObject("message", "삭제 실패"); 
            return mv; 
         }
        
     //삭제 처리 성공한 경우- 글 목록 보기 요청을 전송하는 부분입니다
        System.out.println("게시판 삭제 성공");
        response.setContentType("text/html;charset=utf-8"); 
        PrintWriter out = response.getWriter(); 
        out.println("<script>"); 
        out.println("alert('삭제 되었습니다.');"); 
        out.println("location.href='BoardList.bo';"); 
        out.println("</script>"); 
        out.close();
        return null; 
        
     }
}
     