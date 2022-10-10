package com.naver.myhome4.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome4.domain.Comment;

/*
		*** 

*/
@Repository
public class CommentDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	
	//select리스트는 두개의 인자만 받아요. 쿼리문(Comments)과 파라미터값(map-> 4가지 정보를 넘겨줄때쓰는건 가장 흔한 map) key와 value값
	public List<Comment> getCommentList(Map<String, Integer> map) {
		return sqlSession.selectList("Comments.getList", map);// -> sqlSession 에서 뽑아낼 문장입니다.
	}

	public int commentsInsert(Comment c) {
		return sqlSession.insert("Comments.insert", c);
		
	}
	
	public int commentDelete(int num) {
		return sqlSession.delete("Comments.delete", num);
	}

	public int getListCount(int board_num) {  //두번쨰 인자값은 파라미터입니다. xml에서 parameterType이 됩니다.
		return sqlSession.selectOne("Comments.count",board_num);
	} //comments가서 작성이 필요한걸까..?
	

	public int commentsUpdate(Comment co) {
		return sqlSession.update("Comments.update", co);
	}
	

	
	
}
