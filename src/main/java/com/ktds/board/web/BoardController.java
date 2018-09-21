package com.ktds.board.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.board.service.BoardService;
import com.ktds.board.vo.BoardSearchVO;
import com.ktds.board.vo.BoardVO;
import com.ktds.common.exceptions.PolicyViolationException;
import com.ktds.common.session.Session;
import com.ktds.common.web.DownloadUtil;
import com.ktds.member.vo.MemberVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

@Controller
public class BoardController {
	
	//private Logger logger = LoggerFactory.getLogger(BoardController.class);
	private Logger statisticslogger = LoggerFactory.getLogger("list.statistics");
	private Logger paramLogger = LoggerFactory.getLogger(BoardController.class);
	
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	@Qualifier("boardServiceImpl")
	private BoardService boardService;
	
	
	//init을 요청하면 검색창 초기화하기
	@RequestMapping("/board/list/init")
	public String viewBoardListPageForInitiate(
							HttpSession session) {
		session.removeAttribute(Session.SEARCH);
		
		return "redirect:/board/list";
	}
	
	
	
	@RequestMapping("/board/list")
	public ModelAndView viewBoardListPage(
						@ModelAttribute BoardSearchVO boardSearchVO
						, HttpServletRequest request
						, HttpSession session
						) {
		
		//전체검색 or 상세에서 목록으로 넘어가거나 or 글쓰기
		if ( boardSearchVO.getSearchKeyword() == null) {
			boardSearchVO = (BoardSearchVO)session.getAttribute(Session.SEARCH);
			
			//최초 검색일 경우 무조건 널 값을 가지기 때문에
			if ( boardSearchVO == null ) {
				boardSearchVO = new BoardSearchVO();
				boardSearchVO.setPageNo(0);
			}
			
		}
		
		
		//Integer.parseInt("ss");
		PageExplorer pageExplorer = this.boardService.readAllBoards(boardSearchVO);
		
		
		statisticslogger.info("URL : /board/list, IP: "
				+ request.getRemoteAddr()
				+ ", List size: "
				+ pageExplorer.getList().size());
		
		//이전 검색어를 넣어준다
		session.setAttribute(Session.SEARCH, boardSearchVO);
		
		ModelAndView  view = new ModelAndView("board/list");
		view.addObject("boardVOList", pageExplorer.getList());
		view.addObject("pagenation", pageExplorer.make());
		view.addObject("size", pageExplorer.getTotalCount());
		view.addObject("boardSearchVO", boardSearchVO);
		return view;
	}
	
	
	// Spring 4.0 이하에서 사용
	// @RequestMapping(value="/write", method=RequestMethod.GET)
	
	// Spring 4.0 이상에서 사용
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
			
	// Sessioninterceptor 클래스로 인해 필요가 없어졌다.
/*			@SessionAttribute(name="_USER_", required=false) MemberVO memberVO) {
		
		
		
		//로그인을 하지 않았다면
		if( memberVO == null ) {
			return "redirect:/member/login";
		}*/
		
		return "board/write";
	}
	
	
		
	//폼데이터를 command object라고 부른다
	@PostMapping("/board/write")
	public ModelAndView doBoardWriteAction(
					@Valid@ModelAttribute BoardVO boardVO
					, Errors errors
					, HttpServletRequest request
					, @SessionAttribute(Session.USER) MemberVO memberVO) {
		
		ModelAndView view = new ModelAndView("redirect:/board/list/init");
		
		
		//Validation Annotation이 실패했는지 체크
		if ( errors.hasErrors() ) {
			view.setViewName("board/write");
			view.addObject("boardVO", boardVO);
			return view;
		}
		
		MultipartFile uploadFile = boardVO.getFile();
		
		if( !uploadFile.isEmpty() ) {
			String originFileName = uploadFile.getOriginalFilename();
			
			String fileName = UUID.randomUUID().toString();
			
			//폴더가 존재하지 않는다면 폴더 생성
			File uploadDir = new File(this.uploadPath);
			if ( !uploadDir.exists() ) {
				uploadDir.mkdirs();
			}
			
			//파일이 업로드될 경로 지정
			
			File destFile = new File(this.uploadPath, fileName);
			
			try {
				//업로드
				uploadFile.transferTo(destFile);
				// DB에 파일 정보 저장하기 위한 정보 세팅
				boardVO.setOriginFileName(originFileName);
				boardVO.setFileName(fileName);
			} catch (IllegalStateException | IOException e) {
				
				throw new RuntimeException(e.getMessage(),e);
			}
		}
		
		//4.3버전 미만에서 쓰는 방법
		//오브젝트로 리턴하기 때문에 MemberVO로 캐스팅을 한다.

		boardVO.setEmail(memberVO.getEmail());
		boardVO.setMemberVO(memberVO);
		
		boolean isSuccess = this.boardService.createBoard(boardVO, memberVO);

		
		String paramFormat = "IP: %s, Param: %s, Result:%s ";
		paramLogger.debug( String.format(paramFormat
							, request.getRemoteAddr()
							, boardVO.getSubject() + ", "
								+ boardVO.getContent() + ", "
								+ boardVO.getEmail()
								+ boardVO.getFile()
								+ boardVO.getOriginFileName()
							, view.getViewName()
							));
		
		  if ( isSuccess ) {
		         return view;         
		      }
		      else {
		         return view;
		      }
	}
	
	
	//http://localhost:8080/HelloSpring/board/detail/?id=1
	@RequestMapping("/board/detail/{id}")
	public ModelAndView viewBoardDetailPage(
					@PathVariable int id
					, HttpServletRequest request
					, @SessionAttribute(Session.USER) MemberVO memberVO) {
		

		BoardVO boardVO = this.boardService.readOneBoard(id, memberVO);
		ModelAndView view = new ModelAndView("board/detail");
		view.addObject("boardVO", boardVO);
		
		String paramFormat = "IP: %s, Param: %s, Result:%s ";
		paramLogger.debug( String.format(paramFormat
							, request.getRemoteAddr()
							, id
							, "id: " + boardVO.getId() + ", "
								+ "subject: " + boardVO.getSubject() + ", "
								+ "content: " + boardVO.getContent() + ", "
								+ "email: " + boardVO.getEmail() + ", "
								+ "file: " + boardVO.getFileName() + ", "
								+ "orginFile: " + boardVO.getOriginFileName()
							, view
							));
		
		return view;
	}
	
	
	//다른  url로 이동해야 하기 때문에 String
	@RequestMapping("/board/delete/{id}")
	public String doBoardDeleteAction( @PathVariable int id
			, HttpServletRequest request
			, @SessionAttribute(Session.USER) MemberVO memberVO) {
		
		boolean isSuccess = boardService.deleteOneBoard(id);
		
		String paramFormat = "IP: %s, Actor: %s Param: %s, Result:%s ";
		paramLogger.debug( String.format(paramFormat
							, request.getRemoteAddr()
							, memberVO.getEmail()
							, id
							, isSuccess
							));
		
		
		return "redirect:/board/list";
	}
	
	@RequestMapping("/board/download/{id}")
	public void fileDownLoad(
					@PathVariable int id
					, HttpServletRequest request
					, HttpServletResponse response
					, @SessionAttribute(Session.USER) MemberVO memberVO) {
		
		if( memberVO.getPoint() < 5) {
			throw new PolicyViolationException("다운로드 포인트 부족", "/board/detail/"+id);
		}
		
		BoardVO boardVO = this.boardService.readOneBoard(id);
		String originFileName = boardVO.getOriginFileName();
		String fileName = boardVO.getFileName();
		
		try {
			new DownloadUtil(this.uploadPath + File.separator + fileName)
			.download(request, response, originFileName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
}
















