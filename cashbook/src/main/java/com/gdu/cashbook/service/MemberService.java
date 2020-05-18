package com.gdu.cashbook.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;
import com.gdu.cashbook.vo.Memberid;


@Service
@Transactional  //이클래스안에서 하나라도 예외가 생길경우 취소시킨다.
public class MemberService {
	
	@Autowired private MemberMapper memberMapper;
	@Autowired private MemberidMapper memberidMapper;
	@Autowired private JavaMailSender javaMailSender;
	
	//비밀번호찾기
	public int getMemberPw(Member member) {
		//비밀번호 추가
		UUID uuid = UUID.randomUUID(); //랜덤문자열 생성라이브러리(API)
		String memberPw=uuid.toString().substring(0,8);		
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		if(row == 1) {
			System.out.println(memberPw+"<---update memberPw");
			//메일로 UPDATE성공한 랜덤PW를 전송
			//매일 객체 ? new JavaMailSender();
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			//받는사람
			simpleMailMessage.setTo(member.getMemberEmail());
			//보낸사람
			simpleMailMessage.setFrom("tkdgusrnt11@gmail.com");
			//제목
			simpleMailMessage.setSubject("cashBook 비밀번호 찾기");
			//답변
			simpleMailMessage.setText("변경된 비밀번호는:"+ memberPw+"입니다!!! 새로운비밀번호로 변경하시오");
			javaMailSender.send(simpleMailMessage);
			
		}
		return row;
	}
	
	//아이디찾기
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	
	//수정
	public void modifyMember(Member member) {
		memberMapper.updateMember(member);
	}
	
	//삭제  1
	public void removeMember(LoginMember loginMember) {
		Memberid memberid = new Memberid();		
		memberid.setMemberId(loginMember.getMemberId());
		memberidMapper.insertMemberid(memberid);
		
		//2
		memberMapper.deleteMember(loginMember);
	}
	
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	public String memberIdCheck(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck); //아이디가 리턴되거나 , 결과물이없을시  null이 리턴된다.
	}
	
	public void addMember(MemberForm memberForm) { // 회원가입을 위한  insert 서비스
		MultipartFile mf = memberForm.getMemberPic();
		//확장자가 필요하다.
	
		String originName = mf.getOriginalFilename();
//		if(mf.getContentType().equals("image/jpg") || mf.getContentType().equals("image/png")){
//			//업로드
//		}else {
//			//업로드실패
//		}
		
		System.out.println(originName+"<-------original");
		int lastDot = originName.lastIndexOf("."); // mm.jpg
		String extension = originName.substring(lastDot);
		//새로운 이름을 생성한다 : UUID
		String memberPic =memberForm.getMemberId()+extension;
		//1.DB에 저장한다.
		Member member = new Member();   
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberDate(memberForm.getMemberDate());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPic(memberPic);
		
		System.out.println(member+"<---MemberService.addMember:member");
		memberMapper.insertMember(member);
		
		String path = "C:\\elicles\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload";
		
		//2.파일을저장한다.
		File file = new File(path+"\\"+memberPic);
		try {
			mf.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 
			
		
		//memberForm ->member
		//파일을 디스크에 물리적으로 저장하여야한다.
		// memberMapper.insertMember(member);
		//Exception
		//1. 예외처리를 해야만 문법적으로 이상없는 예외가있다.
		//2. 예외처리를 코드에서 구현하지 않아도 아무 문제 없는 예외가있다. 이것을 RuntimeException이다.
	}
	
	public LoginMember login(LoginMember loginMember) {  //로그인을 하기위한  select 서비스
		return memberMapper.selectLoginMember(loginMember);
		
	}
}
