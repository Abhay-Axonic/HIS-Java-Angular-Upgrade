package com.param.linenLaundry.rejectedLinenAcceptance.dao;

import java.util.List;

import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectLinenResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenRequest;

public interface IRejectedLinenAcceptanceDao {

	List<RejectLinenResponse> searchRejectLinen(RejectedLinenRequest request, Integer screenId);
	
}
