package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;

@Controller
public class MemberController {	//회원가입폼을 만들기 위한 
	@Autowired
	private MemberService memberService;
	//비밀번호 찾기
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		int row = memberService.getMemberPw(member);
		System.out.println(row+"<-------row");
		String msg = "아이디와 메일을 확인하세요";
		if(row==1) {
			msg="비밀번호를 입력한 메일로 발송하였습니다.";
		}	
		model.addAttribute("msg", msg);
		return "MemberPwView";
		
	}
	
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	//아이디찾기
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	@PostMapping("/findMemberId")
	 public String findMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		String memberIdPart= memberService.getMemberIdByMember(member);
		System.out.println(memberIdPart+"<----memberIdPart");
		if(memberIdPart == null) {
			model.addAttribute("msg", "입력하신정보와 일치하지않습니다");
			 return "findMemberId";
		}
		memberIdPart = "회원님의 아이디는" + memberIdPart + "입니다";
 		model.addAttribute("memberIdPart", memberIdPart);
		return "MemberIdView";
	}
	
	//회원수정
	@GetMapping("/modifyMember")
	public String modifyMember(Model model, HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		model.addAttribute("member", member);
		return "modifyMember";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(Member member,HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		memberService.modifyMember(member);
		return "redirect:/memberInfo";
		
	}
	
	
	//회원삭제
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		
		return "removeMember"; // input type = "password" name="memberPw"
	}
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, @RequestParam("memberPw") String memberPw) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		memberService.removeMember(loginMember);
		session.invalidate();
		return "redirect:/";
	}
	
	//회원정보
	@GetMapping("/memberInfo")
	public String memberInfo(Model model, HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)(session.getAttribute("loginMember")));
		System.out.println(member);
		model.addAttribute("member", member);
		return "memberInfo";
	}
	
	@PostMapping("/memberIdCheck")
	public String memberIdCheck(Model model, HttpSession session, @RequestParam("memberIdCheck") String memberIdCheck) {
		//로그인 중일떄
				if(session.getAttribute("loginMember") !=null) {
					return "redirect:/";
				}
		String confirmMeberId = memberService.memberIdCheck(memberIdCheck);
		if(confirmMeberId == null) {
			//아이디를 사용할수 있습니다.
			System.out.println("아이디를 사용할수있습니다.");
			model.addAttribute("memberIdCheck", memberIdCheck);
		}else {
			//아이디를 사용할수 없습니다.
			System.out.println("아이디를 사용할수없어요.");
			model.addAttribute("msg", "사용중인 아이디입니다");
		}
		return "addmember";
	}
	
	@GetMapping("/login")
	public String login(HttpSession session) {
		//로그인 중일떄
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		//로그인이아닐떄
		return "login";
		
	}
	@PostMapping("/login")
	public String Login(Model model, LoginMember loginMember, HttpSession session) { //HttpSession session = request.getSession();
		//로그인 중일떄
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		System.out.println(loginMember);
		LoginMember returnLoginMember = memberService.login(loginMember);
		System.out.println("returnLoginMember:"+returnLoginMember);
		if(returnLoginMember == null) { //로그인실패시
			model.addAttribute("msg", "아이디와 비밀번호를 확인바랍니다");
			return "login";
		}else { // 로그인 설공시
			session.setAttribute("loginMember", loginMember);
			return "redirect:/home"; ///index"; 같은것이다
		}
		
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		//로그인 아닐떄
		if(session.getAttribute("loginMember") ==null) {
			return "redirect:/";
		}
		session.invalidate();
		return "redirect:/"; //로그아웃 시 로그인창으로 가겠다.
	}
	
	
	
	//회원가입
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		//로그인 중일떄
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		return "addMember";
		
	}
	
	@PostMapping("/addMember")
	public String addMember(MemberForm memberForm, HttpSession session) {
		//로그인 중일떄
		if(session.getAttribute("loginMember") !=null) {
			return "redirect:/";
		}
		
		System.out.println(memberForm+"<--------memberForm");
		memberService.addMember(memberForm);
		//service : memberForm -> member타입으로 변경시킨다., 폴더에 파일도저장해야한다.
		//System.out.println(member);
		return "redirect:/index";
	}
}
