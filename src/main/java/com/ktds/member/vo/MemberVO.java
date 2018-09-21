package com.ktds.member.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.ktds.common.dao.support.Types;
import com.ktds.member.validator.MemberValidator;

public class MemberVO {
	
	@Types(alias="M_EMAIL")
	@NotEmpty(message="이메일은 필수 입력값입니다."
				, groups={MemberValidator.Signup.class, MemberValidator.Login.class})
	@Email(message="이메일 형식으로 작성해주세요"
				, groups={MemberValidator.Signup.class, MemberValidator.Login.class})
	private String email;
	@Types
	@NotEmpty(message="이름은 필수 입력값입니다."
				, groups={MemberValidator.Signup.class})
	private String name;
	@Types
	@NotEmpty(message="비밀번호는 필수 입력값입니다."
				, groups={MemberValidator.Signup.class, MemberValidator.Login.class})
	@Length(min=10, max=20, message="비밀번호는 10~20글자 사이 값을 입력하세요"
				, groups={MemberValidator.Signup.class})
	private String password;
	@Types
	private int point;
	
	private	int loginFailCount;
	private String latestLogin;
	
	
	public int getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getLatestLogin() {
		return latestLogin;
	}

	public void setLatestLogin(String latestLogin) {
		this.latestLogin = latestLogin;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		
		String format = "MemberVO: [Email: %s, Name: %s, Password: %s, Point: %d]";
		return String.format(format
							, this.email
							, this.name
							, this.password
							, this.point);
	}
	


}






