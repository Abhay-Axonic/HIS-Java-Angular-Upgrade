package com.param.linenLaundry.linenIssueList.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenIndent;
import com.param.entity.model.linenAndLaundry.LinenIndentDetail;
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueListDao {
	
	List<LinenIssue> searchLinenIssueList(LinenRequest storeIndentRequest, Integer screenId);

	LinenIssue saveLinenIssueList(LinenIssue linenIssue);
	
	LinenIssue getLinenIssueList(Integer id);
	
	List<ItemResponse> getLinenIssueListDetail(Integer linenIssueId);
	
	LinenIssue updateLinenIssueList(LinenIssue linenIssue);
	
	List<LinenIssueBatchMapper> getLinenIssueBatchMapper(Integer linenIssueDetailId);

}