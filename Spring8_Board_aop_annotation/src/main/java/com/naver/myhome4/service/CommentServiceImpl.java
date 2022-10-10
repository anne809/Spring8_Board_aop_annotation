package com.naver.myhome4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome4.dao.CommentDAO;
import com.naver.myhome4.domain.Comment;

@Service //클래스 만들면 항상 빈으로 만들기 위해서 작성해줍니다
public class CommentServiceImpl implements CommentService	 {
	
	@Autowired
	private CommentDAO dao;
	
	@Override
	public int getListCount(int board_num) {
		return dao.getListCount(board_num); //CommentDAO에서 또 생성하러 갑니다 
	}

	@Override
	public List<Comment> getCommentList(int board_num, int page) {
		//담을 종류가 많아서 map에 담는거에요 유용합니다~
		int startrow=(page-1)*3+1;
		int endrow=startrow+3-1;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("board_num", board_num); //해당하는 것을 받아서 이부분에넣습니다.
		map.put("page",page); //페이징 처리하는 공식에 넣습니다. 
		map.put("start",startrow);
		map.put("end",endrow);
		return dao.getCommentList(map);
		//서비스계층 메모해드린걸 토대로 여기에 어떤걸 넣어야할지 한번 생각해주세요~
	}
	
	
	@Override //만들고 CommentDAO가서 또 생성해줍니다~
	   public int commentsInsert(Comment c) {
	      return dao.commentsInsert(c);
	   }

	   @Override
	   public int commentsDelete(int num) {
	      return dao.commentDelete(num);
	   }

	   @Override
	   public int commentsUpdate(Comment co) {
	      return dao.commentsUpdate(co);
	   }


}
