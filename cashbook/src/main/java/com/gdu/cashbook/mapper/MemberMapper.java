package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper


public interface MemberMapper {
	//회원정보
	public Member selectMemberOne(LoginMember loginMember);
	
	//아이디 중복체크
	public String selectMemberId(String MemberIdCheck);
	
	//회원가입을 위한 
	public void insertMember(Member member);
	
	//로그인 아이디 
	public LoginMember selectLoginMember(LoginMember loginMember);
}
