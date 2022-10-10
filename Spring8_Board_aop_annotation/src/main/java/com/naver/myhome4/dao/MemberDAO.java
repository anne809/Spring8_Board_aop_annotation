package com.naver.myhome4.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome4.domain.Member;

//MemberServiceImpl ���� �ۼ��Ϸ� �̵�������



@Repository
public class MemberDAO {
	
	
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public int insert(Member m) {
		return sqlSession.insert("Members.insert", m);
		
	}
	
	

	/*
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1; // DB�� �ش� id�� �����ϴ�.

		try {
			con = ds.getConnection();
			System.out.println("getConnection:isId()");

			String sql = "select id from member where id = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = 0; // DB�� �ش� id�� �ֽ��ϴ�
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		} // finally

		===> �̷� ������ �����Ǿ����ϴ�.
		
		
		*/
	public Member isId(String id) { //������ try/catch �ߴ�����..
		return sqlSession.selectOne("Members.idcheck",id); // �̺κ��� mamber.xml ���� mapper Members
	}



	public Member member_info(String id) {
		return sqlSession.selectOne("Members.idcheck",id);
	}


	public int update(Member m) {
		return sqlSession.update("Members.update",m);
	}
	



	public int delete(String id) {
		  return sqlSession.delete("Members.delete",id);
	}



	public List<Member> getSearchList(Map<String, Object> map) {
		return sqlSession.selectList("Members.getSearchList", map);
	}



	
	
	public int getSearchListCount(Map<String, String> map) {
		return sqlSession.selectOne("Members.searchcount", map);
	}
	
	
	
	
	 public List<Member> getList(Map<String, Integer> map){
	      return sqlSession.selectList("Members.list", map);
	   }


	

}
