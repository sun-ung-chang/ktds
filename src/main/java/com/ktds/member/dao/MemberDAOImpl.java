package com.ktds.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO{

	
	public interface Query{
		int INSERT_QUERY = 0;
		int SELECT_QUERY = 1;
		int SELECT_ALL_QUERY = 2;
		int SELECT_LOGIN_QUERY = 3;
		int UPDATE_POINT_QUERY = 4;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="memberQueries")
	private List<String> memberQueries;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		
		return jdbcTemplate.update(
					this.memberQueries.get(Query.INSERT_QUERY) 
					, memberVO.getEmail()
					, memberVO.getName()
					, memberVO.getPassword()
				);
	}

	@Override
	public MemberVO selectOneMember(MemberVO memberVO) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(
					this.memberQueries.get(Query.SELECT_LOGIN_QUERY)
					, new Object[] {memberVO.getEmail(), memberVO.getPassword()}
					, new RowMapper<MemberVO>() {
						
						@Override
						public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							return BindData.bindData(rs, new MemberVO());
						}});
	}

	@Override
	public List<MemberVO> selectAllMember() {
		return jdbcTemplate.query(
					this.memberQueries.get(Query.SELECT_ALL_QUERY)
					,new RowMapper<MemberVO>() {

						@Override
						public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
							return BindData.bindData(rs, new MemberVO());
						}});
	}

	@Override
	public int updatePoint(Map<String, Object> param) {
		return jdbcTemplate.update(
						this.memberQueries.get(Query.UPDATE_POINT_QUERY)
						, param.get("point")
						, param.get("email")
						);
	}

	@Override
	public int isBlockUser(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean unBlockUser(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean blockUser(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean increaseLoginFailCount(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
