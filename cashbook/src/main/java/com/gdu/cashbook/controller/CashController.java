package com.gdu.cashbook.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.LoginMember;
import com.sun.mail.iap.Response;

@Controller
public class CashController {
	@Autowired CashService cashService;
	
	
	@GetMapping("/removeCash")
	public String removeCash(HttpServletRequest request,HttpServletResponse response,Model model, HttpSession session, Cash cash) throws IOException {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		
		System.out.println(cash.getCashNo()+"<----cashNo");
		System.out.println(cash.getMemberId()+"<---cashMemberId");
		model.addAttribute("msg", "삭제시 다시 되돌릴수없습니다. 그래도 삭제하시겠습니까?");
		/*
		response.setCharacterEncoding("UTF-8");
		 PrintWriter writer = response.getWriter();
		 writer.println("<script type='text/javascript'>");
		 writer.println("alert('삭제시 다시 되돌릴수없습니다. 그래도 삭제하시겠습니까?');");
		 writer.println("history.back();");
		 writer.println("</script>");
	     writer.flush();
	     */
		cashService.removeCash(cash);
		//LocalDate day=LocalDate.now(); model.addAttribute("day", day);
		return "redirect:/getCashListByDate";
	}
	
	@GetMapping("/getCashListByMonth")
	public String getCashListByMonth(Model model,HttpSession session,@RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/"; //로그인이 안되면 할수없게하자
		}
		
		Calendar cDay = Calendar.getInstance(); // 오늘날짜를 구하는
		
		if(day == null) {
			day= LocalDate.now();//day를 cDay형으로 변환(찾아보자)
		}else {
			/* day -->cDay
			 * LocalDate - > Calendar
			 * LocalDate - > Date -> Calendar
			 * LocalDate - > String - > Calendar
			 * LocalDate - > Calendar
			 */
			cDay.set(day.getYear(), day.getMonthValue()-1,day.getDayOfMonth()); //오늘 날짜에서 day갑과 동일한 값으로 변경된다.
		}
		
			/* 0. 오늘 LocalDate 타입
			 * 1. 오늘 Calendar 타입
			 * 2. 이번달의 마지막날
			 * 3. 이번달 1일의 요일
			 * */
		
		
		//일별 수입, 지출 총액을 가져오겠다.
		String memberId =((LoginMember) session.getAttribute("loginMember")).getMemberId();
		int year = cDay.get(Calendar.YEAR);
		int month =cDay.get(Calendar.MONTH)+1;
		
		List<DayAndPrice> dayAndPriceList = cashService.getDayAndPriceList(memberId, year, month);
		 System.out.println(dayAndPriceList+"<--------dayAndPriceList");
		 for(DayAndPrice dp: dayAndPriceList) {
			 System.out.println();
		 }
		
		model.addAttribute("dayAndPriceList", dayAndPriceList);
		model.addAttribute("day", day);
		model.addAttribute("month", cDay.get(Calendar.MONTH)+1); // 현재구하려는 월
		model.addAttribute("lastDay", cDay.getActualMaximum(Calendar.DATE)); // 마지막날을 구하는
		
		
		Calendar firstDay = cDay;
		firstDay.set(Calendar.DATE, 1); // 일 을 1일로 변경
		System.out.println(firstDay.get(Calendar.DAY_OF_WEEK)+"<-------firstDay");//cDay.get(Calendar.DAY_OF_WEEK); // 1이면 일요일 2이면 월요일 ........7이면토요일이된다.
		System.out.println(firstDay.get(Calendar.YEAR)+","+(firstDay.get(Calendar.MONTH)+1)+","+firstDay.get(Calendar.DATE));
		model.addAttribute("firstDayOfWeek", firstDay.get(Calendar.DAY_OF_WEEK));
		return "/getCashListByMonth";	
	}
	
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(Model model, HttpSession session, @RequestParam(value="day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/"; //로그인이 안되면 할수없게하자
		}
		
		if(day == null) {
			day = LocalDate.now();
		}
			System.out.println(day+"<-----day");		
		
		
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
		
		//Integer cashFindSum = (Integer)map.get("cashFindSum");
		
		model.addAttribute("cashList", map.get("cashList"));
		System.out.println(map.get("cashList")+"<---------cashList");
		model.addAttribute("cashFindSum",map.get("cashFindSum"));  
		System.out.println(map.get("cashFindSum")+"<---------cashFindSum");
		model.addAttribute("day", day);
		System.out.println(day+"<--------day");
		//for(Cash c : cashList) {
		//	System.out.println(c+"<--------c");
		//}
	
		return "getCashListByDate";
	
	}
	
}
