package com.ktds.member.dao;

import java.util.List;
import java.util.Map;

import com.ktds.member.vo.MemberVO;

public interface MemberDAO {

	public int insertMember(MemberVO memberVO);

	public MemberVO selectOneMember(MemberVO memberVO);

	public List<MemberVO> selectAllMember();

	public int updatePoint(Map<String, Object> param);

	public int isBlockUser(String userId);

	public boolean unBlockUser(String userId);

	public boolean blockUser(String userId);

	public boolean increaseLoginFailCount(String userId);

}
