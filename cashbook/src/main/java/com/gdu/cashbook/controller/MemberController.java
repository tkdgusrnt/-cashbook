package com.gdu.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.mapper.Member;

@Controller
public class MemberController {
	@GetMapping("/addMember")
	public String addMember() {
		return "addMember";
		
	}
	
	@PostMapping("/addMember")
	public String addMember(Member member) {
		System.out.println(member);
		return "redirect:/index";
	}
}
