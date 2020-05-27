package com.gdu.cashbook.vo;

public class Comment {
	private int commentNo;
	private String memberId;
	private String commentContant;
	private int boardNo;
	public int getCommentNo() {
		return commentNo;
	}
	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCommentContant() {
		return commentContant;
	}
	public void setCommentContant(String commentContant) {
		this.commentContant = commentContant;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	@Override
	public String toString() {
		return "Comment [commentNo=" + commentNo + ", memberId=" + memberId + ", commentContant=" + commentContant
				+ ", boardNo=" + boardNo + "]";
	}
}
