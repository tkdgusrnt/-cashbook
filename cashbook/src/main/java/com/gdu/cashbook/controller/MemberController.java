package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {	//회원가입폼을 만들기 위한 
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
		
	}
	@PostMapping("/login")
	public String Login(LoginMember loginMember, HttpSession session) { //HttpSession session = request.getSession();
		System.out.println(loginMember);
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println("returnLoginMember:"+returnLoginMember);
		if(returnLoginMember == null) { //로그인실패시
			return "redirect:/login";
		}else { // 로그인 설공시
			session.setAttribute("loginMember", loginMember);
			return "redirect:/"; ///index"; 같은것이다
		}
		
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/"; //로그아웃 시 로그인창으로 가겠다.
	}
	
	
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
		
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		memberService.addMember(member);
		System.out.println(member);
		return "redirect:/index";
	}
}
