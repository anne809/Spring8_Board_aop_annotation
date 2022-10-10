package com.naver.myhome4.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome4.domain.Board;

@Repository
public class BoardDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	//root-context���� ���ƿ����ϴ�...
	
	
	public int getListCount() {
		return sqlSession.selectOne("Boards.count");
	}
	
	
	public List<Board> getBoardList(HashMap<String, Integer> map) {
		return sqlSession.selectList("Boards.list", map);
		
	}
	public void insertBoard(Board board) {
		 sqlSession.insert("Boards.insert", board);
	}
	
	public int setReadCountUpdate(int num) {
		return sqlSession.update("Boards.ReadCountUpdate", num);//board.xml�� ���� ������ݴϴ�..
	}


	public Board isBoardWriter(Map<String, Object> map) {
		return sqlSession.selectOne("Boards.BoardWriter", map);//board.xml�� ���� ������ݴϴ�..
	}

	public int boardModify(Board modifyboard) {
		return sqlSession.update("Boards.modify", modifyboard);
	}

	public int boardDelete(Board board) {
		return sqlSession.delete("Boards.delete",board);
	}

	public int boardReply(Board board) {
		return sqlSession.insert("Boards.reply_insert", board);
	}

	public int boardReplyUpdate(Board board) {// ���� ���ϵ� �߿� ���̴ٰ� �ϳ��� ��� �������� namespace ���� �����մϴ�.
		return sqlSession.update("reply_update", board);
	}
	
	public Board getDetail(int num) {
		return sqlSession.selectOne("Boards.Detail", num);//board.xml�� ���� ������ݴϴ�..
	}


	public int insert_deleteFile(String before_file) {
			return sqlSession.insert("Boards.insert_deleteFile", before_file);
		
	}
	

	public int insert_deleteFiles(Board board) {
		return sqlSession.insert("Boards.insert_deleteFiles", board);
		
	}

	public List<String> getDeleteFileList(){
		return sqlSession.selectList("Boards.deleteFileList");
		
	}
}
	

	

	