package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.BoardService;

@Controller
public class BoardController {
@Autowired BoardService boardService;

	//게시판 리스트 출력 및 페이징하기
	@GetMapping("/boardList")
	public String boardList(Model model, HttpSession session, @RequestParam(value="currentPage", defaultValue = "1")int currentPage, @RequestParam(value = "searchWord", defaultValue = "")String searchWord) {
		
		if(session.getAttribute("loginMember")==null) {
			 return "redirect:/";
		}
				
		System.out.println(currentPage);
		
			//리스트 디버깅
			Map<String, Object> map = boardService.BoardList(currentPage, searchWord);
			System.out.println(map.get("list")+"<---------------list");
			System.out.println(map.get("lastPage")+"<------------lastPage");
			
			LocalDate localDate = LocalDate.now();
			System.out.println(localDate+"<-----localDate");
			
			//model에 list를 담아서 보내주기
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("list", map.get("list"));
			model.addAttribute("lastPage", map .get("lastPage"));
			model.addAttribute("localDate", localDate);

			System.out.println(map.get("list")+"<---------------list");
			System.out.println(map.get("lastPage")+"<------------lastPage");
		return "/boardList";
			
	}
}
