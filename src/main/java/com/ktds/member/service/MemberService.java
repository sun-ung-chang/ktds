package com.ktds.member.service;

import java.util.List;

import com.ktds.member.vo.MemberVO;


public interface MemberService {
	
	public boolean createMember(MemberVO memberVO);
	
	public MemberVO readOneMember(MemberVO memberVO); 
	
	public List<MemberVO> readAllMembers();
	
	public boolean isBlockUser(String userId);

	public boolean unBlockUser(String userId);

	public boolean blockUser(String userId);

	public boolean increaseLoginFailCount(String userId);
	
	
}
