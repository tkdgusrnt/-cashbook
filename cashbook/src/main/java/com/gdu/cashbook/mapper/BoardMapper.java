package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;

@Mapper
public interface BoardMapper {
	//해당 넘버의 다음 넘버를 가져오는 쿼리
	public int nextNo(int boardNo);
	
	//해당 넘버의 이전 넘버를 가져오는 쿼리
	public int previousNo(int boardNo);
	
	//boardList 출력하기
	public List<Board> selectBoardList(Map<String, Object> map);
	
	//라스트페이지
	public int getTotalRow(String searchWord);
	
	//boardNo 받아서 해당게시글 정보 가져오기
	public Board selectBoardOne(int boardNo);
	
	//처음 게시판 글 
	public int firstBoardNo();
	
	// 이전, 다음 게시판글을 출력하기 위해 마지막 번호구해오기
	public int lastBoardNo();
	
	//게시글 작성
	public int addBoard(Board board);
	
	//게시글 삭제하기
	public int removeBoard(int boardNo);
}
