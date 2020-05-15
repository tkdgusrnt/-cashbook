package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper


public interface MemberMapper {
	//비밀번호찾기
	
	public int updateMemberPw(Member member);
	
	//아이디 찾기
	public String selectMemberIdByMember(Member member);
	
	//수정하기
	public void updateMember(Member member);
	
	//회원탈퇴
	public void deleteMember(LoginMember loginmember);
	
	//회원정보
	public Member selectMemberOne(LoginMember loginMember);
	
	//아이디 중복체크
	public String selectMemberId(String MemberIdCheck);
	
	//회원가입을 위한 
	public void insertMember(Member member);
	
	//로그인 아이디 
	public LoginMember selectLoginMember(LoginMember loginMember);
}
