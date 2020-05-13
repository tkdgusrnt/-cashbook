package com.gdu.cashbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.Member;

@Controller
public class MemberController {	//회원가입폼을 만들기 위한 
	@Autowired
	private MemberService memberService;
	
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
