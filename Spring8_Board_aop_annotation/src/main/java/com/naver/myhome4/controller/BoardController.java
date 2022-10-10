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
//�츮 ������ ������� BoardListAction ���ݾƿ�? �װ� �����Ͻø� �ǿ�. �⺻ ������ ���ϴ°� ���� ����ؿ�...

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	
	
	//�߰�
		//savefolder.properties
		//�Ӽ�=��
		//�� �������� �ۼ��ϸ� �˴ϴ�.
		//savefoldername=D:\\workspace-sts-3.9.8.RELEASE\\Spring4_MVC_BOARD2\\src\\main\\webapp\\resources\\upload
		//���� �������� ���ؼ��� �Ӽ�(property)�� �̿��մϴ�.
	@Value("${savefoldername}")
	private String saveFolder;

	
		
	
	//�۾���		
	@GetMapping(value = "/BoardWrite.bo")
	//@RequestMapping(value="/BoardWrite.bo", method=RequestMethod.GET)	�̰� �������Ǹ鼭 ���� GetMapping�� value������ ���ϴ�.
	public String board_write() {
		return "board/qna_board_write";
	}
	
	

	
	
	// �Խ��� ���� board_write_ok
	
	@PostMapping(value = "/Board_write_ok.bo")  
	// @RequestMapping(value = "/board_write_ok.nhn", method = RequestMethod.POST)�������Ǹ鼭 ���� ���� �Ǿ����ϴ�.
	public String board_write_ok(Board board) {
		boardService.insertBoard(board);//���� �޼��� ȣ��
		return "redirect:/BoardList.bo";
	}
	
	
	
	
	
	
	//�۸�� ����
	
	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(
		      @RequestParam(value="page", defaultValue="1", required=false) 
		      int page, ModelAndView mv) {
		   
		   int limit = 10; //�� ȭ�鿡 ����� ���ڵ� ����
		   
		   int listcount = boardService.getListCount(); //�� ����Ʈ ���� �޾ƿ�
		   
		   //�� ������ ��
		   int maxpage = (listcount + limit - 1) / limit; 
		   
		   //���� �������� ������ ���� ������ �� (1, 11, 21 ��...)
		   int startpage = ((page - 1) / 10 ) * 10 + 1; 
		   
		   //���� �������� ������ ������ ������ �� (10, 20, 30 ��...)
		   int endpage = startpage + 10 - 1; 
		   
		   if(endpage > maxpage)
		      endpage = maxpage; 
		   
		   List<Board> boardlist = boardService.getBoardList(page,limit); //����Ʈ�� �޾ƿ�
		   
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


	
	/* ���� ���ε� */
	
	
	//�ڷ� ���ε� �ϴ°� postmapping�� ����մϴ�.
	//MultipartFile��ü �����ͼ� �����ϴ�. ���� ���ε��� ��쿡�� ���𰡸� ó�����ٲ�����. ���⼭ ����� ���𰡸� ó�����ְ� �� ���ø�
	//�������� setBOARD_FILE �� �־��ٰŰŵ��.

	@PostMapping ("/BoardAddAction.bo")
    //@RequestMapping(value="/BoardAddAction.bo",method=RequestMethod.POST)
    public String bbs_write_ok(Board board, HttpServletRequest request)
          throws Exception{
       MultipartFile uploadfile = board.getUploadfile(); //�ڷ�����  MultipartFile�� �����°̴ϴ�.
       
       
     //���������� �̸� �״�� ����������. �ø� �̸� �״��. ������ �����е��� �����ϴ� �� ��ġ������ �̸��� �ٲܰſ���.
       if(!uploadfile.isEmpty()) {							 //������ �����ߴٸ�
          String fileName = uploadfile.getOriginalFilename();//���� ���ϸ�, �̰��� ����ֽ��ϴ�.
          board.setBOARD_ORIGINAL(fileName); //���� ���ϸ� ����
          
          
        //���ο� ���� �̸� : ���� ��-��-�� , �����Ҷ� �̸��� ��ġ�� ��찡 �ֱ⶧����.. ù��°�� ���ó�¥�� ���� �������� ���մϴ�. 
          // ���ο� ���� �̸� : ���� - �� - �� - �� 
       Calendar c = Calendar.getInstance();
       int year = c.get(Calendar.YEAR); // ���� �⵵ ���մϴ�.
       int month = c.get(Calendar.MONTH) +  1; // ���� �� ���մϴ�.
       int date = c.get(Calendar.DATE); // ���� �� ���մϴ�.
       
       
       //����
       //1. ��Ŭ���� ���� 2. Ư������ - �� �ȿ����� ���� ���������� �����̾����. ������ ���� ���������� ���մϴ�.
       //String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
       
       String homedir = saveFolder + year + "-" + month + "-" + date;
       System.out.println(homedir);
       File path1 = new File(homedir);
       if (!(path1.exists())) {
          path1.mkdir(); //���ο� ������ ����
       }
       
       //������ ���մϴ�.
       Random r = new Random();
       int random = r.nextInt(100000000);
       
       /**** Ȯ���� ���ϱ� ���� ****/
       int index = fileName.lastIndexOf(".");
       // ���ڿ����� Ư�� ���ڿ��� ��ġ �� (index)�� ��ȯ�Ѵ�.
       // indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
       // lastIndextOf�� ������������ �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�.
       // (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� �����մϴ�.)
       System.out.println("index = " + index);
       
       String fileExtension = fileName.substring(index + 1);
       System.out.println("fileExtension = " + fileExtension);
       /**** Ȯ���� ���ϱ� ��****/
       
       
       
       //���ο� ���ϸ�
       String refileName = "bbs" + year + month + date + random + "." + fileExtension;
       System.out.println("refileName = " + refileName);
       
       //����Ŭ ��� ����� ���� ��
       String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
       System.out.println("fileDBName = " + fileDBName);
       
       // tranferTo ���ε��� ������ �Ű����� ��ο������ϴ°�! �ſ� �߿��մϴ�.
       //transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
       uploadfile.transferTo(new File(saveFolder + fileDBName));
       
       
       // bbs.������. �����߻�. fileExtension(Ȯ���� ���Ѱ�)= ������ ���ο� ���ϸ�
       //�ٲ� ���ϸ� ����
       board.setBOARD_FILE(fileDBName);
    }
       
       boardService.insertBoard(board); //���� �޼��� ȣ��
       
       return "redirect:BoardList.bo";
          
    }
	
	

	
	
	//BoardDetailAction.bo?num=9 ��û�� �Ķ���� num�� ���� int num�� �����մϴ�.
	//���������� ������ ó���ϴ°ſ���..
	@GetMapping("/BoardDetailAction.bo")
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {
		Board board = boardService.getDetail(num);
		if(board == null) {
			System.out.println("�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURI()); 
			mv.addObject("message", "�󼼺��� �����Դϴ�."); 
		} else {
			System.out.println("�󼼺��� ����");
			int count = commentService.getListCount(num);
		
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count); 
			mv.addObject("boarddata", board); 
		}
		return mv;
		
	}
	
	
	
	//ajax�ֱ�! 
	 @ResponseBody
	 @RequestMapping(value="/BoardListAjax.bo")
	 public Map<String,Object> boardListAjax(
	       @RequestParam(value="page", defaultValue="1", required=false) int page,
	       @RequestParam(value="limit", defaultValue="10", required=false) int limit
	       )
	 {
	    int listcount = boardService.getListCount(); //�� ����Ʈ ���� �޾ƿ�
	    
	    //�� ������ ��
	    int maxpage = (listcount + limit - 1 ) / limit; 
	    
	    //���� �������� ������ ���� ������ ��(1, 11, 21 �� ...)
	    int startpage = ((page - 1) / 10) * 10 + 1 ; 
	    
	    //���� �������� ������ ������ ������ �� (10, 20, 30 ��...)
	    int endpage = startpage + 10 - 1; 
	    
	    if(endpage > maxpage)
	       endpage = maxpage; 
	    
	    List<Board> boardlist = boardService.getBoardList(page, limit); //����Ʈ�� �޾ƿ�
	    
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
	 
	 
	
	
	//������ư �������� ���� ������ �����մϴ�. 
	@GetMapping("/BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) {
		Board boarddata = boardService.getDetail(num);
		//�� ���� �ҷ����� ������ ����Դϴ�.
		if(boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url",request.getRequestURI());
			mv.addObject("message", "(����)�󼼺��� �����Դϴ�.");
			return mv;
			
		}
		System.out.println("(����)�󼼺��� ����");
		
		//���� �� �������� �̵��� �� ���� �۳����� �����ֱ⶧���� boarddata ��ü��
		//ModelAndView ��ü�� �����մϴ�.
		mv.addObject("boarddata",boarddata);
		
		//�� ���� ���������� �̵��ϱ����� ��θ� �����մϴ�.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}
	
	
	
	
	//���� �׼�
	
	 @PostMapping("BoardModifyAction.bo")
     public ModelAndView BoardModifyAction(Board board, String before_file,
           String check, ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception{
		 
        boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());
        
        //��й�ȣ�� �ٸ� ���
        if(usercheck == false) {
           response.setContentType("text/html;charset=utf-8");
           PrintWriter out = response.getWriter()   ;
           out.println("<script>");
           out.println("alert('��й�ȣ�� �ٸ��ϴ�.')");
           out.println("history.back();");
           out.println("</script>");
           out.close();
           return null;
        }
        
        MultipartFile uploadfile = board.getUploadfile();
       // String saveFolder = request.getSession().getServletContext().getRealPath("resources")+ "/upload/";
        
        System.out.println("check = " + check);
        if(check != null && !check.contentEquals("")) { //���� ���� �״�� 
          board.setBOARD_ORIGINAL(check);
          //<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_fILE}">
          //�� ���� ������ board.setBOARD_FILE()�� �ڵ� ���� �˴ϴ�.
          
        } else {
        	
           if(uploadfile !=null && !uploadfile.isEmpty()) {//���� ������ ���
             System.out.println("���� ���� �Ǿ����ϴ�.");
             
              String fileName=uploadfile.getOriginalFilename(); //���� ���� ��
              board.setBOARD_ORIGINAL(fileName);
              
              String fileDBName = fileDBName(fileName, saveFolder);
              
              //transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
              uploadfile.transferTo(new File(saveFolder + fileDBName));
              
              //�ٲ� ���ϸ����� ����
              board.setBOARD_FILE(fileDBName); 
              
              
           } else { //uploadfile.isEmpty() �� ��� - ���� �������� ���� ���
        	   
              System.out.println("���� ���� �����ϴ�.");
              
              
              // <input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
              // �� �±׿� ���� �ִٸ� ""�� ���� �����մϴ�.
              
              board.setBOARD_FILE(""); //""�� �ʱ�ȭ �մϴ�.
              board.setBOARD_ORIGINAL(""); //""�� �ʱ�ȭ �մϴ�.
           }
        }
        
        
        
        //DAO���� ���� �޼��� ȣ���Ͽ� �����մϴ�.
        int result = boardService.boardModify(board);
        
        //������ ������ ���
        if(result == 0) {
           System.out.println("�Խ��� ���� ����");
           mv.setViewName("error/error");
           mv.addObject("url",request.getRequestURI());
           mv.addObject("message", "�Խ��� ���� ����");
        } else { //���� ������ ���
           System.out.println("�Խ��� ���� �Ϸ�");
           
           //�߰��Ⱥκ�
           //�������� ������ �ְ� ���ο� ������ ������ ���� ������ ����� ���̺� �߰��մϴ�.
           if(!before_file.equals("") && !before_file.equals(board.getBOARD_FILE())) {
        	   boardService.insert_deleteFile(before_file);
           }
           
           
           
           
           String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();
           
           //������ �� ������ �����ֱ� ���� �� ���� ���� ���� �������� �̵��ϱ� ���� ��θ� �����մϴ�
           mv.setViewName(url);
        }
        return mv;
        
     }

  
	 //db�� �� �κ� ����
     private String fileDBName(String fileName, String saveFolder) {
           Calendar c = Calendar.getInstance();
           int year = c.get(Calendar.YEAR);
           int month = c.get(Calendar.MONTH);
           int date = c.get(Calendar.DATE);
           
           String homedir = saveFolder + year + "-" + month + "-" + date;
           System.out.println(homedir);
           File path1 = new File(homedir);
           if(!(path1.exists())) {
              path1.mkdir(); //���ο� ������ ����
           }
           
           //������ ���մϴ�.
           Random r = new Random();
           int random = r.nextInt(100000000);
           
           //Ȯ���� ���ϱ� ����
           int index = fileName.lastIndexOf(".");
           //���ڿ����� Ư�� ���ڿ��� ��ġ ��(index)�� ��ȯ�Ѵ�.
           //indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
           //lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�.
           //(���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� �����մϴ�.)
           System.out.println("index = " + index);
           
           String fileExtension = fileName.substring(index + 1);
           System.out.println("fileExtension = " + fileExtension);
           //Ȯ���� ���ϱ� ��
           
           //���ο� ���ϸ�
           String refileName = "bbs" + year + month + date + random + "." + fileExtension;
           System.out.println("refileName = " + refileName);
           
           //����Ŭ ��� ����� ���� ��
           String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
           System.out.println("fileDBName = " + fileDBName);
           return fileDBName;
        }

	
	
 	//�亯�ޱ�
     @GetMapping("BoardReplyView.bo")
     public ModelAndView BoardReplyView(int num, ModelAndView mv, HttpServletRequest request) {
        Board board = boardService.getDetail(num);
        if (board == null) {
           mv.setViewName("error/error");
           mv.addObject("url", request.getRequestURL());
           mv.addObject("message", "�Խ��� �亯�� �������� ����");
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
           mv.addObject("message", "�Խ��� �亯 ó�� ����");
        }else {
           mv.setViewName("redirect:BoardList.bo");
        
        }
        return mv;
     }
     
     @GetMapping("BoardFileDown.bo")
     public void BoardFileDown(String filename, HttpServletRequest request, String original,
           HttpServletResponse response) throws Exception {

        String savePath = "resources/upload";

        // ������ ���� ȯ�� ������ ��� �ִ� ��ü�� �����մϴ�.
        ServletContext context = request.getSession().getServletContext();
        String sDownloadPath = context.getRealPath(savePath);

        // String sFilePath = sDownloadPath + "\\" + fileName;
        // "\" �߰��ϱ� ���� "\\" ����մϴ�.
        String sFilePath = sDownloadPath + "/" + filename;
        System.out.println(sFilePath);

        byte b[] = new byte[4096];

        // sFilePath�� �ִ� ���� MimeType�� ���ؿɴϴ�
        String sMimeType = context.getMimeType(sFilePath);
        System.out.println("sMimeType>>>" + sMimeType);

        if (sMimeType == null)
           sMimeType = "application/octet-stream";

        response.setContentType(sMimeType);

        // - �� �κ��� �ѱ� ���ϸ��� ������ ���� ������ �ݴϴ�.
        String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
        System.out.println(sEncoding);

        /*
         * Content-Dispostion: attachment : �������� �ش� Content�� ó�������ʰ�, �ٿ�ε� �ϰ� �˴ϴ�.
         * 
         */
        response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
        //������Ʈ properties - Project facets���� �ڹٹ��� 1.8�� ����

        try (
              // �� ���������� ��� ��Ʈ�� �����մϴ�.
              BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
              // sFilePath�� ������ ���Ͽ� ���� �Է� ��Ʈ���� �����մϴ�.
              BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) {
           int numRead;
           // read(b, 0, b.length) : ����Ʈ �迭 b�� 0������ b.length
           // ũ�⸸ŭ �о�ɴϴ�.
           while ((numRead = in.read(b, 0, b.length)) != -1) { // ���� �����Ͱ� �����ϴ� ���
              // ����Ʈ �迭 b�� 0�� ���� numReadũ�� ��ŭ �������� ���
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
        //�� ���� ����� ��û�� ����ڰ� ���� �ۼ��� ��������� �Ǵ��ϱ� ����
        //�Է��� ��й�ȣ�� ����� ��й�ȣ�� ���Ͽ� ��ġ�ϸ� �����մϴ�
        boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);
        
        //��й�ȣ ��ġ���� �ʴ� ���
        if(usercheck == false) {
           response.setContentType("text/html;charset=utf-8"); 
           PrintWriter out = response.getWriter(); 
           out.println("<script>"); 
           out.println("alert('��й�ȣ�� �ٸ��ϴ�.');");
           out.println("history.back();"); 
           out.println("</script>"); 
           out.close();
           return null; 
        }
        
         //��й�ȣ�� ��ġ�ϴ� ��� ���� ó���մϴ�
        int result = boardService.boardDelete(num);
        
        //���� ó�� ������ ���
         if(result == 0) {
            System.out.println("�Խ��� ���� ����");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL()); 
            mv.addObject("message", "���� ����"); 
            return mv; 
         }
        
     //���� ó�� ������ ���- �� ��� ���� ��û�� �����ϴ� �κ��Դϴ�
        System.out.println("�Խ��� ���� ����");
        response.setContentType("text/html;charset=utf-8"); 
        PrintWriter out = response.getWriter(); 
        out.println("<script>"); 
        out.println("alert('���� �Ǿ����ϴ�.');"); 
        out.println("location.href='BoardList.bo';"); 
        out.println("</script>"); 
        out.close();
        return null; 
        
     }
}
     