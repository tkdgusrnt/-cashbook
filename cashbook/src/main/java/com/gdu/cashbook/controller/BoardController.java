package com.gdu.cashbook.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.BoardForm;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class BoardController {
@Autowired BoardService boardService;
	
	//게시글 수정하기
	@PostMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, Board board) {
		//세션검사
		if(session.getAttribute("loginMember")==null && session.getAttribute("admin")==null) {
			return "redirect:/login";
		}
		//디버깅 체크
		System.out.println(board + "<----modifyBoard");
		boardService.modifyBoard(board);
		
		return "redirect:/BoardOne?boardNo="+board.getBoardNo();
		
	}

	//게시글 수정하기
	@GetMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, @RequestParam(value = "boardNo")int boardNo, Model model) {
		//세션검사
		if(session.getAttribute("loginMember") == null && session.getAttribute("admin")==null ) {
			return "redirect:/login";
		}
		
		//멤버 한명 상세정보 출력하기
		Map<String, Object> mapp = new HashMap<>();
		mapp.put("boardNo", boardNo);
		mapp.put("commentCurrentPage", 1);
		Map<String, Object> map = boardService.boardOne(mapp);
		model.addAttribute("board", map.get("board"));
		//페이지 요청
		return "modifyBoard";
		
	}
	


	//게시글 삭제하기
	@PostMapping("/removeBoard")
	public String removeBoard(HttpSession session, @RequestParam(value = "boardNo") int boardNo) {
		
		//세션검사
		if(session.getAttribute("loginMember")==null && session.getAttribute("admin")==null) {
			return "redirect:/login";
		}
		
		System.out.println(boardNo +"<---------removeBoard");
		
		//삭제요청
		boardService.removeBoard(boardNo);
		
		return "redirect:/boardList";
	}

	//게시글 가져오기
	@GetMapping("/BoardOne")
	public String boardOne(HttpSession session, Model model, @RequestParam(value = "boardNo") int boardNo, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage, @RequestParam(value = "commentCurrentPage", defaultValue = "1")int commentCurrentPage) {
		System.out.println(boardNo + "/////");
		System.out.println("/BoardOne 요청하기");
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/login";
		}
		
		//로그인아이디로 일반회원과 관리자 분리
		System.out.println(session.getAttribute("admin")+"<------adminLogin");
		String loginMemberId = "";
		if(session.getAttribute("admin")!=null) {
			loginMemberId = (String)session.getAttribute("admin");
			System.out.println(loginMemberId + "<-------admin loginMemberId");
		}else {
			loginMemberId = ((LoginMember)(session.getAttribute("loginMember"))).getMemberId();
			System.out.println(loginMemberId+"<-------loginMemberId");
		}
			Map<String, Object> mapp = new HashMap<>();
			mapp.put("boardNo", boardNo);
			mapp.put("commentCurrentPage", commentCurrentPage);
			
		//상세정보 출력, 모델에 담아서 보내주기
			Map<String, Object> map = boardService.boardOne(mapp);
			 model.addAttribute("commentList", map.get("commentList"));
			 model.addAttribute("loginMember", map.get("loginMember"));
			 model.addAttribute("currentPage", map.get("currentPage"));
			 model.addAttribute("board", map.get("board"));
			 model.addAttribute("firstBoardNo", map.get("firstBoardNo"));
			 model.addAttribute("lastBoardNo", map.get("lastBoardNo"));
			 model.addAttribute("nextNo", map.get("nextNo"));
			 model.addAttribute("previousNo", map.get("previousNo"));
			 model.addAttribute("commentCurrentPage",map.get("commentCurrentPage"));
			 model.addAttribute("commentLastPage", map.get("commentLastPage"));
		
		//페이지 요청하기
		return "BoardOne";
	}

	//게시판입력하기
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session) {
		System.out.println("/addBoard 요청하기");
		//로그인이안되어있으면 인덱스화면으로
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "addBoard";
	}
	
	@PostMapping("/addBoard")
	public String addBoard(HttpSession session, BoardForm boardForm) {
		//로그인이 안되어있으면 인덱스화면으로
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		//파일이 입력되었을떄
		MultipartFile mf = boardForm.getBoardPic();
		if(boardForm.getBoardPic() !=null && !mf.getOriginalFilename().equals("")) {
			if(!boardForm.getBoardPic().getContentType().equals("image/jpeg")&& !boardForm.getBoardPic().getContentType().equals("image/png")&& !boardForm.getBoardPic().getContentType().equals("image/gif")) {
				return "redirect:/";
			}
		}
		boardForm.setMemberId(((LoginMember)session.getAttribute("loginMember")).getMemberId());
		boardService.addBoard(boardForm);
		return "redirect:/boardList";
		
	}

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
