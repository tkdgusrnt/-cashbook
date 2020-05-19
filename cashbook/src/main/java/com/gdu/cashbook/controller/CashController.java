package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(Model model, HttpSession session, LocalDate date) {
		System.out.println(date);
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
	/*	
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		오늘의 날짜와 로그인한 아이디를 보내줘야한다.
		Date day= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strToday = sdf.format(day);//오늘의 날짜가 출력된다.
		System.out.println(strToday+"<-----strToday");
		
		
		
		Cash cash = new Cash(); // +id, date("yyyy-mm-dd")
		cash.setMemberId(loginMemberId);
		cash.setCashDate(strToday);
		List<Cash> cashList = cashService.getCashListByDate(cash);
		model.addAttribute("cashList", cashList);
		model.addAttribute("day", day);
		
		for(Cash c : cashList) {
			System.out.println(c);
		}
	*/
		return "getCashListByDate";
	
	}
	
}
