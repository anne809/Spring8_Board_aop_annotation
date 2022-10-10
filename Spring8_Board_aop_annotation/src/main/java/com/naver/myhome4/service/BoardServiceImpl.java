package com.naver.myhome4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome4.dao.BoardDAO;
import com.naver.myhome4.domain.Board;

//@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO dao;
	
	@Override
	public int getListCount() {
		return dao.getListCount();
		
	}
	
	@Override
	public List<Board> getBoardList(int page, int limit){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int startrow=(page-1)*limit+1;
		int endrow=startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		//double i=1/0;
		return dao.getBoardList(map);
	}

	@Override
	public Board getDetail(int num) {
		if(setReadCountUpdate(num)!=1)
			return null;
		//double i=1/0; //rollback -> Transaction 처리용 오류내기!
		return dao.getDetail(num); //BoardDAO 로.. 가서만들어줍니다.
		
	}

	
	// 댓글 업데이트 -> BoardDAO로 이동해서 작성
	@Override
	public int boardReplyUpdate(Board board) {
		return dao.boardReplyUpdate(board);
	}
	
	// 댓글 -> BoardDAO로 이동해서 작성
	@Override
	public int boardReply(Board board) {
		boardReplyUpdate(board);
		board.setBOARD_RE_LEV(board.getBOARD_RE_LEV()+1);
		board.setBOARD_RE_SEQ(board.getBOARD_RE_SEQ()+1);
		return dao.boardReply(board);
	}

	@Override
	public int boardModify(Board modifyboard) {
		return dao.boardModify(modifyboard);
	}

	/*
	 * @Override 
	 * public int boardDelete(int num) { 
	 * 		 int result = 0; 
	 * 		 Board board=dao.getDetail(num); 
	 * 		 
	 * 		 if (board != null) {
	 *  			result=dao.boardDelete(board);
	 */

	@Override
	public int setReadCountUpdate(int num) {
		return dao.setReadCountUpdate(num);
	}

	@Override
	public boolean isBoardWriter(int num, String pass) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num",num);
		map.put("pass",pass);
		Board result = dao.isBoardWriter(map);
		if(result == null)
			return false;
		else 
			return true;
	}

	@Override
	public void insertBoard(Board board) { //BoardDAO로 이동
		dao.insertBoard(board);
		
	}

	@Override
	public int boardDelete(int num) {
		int result = 0;
		Board board= dao.getDetail(num);
		if(board != null) {
			
			//추가 - 삭제하 파일 목록들을 저장하기 위한 메서드 호출
			dao.insert_deleteFiles(board);
			
			result=dao.boardDelete(board);
		}
		return result;
	}

	@Override
	public int insert_deleteFile(String before_file) {
		return dao.insert_deleteFile(before_file);		
		
	}

	@Override
	public List<String> getDeleteFileList() {
		return dao.getDeleteFileList();	
	}

}
