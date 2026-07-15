package com.param.linenLaundry.linenReturnAcceptance.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenReturn;
import com.param.entity.model.linenAndLaundry.LinenReturnDetail;
import com.param.entity.model.linenAndLaundry.RejectedAcceptance;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;

public interface ILinenReturnAcceptanceDao {
	
	List<LinenResponse> searchLinenReturnAcceptance(LinenRequest storeIndentRequest, Integer screenId);

	LinenReturn saveLinenReturnAcceptance(LinenReturn linenReturn);
	
	LinenResponse getLinenReturnAcceptance(Integer id);
	
	LinenReturn	getLinenReturnAcceptanceId(Integer id);
	
	LinenReturnDetail getLinenReturnDetail(Integer id);
	
	List<LinenReturnDetail> getLinenReturnAcceptanceDetail(Integer linenReturnId);
	
	LinenReturn updateLinenReturnAcceptance(LinenReturn linenReturn);
	
	RejectedAcceptance saveRejectAcceptance(RejectedAcceptance rejectAcceptance);

}