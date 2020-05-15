package com.gdu.cashbook.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
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
	
	public void addMember(Member member) { // 회원가입을 위한  insert 서비스
		memberMapper.insertMember(member);
	}
	
	public LoginMember login(LoginMember loginMember) {  //로그인을 하기위한  select 서비스
		return memberMapper.selectLoginMember(loginMember);
		
	}
}
