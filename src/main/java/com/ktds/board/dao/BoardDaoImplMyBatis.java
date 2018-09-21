package com.ktds.board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;

@Repository
public class BoardDaoImplMyBatis extends SqlSessionDaoSupport implements BoardDAO{

	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int insertBoard(BoardVO boardVO) {
		return getSqlSession().insert("BoardDAO.insertBoard", boardVO);
	}

	@Override
	public int updateBoard(BoardVO boardVO) {
		return getSqlSession().update("BoardDAO.updateBoard", boardVO);
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return getSqlSession().selectOne("BoardDAO.selectOneBoard", id);
	}

	@Override
	public int deleteOneBoard(int id) {
		return getSqlSession().delete("BoardDAO.deleteOneBoard", id);
	}

	@Override
	public List<BoardVO> selectAllBoards(BoardSearchVO boardSearchVO) {
		return getSqlSession().selectList("BoardDAO.selectAllBoards", boardSearchVO);
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {

		return getSqlSession().selectOne("BoardDAO.selectAllBoardsCount", boardSearchVO);
	}
	


}
