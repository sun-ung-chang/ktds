package com.ktds.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ktds.board.dao.BoardDAO;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.member.biz.MemberBiz;
import com.ktds.member.vo.MemberVO;
import com.ktds.reply.dao.ReplyDao;
import com.ktds.reply.vo.ReplyVO;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.PagerFactory;
import io.github.seccoding.web.pager.explorer.ClassicPageExplorer;
import io.github.seccoding.web.pager.explorer.PageExplorer;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	@Qualifier("boardDaoImplMyBatis")
	private BoardDAO boardDAO;
	
	@Autowired
	private ReplyDao replyDao;
	
	
	@Autowired
	private MemberBiz memberBiz;
	

	@Override
	public boolean createBoard(BoardVO boardVO, MemberVO memberVO) {
		
		boolean isUploadFile = boardVO.getOriginFileName().length() > 0;
		
		int point = 10;
		if ( isUploadFile ) {
			point += 10;
		}
		
		this.memberBiz.updatePoint(memberVO.getEmail(), point);
		int memberPoint = memberVO.getPoint();
		memberPoint += point;
		memberVO.setPoint(memberPoint);
		
		boolean isSuccess = this.boardDAO.insertBoard(boardVO) > 0;
		//Integer.parseInt("avc");
		
		return isSuccess;
	}

	@Override
	public boolean updateBoard(BoardVO boardVO) {
		System.out.println("Call BoardService.updateBoard();");
		return this.boardDAO.updateBoard(boardVO) > 0;
	}

	@Override
	public BoardVO readOneBoard(int id, MemberVO memberVO) {
		
		BoardVO boardVO = this.readOneBoard(id);
		
		List<ReplyVO> replyList = this.replyDao.selectAllReplies(id);
		
		boardVO.setReplyList(replyList);

		if (!boardVO.getEmail().equals( memberVO.getEmail())) {
	         
	         
	         if ( memberVO.getPoint() < 2 ) {
	            // /board/list는 예외를 던져줄 경로
	            throw new PolicyViolationException("포인트가 부족합니다.", "/board/list");
	         }
	         
	         // db
	         this.memberBiz.updatePoint(memberVO.getEmail(), -2);
	         // 조회 결과
	         int point = memberVO.getPoint();
	         point -= 2;
	         memberVO.setPoint(point);
	      }
	      return boardVO;
		
	}
	
	@Override
	public BoardVO readOneBoard(int id) {
		
		return this.boardDAO.selectOneBoard(id);
	}

	@Override
	public boolean deleteOneBoard(int id) {
		// TODO Auto-generated method stub
		return this.boardDAO.deleteOneBoard(id) > 0;
	}

	@Override
	public PageExplorer readAllBoards(BoardSearchVO boardSearchVO) {
		
		int totalCount = this.boardDAO.selectAllBoardsCount(boardSearchVO);
		
		Pager pager 
				= PagerFactory.getPager(Pager.ORACLE
										, boardSearchVO.getPageNo() + "");
		
		pager.setTotalArticleCount(totalCount);
		
		PageExplorer pageExplorer 
					= pager.makePageExplorer(ClassicPageExplorer.class
											, boardSearchVO);
		
		pageExplorer.setList( this.boardDAO.selectAllBoards(boardSearchVO) );
		
		return pageExplorer;
	}

}







