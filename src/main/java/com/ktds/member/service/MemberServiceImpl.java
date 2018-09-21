package com.ktds.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.member.biz.MemberBiz;
import com.ktds.member.dao.MemberDAO;
import com.ktds.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	@Qualifier("memberDaoImplMyBatis")
	private MemberDAO memberDAO;
	
	@Autowired
	private MemberBiz memberBiz;

	@Override
	public boolean createMember(MemberVO memberVO) {
		return this.memberDAO.insertMember(memberVO) > 0;
	}

	@Override
	public MemberVO readOneMember(MemberVO memberVO) {
		
		MemberVO loginMember = this.memberDAO.selectOneMember(memberVO);
		if ( loginMember != null ) {
			
			this.memberBiz.updatePoint(memberVO.getEmail(), +2);
			
			
			int point  = loginMember.getPoint();
			point +=2;
			loginMember.setPoint(point);

		}
		return loginMember;
	}

	@Override
	public List<MemberVO> readAllMembers() {
		return this.memberDAO.selectAllMember();
	}

	@Override
	public boolean isBlockUser(String userId) {
		return this.memberBiz.isBlockUser(userId);
	}

	@Override
	public boolean unBlockUser(String userId) {
		return this.memberBiz.unBlockUser(userId);
	}

	@Override
	public boolean blockUser(String userId) {
		return this.memberBiz.blockUser(userId);
	}

	@Override
	public boolean increaseLoginFailCount(String userId) {
		// TODO Auto-generated method stub
		return this.memberBiz.increaseLoginFailCount(userId);
	}



}
