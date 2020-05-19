package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(Model model, HttpSession session, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		
		if(day == null) {
			day = LocalDate.now();
		}
			System.out.println(day+"<-----day");		
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		
		//오늘의 날짜와 로그인한 아이디를 보내줘야한다.
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String strToday = "";
		strToday = dateTime.format(day);//오늘의 날짜가 출력된다.
		System.out.println(strToday+"<-----strToday");
		
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(loginMemberId+"<---------loginMemberId");
		
		Cash cash = new Cash(); // +id, date("yyyy-mm-dd")
		cash.setMemberId(loginMemberId);
		cash.setCashDate(strToday);
		
		
		Map<String, Object> map = cashService.getCashListByDate(cash);
		model.addAttribute("cashList", map.get("cashList"));
		
		model.addAttribute("cashFindSum",map.get("cashFindSum"));   
		model.addAttribute("day", day);
		System.out.println(day+"<--------day");
		//for(Cash c : cashList) {
		//	System.out.println(c+"<--------c");
		//}
	
		return "getCashListByDate";
	
	}
	
}
