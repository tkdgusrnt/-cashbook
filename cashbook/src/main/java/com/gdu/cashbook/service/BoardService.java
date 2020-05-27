package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.Board;

@Service
@Transactional
public class BoardService {
	@Autowired BoardMapper boardMapper;
	
	//board 상세정보 출력
	public Map<String, Object> boardOne(int boardNo){
		//map 에 상세보기와 처음게시판보드와 마지막보드넘버를 보내주자
		int firstBoardNo = boardMapper.firstBoardNo();
		int lastBoardNo = boardMapper.lastBoardNo();
		Board board = boardMapper.selectBoardOne(boardNo);
		int previousNo = boardMapper.previousNo(boardNo);
		int nextNo = boardMapper.nextNo(boardNo);
		
		System.out.println(firstBoardNo + "<-----------처음게시판글");
		System.out.println(lastBoardNo + "<------------마지막게시판글");
		System.out.println(board + "<---------board");
		System.out.println(previousNo + "<-----------이전 컬럼");
		System.out.println(nextNo + "<-----------다음 컬럼");
		
		//맵의 추가
		Map<String, Object> map = new HashMap<>();
		map.put("firstBoardNo", firstBoardNo);
		map.put("lastBoardNo", lastBoardNo);
		map.put("board", board);
		map.put("previousNo", previousNo);
		map.put("nextNo", nextNo);
		return map;
		
	}
	
	//board 리스트 출력
	public Map<String, Object> BoardList(int currentPage, String searchWord){
		//디버깅
		System.out.println(currentPage);
		System.out.println(searchWord);
		
		//페이징에 필요한것들을 map 담아보자
		int rowPerPage = 5;
		int beginRow = (currentPage-1)*rowPerPage;
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("searchWord", searchWord);
		map.put("beginRow", beginRow);
		map.put("rowPerPage", rowPerPage);
		
		//라스트 페이지 출력
		int totalRow = boardMapper.getTotalRow(searchWord);
		int lastPage = totalRow/rowPerPage;
		if(totalRow%rowPerPage !=0) {
			lastPage+=1;
		}
		
		List<Board> list = boardMapper.selectBoardList(map);
		Map<String, Object> map2 = new HashMap<>();
		map.put("list", list);
		map.put("lastPage", lastPage);
		
		
		return map2;
		
	} 
}
