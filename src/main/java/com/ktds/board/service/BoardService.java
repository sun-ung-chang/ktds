package com.ktds.board.service;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.member.vo.MemberVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

public interface BoardService {

	public boolean createBoard(BoardVO boardVO, MemberVO memberVO);
	
	public boolean updateBoard(BoardVO boardVO);
	
	public BoardVO readOneBoard( int id, MemberVO memberVO);

	//오버로딩
	public BoardVO readOneBoard( int id);
	
	public boolean deleteOneBoard(int id);
	
	public PageExplorer readAllBoards(BoardSearchVO boardSearchVO);
}
