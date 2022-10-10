package com.naver.myhome4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome4.dao.CommentDAO;
import com.naver.myhome4.domain.Comment;

@Service //Ŭ���� ����� �׻� ������ ����� ���ؼ� �ۼ����ݴϴ�
public class CommentServiceImpl implements CommentService	 {
	
	@Autowired
	private CommentDAO dao;
	
	@Override
	public int getListCount(int board_num) {
		return dao.getListCount(board_num); //CommentDAO���� �� �����Ϸ� ���ϴ� 
	}

	@Override
	public List<Comment> getCommentList(int board_num, int page) {
		//���� ������ ���Ƽ� map�� ��°ſ��� �����մϴ�~
		int startrow=(page-1)*3+1;
		int endrow=startrow+3-1;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("board_num", board_num); //�ش��ϴ� ���� �޾Ƽ� �̺κп��ֽ��ϴ�.
		map.put("page",page); //����¡ ó���ϴ� ���Ŀ� �ֽ��ϴ�. 
		map.put("start",startrow);
		map.put("end",endrow);
		return dao.getCommentList(map);
		//���񽺰��� �޸��ص帰�� ���� ���⿡ ��� �־������ �ѹ� �������ּ���~
	}
	
	
	@Override //����� CommentDAO���� �� �������ݴϴ�~
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
