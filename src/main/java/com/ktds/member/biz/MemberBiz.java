package com.ktds.member.biz;

public interface MemberBiz {
	
	public int updatePoint(String eamil, int point);
	
	public boolean isBlockUser(String userId);

	public boolean unBlockUser(String userId);

	public boolean blockUser(String userId);

	public boolean increaseLoginFailCount(String userId);
}
