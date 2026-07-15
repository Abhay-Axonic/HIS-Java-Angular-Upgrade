package com.param.linenLaundry.linenReturn.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.entity.model.linenAndLaundry.LinenReturn;
import com.param.entity.model.linenAndLaundry.LinenReturnDetail;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;

public interface ILinenReturnDao {
	
	List<LinenResponse> searchLinenReturn(LinenRequest storeIndentRequest, Integer screenId);

	LinenReturn saveLinenReturn(LinenReturn linenReturn);
	
	LinenResponse getLinenReturn(Integer id);
	
	LinenReturn	getLinenReturnId(Integer id);
	
	List<LinenReturnDetail> getLinenReturnDetail(Integer linenReturnId);
	
	LinenReturn updateLinenReturn(LinenReturn linenReturn);
	
	List<LinenIssueBatchMapper> getLinenIssueBatchMapper(Integer linenIssueDetailId);

}