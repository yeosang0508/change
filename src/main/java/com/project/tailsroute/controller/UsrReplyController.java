package com.project.tailsroute.controller;


import com.project.tailsroute.service.ReactionPointService;
import com.project.tailsroute.service.ReplyService;
import com.project.tailsroute.util.Ut;
import com.project.tailsroute.vo.Reply;
import com.project.tailsroute.vo.ResultData;
import com.project.tailsroute.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsrReplyController {

	private final Rq rq;

	public UsrReplyController(Rq rq) {
		this.rq = rq;
	}

	@Autowired
	private ReactionPointService reactionPointService;

	@Autowired
	private ReplyService replyService;

	@PostMapping("/usr/reply/doWrite")
	@ResponseBody
	public String doWrite(String relTypeCode, int relId, String body) {

		if (Ut.isEmptyOrNull(body)) {
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		ResultData writeReplyRd = replyService.writeReply(rq.getLoginedMemberId(), body, relTypeCode, relId);

		int id = (int) writeReplyRd.getData1();

		return Ut.jsReplace(writeReplyRd.getResultCode(), writeReplyRd.getMsg(), "../article/detail?id=" + relId);
	}

	@PostMapping("/usr/reply/doModify")
	@ResponseBody
	public String doModify(int id, String body) {
		// System.err.println(id);
		// System.err.println(body);

		Reply reply = replyService.getReply(id);

		if (reply == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 댓글은 존재하지 않습니다", id));
		}

		ResultData loginedMemberCanModifyRd = replyService.userCanModify(rq.getLoginedMemberId(), reply);

		if (loginedMemberCanModifyRd.isSuccess()) {
			replyService.modifyReply(id, body);
		}

		reply = replyService.getReply(id);

		return reply.getBody();
	}

	@PostMapping("/usr/reply/doDelete")
	@ResponseBody
	public String doDelete(int id) {

		Reply reply = replyService.getReply(id);

		if (reply == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 댓글은 존재하지 않습니다", id));
		}

		ResultData loginedMemberCanDeleteRd = replyService.userCanDelete(rq.getLoginedMemberId(), reply);

		if (loginedMemberCanDeleteRd.isSuccess()) {
			replyService.deleteReply(id);
		}

		return "success";
	}

}
