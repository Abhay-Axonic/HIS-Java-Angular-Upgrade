package com.param.linenLaundry.linenIndent.dao;

import java.util.List;

import com.param.entity.model.linenAndLaundry.LinenIndent;
import com.param.entity.model.linenAndLaundry.LinenIndentDetail;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

public interface ILinenIndentDao {
	
	List<LinenIndent> searchLinenIndent(LinenRequest storeIndentRequest, Integer screenId);

	LinenIndent saveLinenIndent(LinenIndent linenIndent);
	
	LinenIndent getLinenIndent(Integer id);
	
	List<LinenIndentDetail> getLinenIndentDetail(Integer linenIndentId);
	
	LinenIndent updateLinenIndent(LinenIndent linenIndent);

}