package com.gdu.cashbook.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("C:\\elicles\\git-cashbook\\cashbook\\src\\main\\resources\\static\\upload\\")
	private String path;
	
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
//	public void modifyMember(Member member) {
//		//1.멤버 수정할 이미지 파일을 가져와야한다.
//		String memberPic = memberMapper.selectMemberPic(member.getMemberPic());
//		// 파일수정하기
//		File file = new File(path+memberPic);
//		if(!file.exists()) {
//			file.mkdir();
//		}
//		Memberid memberid = new Memberid();
//		memberid.setMemberId(member.getMemberId());
//		memberidMapper.insertMemberid(memberid);
//		
//		memberMapper.updateMember(member);
//	}
	
	//삭제  1
	public void removeMember(LoginMember loginMember) {
		//1.멤버 이미지 파일 삭제 , 파일 이름을 가져와야한다.SELECT member_pic FROM member
		String memberPic = memberMapper.selectMemberPic(loginMember.getMemberId());
		
		// 그다음 파일삭제
		File file = new File(path+memberPic);
		if(file.exists()) {
			file.delete();
		}
		//2.
		Memberid memberid = new Memberid();		
		memberid.setMemberId(loginMember.getMemberId());
		memberidMapper.insertMemberid(memberid);
		
		//3.
		memberMapper.deleteMember(loginMember);
	}
	
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	public String memberIdCheck(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck); //아이디가 리턴되거나 , 결과물이없을시  null이 리턴된다.
	}
	
//수정
	public void modifyMember(MemberForm memberForm) {
		String originMemberPic = memberMapper.selectMemberPic(memberForm.getMemberId());
		MultipartFile mf = memberForm.getMemberPic();
		String originName = mf.getOriginalFilename();
		System.out.println(originName+"<----originName");
		String memberPic = null;
		//파일이 입력안되시 그전파일이랑 이름이 같게
		if(originName.equals("")) {
			memberPic = originMemberPic;
		}else { //파일확장자
			File file = new File(path+originMemberPic);
			//새로운 파일이 입력되면 그전파일은 삭제한다.
			if(file.exists() && !originMemberPic.equals("default.jpg")) {
				//exists = 파일이존재하는지 여부확인 , 반환결과가 boolean으로 파일이 존재하면 참, 없으면거짓을 반환
				file.delete();
			}
			int lastDot = originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			System.out.println(extension+"<-----extension");
			memberPic = memberForm.getMemberId()+extension;
			System.out.println(memberPic+"<-----memberPic");
		}
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberDate(memberForm.getMemberDate());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberPic(memberPic);
		memberMapper.updateMember(member);
		
		if(!originName.equals("")) {
			//file 을 저장
			File file = new File(path+memberPic);
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			} 
		}
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
		String memberPic = null;
				
		if(originName.equals("")) {
			memberPic = "default.jpg";
		}else {
			int lastDot= originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			//새로운 이름을 생성한다 : UUID
			System.out.println(extension);
			memberPic = memberForm.getMemberId()+extension;
			System.out.println(memberPic);
		}
		
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
		// /슬러시는 Linux \ 역슬러시는 Window
		
		
		//2.파일을저장한다.
		File file = new File(path+memberPic);
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
