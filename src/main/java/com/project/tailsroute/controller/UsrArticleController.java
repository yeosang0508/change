package com.project.tailsroute.controller;

import com.project.tailsroute.service.ArticleService;
import com.project.tailsroute.service.BoardService;
import com.project.tailsroute.service.ReactionPointService;
import com.project.tailsroute.service.ReplyService;
import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;
import java.util.Map;

@Controller
public class UsrArticleController {

	private final Rq rq;

	public UsrArticleController(Rq rq) {
		this.rq = rq;
	}

	@Autowired
	private ArticleService articleService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private ReactionPointService reactionPointService;

	@Autowired
	private ReplyService replyService;

	@GetMapping("/usr/article/detail")
	public String showDetail(Model model, int id) {

		boolean isLogined = rq.isLogined();
		if (isLogined) {
			Member member = rq.getLoginedMember();
			model.addAttribute("member", member);
		}

		model.addAttribute("isLogined", isLogined);

		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);

		if (article == null) {
			return "redirect:/usr/article/list";
		}

		// System.err.println(id + "번 글");
		// System.err.println(rq.getLoginedMemberId() + "번 회원 접속중");
		// System.err.println("내용" + article);

		model.addAttribute("article", article);

		List<Reply> replies = replyService.getForPrintReplies(rq.getLoginedMemberId(), "article", id);

		int repliesCount = replies.size();
		model.addAttribute("replies", replies);
		model.addAttribute("repliesCount", repliesCount);

		model.addAttribute("isAlreadyAddGoodRp",
				reactionPointService.isAlreadyAddGoodRp(rq.getLoginedMemberId(), id, "article"));
		model.addAttribute("isAlreadyAddBadRp",
				reactionPointService.isAlreadyAddBadRp(rq.getLoginedMemberId(), id, "article"));

		return "usr/article/detail";
	}

	@PostMapping("/usr/article/doIncreaseHitCountRd")
	@ResponseBody
	public ResultData doIncreaseHitCount(int id) {

		ResultData increaseHitCountRd = articleService.increaseHitCount(id);

		if (increaseHitCountRd.isFail()) {
			return increaseHitCountRd;
		}

		ResultData rd = ResultData.newData(increaseHitCountRd, "hitCount", articleService.getArticleHitCount(id));

		rd.setData2("조회수가 증가된 게시글의 id", id);

		return rd;
	}

	@GetMapping("/usr/article/modify")
	public String showModify(Model model, int id) {

		boolean isLogined = rq.isLogined();
		if (isLogined) {
			Member member = rq.getLoginedMember();
			model.addAttribute("member", member);
		}else {
			return "redirect:/usr/member/login";
		}
		model.addAttribute("isLogined", isLogined);

		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);


		ResultData userCanModifyRd = articleService.userCanModify(rq.getLoginedMemberId(), article);


		if (article == null || userCanModifyRd.isFail()) {
			return "redirect:/usr/article/list";
		}

		model.addAttribute("article", article);

		return "/usr/article/modify";
	}

	// 로그인 체크 -> 유무 체크 -> 권한 체크 -> 수정
	@PostMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body) {

		Article article = articleService.getArticleById(id);

		if (article == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 게시글은 없습니다", id));
		}

		ResultData userCanModifyRd = articleService.userCanModify(rq.getLoginedMemberId(), article);

		if (userCanModifyRd.isFail()) {
			return Ut.jsHistoryBack(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg());
		}

		if (userCanModifyRd.isSuccess()) {
			articleService.modifyArticle(id, title, body);
		}

		article = articleService.getArticleById(id);

		return Ut.jsReplace(userCanModifyRd.getResultCode(), userCanModifyRd.getMsg(), "../article/detail?id=" + id);
	}

	@GetMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		Article article = articleService.getArticleById(id);

		if (article == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 게시글은 없습니다", id));
		}

		ResultData userCanDeleteRd = articleService.userCanDelete(rq.getLoginedMemberId(), article);

		if (userCanDeleteRd.isFail()) {
			return Ut.jsHistoryBack(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg());
		}

		if (userCanDeleteRd.isSuccess()) {
			articleService.deleteArticle(id);
		}

		return Ut.jsReplace(userCanDeleteRd.getResultCode(), userCanDeleteRd.getMsg(), "../article/list");
	}

	@GetMapping("/usr/article/write")
	public String showWrite(Model model) {

		boolean isLogined = rq.isLogined();
		if (isLogined) {
			Member member = rq.getLoginedMember();
			model.addAttribute("member", member);
		}else{
			return "redirect:/usr/member/login";
		}
		model.addAttribute("isLogined", isLogined);

		Integer currentId = articleService.getCurrentArticleId();
		if (currentId == null) {
			currentId = 1;
		}

		model.addAttribute("currentId", currentId);

		return "usr/article/write";
	}

	@PostMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(String boardId, String title, String body, String replaceUri,
			MultipartRequest multipartRequest) {

		if (Ut.isEmptyOrNull(title)) {
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.isEmptyOrNull(body)) {
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}
		if (Ut.isEmptyOrNull(boardId)) {
			return Ut.jsHistoryBack("F-3", "게시판을 선택해주세요");
		}

		System.err.println(boardId);

		ResultData writeArticleRd = articleService.writeArticle(rq.getLoginedMemberId(), title, body, boardId);

		int id = (int) writeArticleRd.getData1();

		Article article = articleService.getArticleById(id);

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
		}

		return Ut.jsReplace(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), "../article/detail?id=" + id);

	}

	@GetMapping("/usr/article/list")
	public String showList(Model model, @RequestParam(defaultValue = "0") int boardId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "전체") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword){

		boolean isLogined = rq.isLogined();
		if (isLogined) {
			Member member = rq.getLoginedMember();
			model.addAttribute("member", member);
		}
		model.addAttribute("isLogined", isLogined);

		Board board = boardService.getBoardById(boardId);

		int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);

		// 한페이지에 글 10개
		// 글 20개 -> 2page
		// 글 25개 -> 3page
		int itemsInAPage = 10;

		int pagesCount = (int) Math.ceil(articlesCount / (double) itemsInAPage);

		List<Article> articles = articleService.getForPrintArticles(boardId, itemsInAPage, page, searchKeywordTypeCode,
				searchKeyword);

		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("board", board);
		model.addAttribute("page", page);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("boardId", boardId);

		return "usr/article/list";
	}
}
