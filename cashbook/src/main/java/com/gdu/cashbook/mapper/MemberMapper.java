package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.Memberid;

@Mapper


public interface MemberMapper {
	//멤버 총 인원 구하기
	public int getTotalMember();
	
	//멤버 한명 삭제..관리자만 가능
	public int removeAdmin(String memberId);
	
	//멤버 리스트 출력
	public List<Member> selectMemberListAll(Map<String, Object> map);
	
	//이미지 삭제
	public String selectMemberPic(String memberId);
	
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
