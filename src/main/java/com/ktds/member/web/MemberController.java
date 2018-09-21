package com.ktds.member.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.common.session.Session;
import com.ktds.common.util.SendMessage;
import com.ktds.member.service.MemberService;
import com.ktds.member.validator.MemberValidator;
import com.ktds.member.vo.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping("/member/logout")
	public String doMemeberLogOutAction( HttpSession session ) {
		
		//Logout
		session.invalidate();
		return "redirect:/member/login";
	}
	

	
	@GetMapping("/member/login")
	public String viewMemberLoginPage() {
		
		return "member/login";
	}
	
	@PostMapping("/member/login")
	public ModelAndView doMemberLoginAction( 
					@Validated({MemberValidator.Login.class}) @ModelAttribute MemberVO memberVO
					, Errors errors
					, HttpSession session
					, HttpServletResponse response) {
		
		boolean isBlockAccount = memberService.isBlockUser(memberVO.getEmail());
	    boolean isLoginSuccess = false;
	    
	    ModelAndView view = new ModelAndView("redirect:/board/list");
	    MemberVO loginMemberVO = this.memberService.readOneMember(memberVO);
	    if ( loginMemberVO != null ) {
	    	isLoginSuccess = true;
	    }else {
	    	isLoginSuccess = false;
	    }
	    if ( !isBlockAccount ){
	    	 
	         if ( !isLoginSuccess ){
	            memberService.increaseLoginFailCount(memberVO.getEmail());
	         }
	         else {
	            memberService.unBlockUser(memberVO.getEmail());
	         }
	      }
	      else {
	         SendMessage.send(response, "BLOCK_ACCOUNT");
	         return view;
	      }

	      if ( isLoginSuccess ) {
	    	  String token = UUID.randomUUID().toString();
	    	  session.setAttribute(Session.CSRF_TOKEN, token);
	      }
		
		
		if ( errors.hasErrors() ) {
			view.setViewName("member/login");
			view.addObject("memberVO", memberVO);
			return view;
		}
		
		
		session.setAttribute(Session.USER, loginMemberVO);
		
		//로그인 체크하는 방법
		//jsp에다가 세션을 정보를 불러오는 방법
		
		
		return 	view;
		
	}
	
	
	@GetMapping("/member/signup")
	public String viewMemberSignupPage() {
		
		return "member/signup";
	}
	
	// /member/check/duplicate?email=값
	@PostMapping("/member/check/duplicate")
	@ResponseBody
	public Map<String, Object> doCheckDuplicateEmail(
								@RequestParam String email){
		
		/**
		 * Map쓰는 이유
		 * ajax 브라우저가 아닌 자바스크립트가 처리한다.
		 * 응답
		 * 
		 */
		
		Random random = new Random();
		
		
		
		Map<String, Object> result = new HashMap<>();
		result.put("status", "OK");
		result.put("duplicated", email);
		return result;
	}
	
	@PostMapping("/member/signup")
	public ModelAndView doMemberSignup( 
			@Validated({MemberValidator.Signup.class}) @ModelAttribute MemberVO memberVO
			, Errors errors
			, HttpServletResponse response) {
		
		ModelAndView view = new ModelAndView("redirect:/member/login");
		
		if ( errors.hasErrors() ) {
			view.setViewName("member/signup");
			view.addObject("memberVO", memberVO);
			return view;
		}
		
		String password = memberVO.getPassword();
		String passwordPolicy = "((?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,})";
		
		Pattern pattern = Pattern.compile(passwordPolicy);
		Matcher matcher = pattern.matcher(password);
		
		if ( !matcher.matches() ) {
			return view;
		}
		this.memberService.createMember(memberVO);
		
		return 	view;
		
	}
	

}
