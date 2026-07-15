package com.param.linenLaundry.linenIssueAcceptance.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.entity.model.linenAndLaundry.RejectedAcceptance;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueAcceptanceDao {
	
	List<LinenIssue> searchLinenIssueAcceptance(LinenRequest storeIndentRequest, Integer screenId);

	LinenIssue saveLinenIssueAcceptance(LinenIssue linenIssue);
	
	LinenIssue getLinenIssueAcceptance(Integer id);
	
	List<LinenIssueDetail> getLinenIssueAcceptanceDetails(Integer issueId);
	
	List<ItemResponse> getLinenIssueAcceptanceDetail(Integer linenIssueId);
	
	LinenIssue updateLinenIssueAcceptance(LinenIssue linenIssue);
	
	List<LinenIssueBatchMapper> getLinenIssueBatchMapper(Integer linenIssueDetailId);
	
	RejectedAcceptance saveRejectedAcceptance(RejectedAcceptance rejectedAcceptance);

}