package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Comment;

@Mapper
public interface CommentMapper {

	//comment List 출력
	public List<Comment> getCommentList(int boardNo);
	
	//게시글의 댓글 총 갯수
	public int totalComment(int boardNo);
}
