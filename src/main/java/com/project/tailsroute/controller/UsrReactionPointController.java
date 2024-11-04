package com.project.tailsroute.controller;

import com.project.tailsroute.service.ArticleService;
import com.project.tailsroute.service.ReactionPointService;
import com.project.tailsroute.service.ReplyService;
import com.project.tailsroute.vo.ResultData;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsrReactionPointController {

	private final Rq rq;

	public UsrReactionPointController(Rq rq) {
		this.rq = rq;
	}

	@Autowired
	private ReactionPointService reactionPointService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ReplyService replyService;

	@PostMapping("/usr/reactionPoint/doGoodReaction")
	@ResponseBody
	public ResultData doGoodReaction(String relTypeCode, int relId) {

		// System.err.println("relTypeCode : "+relTypeCode);
		// System.err.println("relId : "+relId);

		ResultData usersReactionRd = reactionPointService.usersReaction(rq.getLoginedMemberId(), relTypeCode, relId);

		int usersReaction = (int) usersReactionRd.getData1();

		int goodRP = 0;
		int badRP = 0;

		if (usersReaction == 1) {
			ResultData rd = reactionPointService.deleteGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			if(relTypeCode.equals("article")){
				goodRP = articleService.getGoodRP(relId);
				badRP = articleService.getBadRP(relId);
			}
			else if(relTypeCode.equals("reply")){
				goodRP = replyService.getGoodRP(relId);
				badRP = replyService.getBadRP(relId);
			}
			return ResultData.from("S-1", "좋아요 취소", "goodRP", goodRP, "badRP", badRP);
		} else if (usersReaction == -1) {
			ResultData rd = reactionPointService.deleteBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			rd = reactionPointService.addGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			if(relTypeCode.equals("article")){
				goodRP = articleService.getGoodRP(relId);
				badRP = articleService.getBadRP(relId);
			}
			else if(relTypeCode.equals("reply")){
				goodRP = replyService.getGoodRP(relId);
				badRP = replyService.getBadRP(relId);
			}
			return ResultData.from("S-2", "싫어요 했었음", "goodRP", goodRP, "badRP", badRP);
		}

		ResultData reactionRd = reactionPointService.addGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);

		if (reactionRd.isFail()) {
			return ResultData.from(reactionRd.getResultCode(), reactionRd.getMsg());
		}

		if(relTypeCode.equals("article")){
			goodRP = articleService.getGoodRP(relId);
			badRP = articleService.getBadRP(relId);
		}
		else if(relTypeCode.equals("reply")){
			goodRP = replyService.getGoodRP(relId);
			badRP = replyService.getBadRP(relId);
		}

		return ResultData.from(reactionRd.getResultCode(), reactionRd.getMsg(), "goodRP", goodRP, "badRP", badRP);
	}

	@PostMapping("/usr/reactionPoint/doBadReaction")
	@ResponseBody
	public ResultData doBadReaction(String relTypeCode, int relId, String replaceUri) {

		ResultData usersReactionRd = reactionPointService.usersReaction(rq.getLoginedMemberId(), relTypeCode, relId);

		int usersReaction = (int) usersReactionRd.getData1();

		if (usersReaction == -1) {
			ResultData rd = reactionPointService.deleteBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			int goodRP = articleService.getGoodRP(relId);
			int badRP = articleService.getBadRP(relId);
			return ResultData.from("S-1", "싫어요 취소", "goodRP", goodRP, "badRP", badRP);
		} else if (usersReaction == 1) {
			ResultData rd = reactionPointService.deleteGoodReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			rd = reactionPointService.addBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);
			int goodRP = articleService.getGoodRP(relId);
			int badRP = articleService.getBadRP(relId);
			return ResultData.from("S-2", "좋아요 했었음", "goodRP", goodRP, "badRP", badRP);
		}

		ResultData reactionRd = reactionPointService.addBadReactionPoint(rq.getLoginedMemberId(), relTypeCode, relId);

		if (reactionRd.isFail()) {
			return ResultData.from(reactionRd.getResultCode(), reactionRd.getMsg());
		}

		int goodRP = articleService.getGoodRP(relId);
		int badRP = articleService.getBadRP(relId);

		return ResultData.from(reactionRd.getResultCode(), reactionRd.getMsg(), "goodRP", goodRP, "badRP", badRP);
	}

}
