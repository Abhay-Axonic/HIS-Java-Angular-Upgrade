package com.param.linenLaundry.linenIssue.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenIndent;
import com.param.entity.model.linenAndLaundry.LinenIndentDetail;
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIssueDao {
	
	List<LinenIssue> searchLinenIssue(LinenRequest storeIndentRequest, Integer screenId);

	LinenIssue saveLinenIssue(LinenIssue linenIssue);
	
	LinenIssue getLinenIssue(Integer id);
	
	List<LinenIssueDetail> getLinenIssueDetail(Integer linenIssueId);
	
	LinenIssue updateLinenIssue(LinenIssue linenIssue);
	
	List<LinenIssueBatchMapper> getLinenIssueBatchMapper(Integer linenIssueDetailId);

}