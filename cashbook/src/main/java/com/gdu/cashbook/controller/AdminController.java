package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gdu.cashbook.service.AdminService;
import com.gdu.cashbook.vo.Admin;

@Controller
public class AdminController {
	@Autowired private AdminService adminService;
	
	//admin 로그인
	@GetMapping("/adminLogin")
	public String AdminLogin(HttpSession session) {
		 //로그인 중일떄
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		//로그인이 안되어 있을떄
		return "adminLogin";
	}
	@PostMapping("/adminLogin")
	public String Login(HttpSession session, Admin admin, Model model) {
		System.out.println("/addminLogin 요청하기");
		//로그인 결과 가져오기  null일시 메시지와 리턴, null 아닐시 세션에 값을 저장한다.
		String returnAdmin = adminService.selectAdmin(admin).getAdminId();
		System.out.println(returnAdmin + "<-----returnAdmin");
		if(returnAdmin == null) {
			model.addAttribute("msg", "아이디와 비밀번호를 확인하세요");
			return "adminLogin";
		}else {
			session.setAttribute("admin", returnAdmin);
			return "redirect:/home";
		}
		
	}
}
