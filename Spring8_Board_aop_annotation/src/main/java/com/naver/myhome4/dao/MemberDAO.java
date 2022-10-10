package com.naver.myhome4.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome4.domain.Member;

//MemberServiceImpl 따라서 작성하러 이동했음다



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
		int result = -1; // DB에 해당 id가 없습니다.

		try {
			con = ds.getConnection();
			System.out.println("getConnection:isId()");

			String sql = "select id from member where id = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = 0; // DB에 해당 id가 있습니다
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

		===> 이런 과정이 생략되었습니다.
		
		
		*/
	public Member isId(String id) { //예전엔 try/catch 했던곳이..
		return sqlSession.selectOne("Members.idcheck",id); // 이부분이 mamber.xml 에서 mapper Members
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
