package com.ktds.member.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.member.vo.MemberVO;

@Repository
public class MemberDaoImplMyBatis extends SqlSessionDaoSupport implements MemberDAO {

	private Logger logger = LoggerFactory.getLogger(MemberDaoImplMyBatis.class);
	
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		logger.debug("Initiate MyBatis");
		super.setSqlSessionTemplate(sqlSessionTemplate);
		
	}

	@Override
	public int insertMember(MemberVO memberVO) {
		return getSqlSession().insert("MemberDAO.insertMember", memberVO);
	}

	@Override
	public MemberVO selectOneMember(MemberVO memberVO) {
		return getSqlSession().selectOne("MemberDAO.selectOneMember", memberVO);
	}

	@Override
	public List<MemberVO> selectAllMember() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updatePoint(Map<String, Object> param) {
		
			
		return getSqlSession().update("MemberDAO.updatePoint", param);
	}

	@Override
	public int isBlockUser(String userId) {
		return getSqlSession().selectOne("MemberDAO.isBlockUser",userId);
	}

	@Override
	public boolean unBlockUser(String userId) {
		return getSqlSession().update("MemberDAO.unBlockUser", userId) > 0;
	}

	@Override
	public boolean blockUser(String userId) {
		return false;
	}

	@Override
	public boolean increaseLoginFailCount(String userId) {
		return getSqlSession().update("MemberDAO.increaseLoginFailCount", userId) > 0;
	}

}
