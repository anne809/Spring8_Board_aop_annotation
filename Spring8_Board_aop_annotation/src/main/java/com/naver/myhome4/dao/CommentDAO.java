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

	
	//select����Ʈ�� �ΰ��� ���ڸ� �޾ƿ�. ������(Comments)�� �Ķ���Ͱ�(map-> 4���� ������ �Ѱ��ٶ����°� ���� ���� map) key�� value��
	public List<Comment> getCommentList(Map<String, Integer> map) {
		return sqlSession.selectList("Comments.getList", map);// -> sqlSession ���� �̾Ƴ� �����Դϴ�.
	}

	public int commentsInsert(Comment c) {
		return sqlSession.insert("Comments.insert", c);
		
	}
	
	public int commentDelete(int num) {
		return sqlSession.delete("Comments.delete", num);
	}

	public int getListCount(int board_num) {  //�ι��� ���ڰ��� �Ķ�����Դϴ�. xml���� parameterType�� �˴ϴ�.
		return sqlSession.selectOne("Comments.count",board_num);
	} //comments���� �ۼ��� �ʿ��Ѱɱ�..?
	

	public int commentsUpdate(Comment co) {
		return sqlSession.update("Comments.update", co);
	}
	

	
	
}
