package com.ktds.member.biz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ktds.member.dao.MemberDAO;

//@Component Controller Service Repository의 부모
@Component
public class MemberBizImpl implements MemberBiz{
	
	@Autowired
	@Qualifier("memberDaoImplMyBatis")
	private MemberDAO memberDAO;

	@Override
	public int updatePoint(String email, int point) {
		Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		param.put("point", point);
		

		return memberDAO.updatePoint(param);
	}

	@Override
	public boolean isBlockUser(String userId) {
		
		int loginFailCount = memberDAO.isBlockUser(userId);
		
		
		return loginFailCount >= 3;
	}

	@Override
	public boolean unBlockUser(String userId) {
		return memberDAO.unBlockUser(userId);
	}

	@Override
	public boolean blockUser(String userId) {
		return memberDAO.blockUser(userId);
	}

	@Override
	public boolean increaseLoginFailCount(String userId) {
		return memberDAO.increaseLoginFailCount(userId);
	}
	

}
