package com.ktds.board.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.dao.support.BindData;
import com.ktds.member.vo.MemberVO;


@Repository
public class BoardDAOImpl implements BoardDAO{
	
	
	// 클래스 안에 인터페이스 만드는 것을 Inner Class
	private interface Query{
		//상수는 인테페이스 만든다.
		int INSERT_QUERY = 0;
		int SELECT_QUERY = 1;
		int DELETE_QUERY = 2;
		int SELECT_ALL_QUERY = 3;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource(name="boardQueries")
	private List<String> boardQueries;

	@Override
	public int insertBoard(BoardVO boardVO) {
		//this.jdbcTemplate.update 쓰는 경우는 delete update insert 등 트랜젝션이 필요할 때 사용
		return this.jdbcTemplate.update(
					this.boardQueries.get(Query.INSERT_QUERY)
					, boardVO.getSubject()
					, boardVO.getContent()
					, boardVO.getEmail()
					, boardVO.getFileName()
					, boardVO.getOriginFileName()
				);
	}

	@Override
	public int updateBoard(BoardVO boardVO) {
		System.out.println("Call updateBoard();");
		return 0;
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return this.jdbcTemplate.queryForObject(
					this.boardQueries.get(Query.SELECT_QUERY)
					//배열 초기화
					, new Object[] {id}
					//ctr + shift + o 눌러서 import
					, new RowMapper<BoardVO>() {

						@Override
						public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
							
							MemberVO memberVO = BindData.bindData(rs,new MemberVO());
							BoardVO boardVO = BindData.bindData(rs, new BoardVO());
							boardVO.setMemberVO(memberVO);
							
							return boardVO;
						}});
	}

	@Override
	public int deleteOneBoard(int id) {
		// TODO Auto-generated method stub
		return this.jdbcTemplate.update(
				this.boardQueries.get(Query.DELETE_QUERY)
				, id
				);
	}

	@Override
	public List<BoardVO> selectAllBoards(BoardSearchVO boardSearchVO) {
		return this.jdbcTemplate.query(
				this.boardQueries.get(Query.SELECT_ALL_QUERY)
				, new RowMapper<BoardVO>() {

					@Override
					public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						MemberVO memberVO = BindData.bindData(rs,new MemberVO());
						BoardVO boardVO = BindData.bindData(rs, new BoardVO());
						boardVO.setMemberVO(memberVO);
						
						return boardVO;
					}});
	}

	@Override
	public int selectAllBoardsCount(BoardSearchVO boardSearchVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}









